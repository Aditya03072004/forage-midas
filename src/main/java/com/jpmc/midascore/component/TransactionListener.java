package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private RestTemplate restTemplate;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    @Transactional
    public void listen(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);

        UserRecord sender = userRepo.findById(transaction.getSenderId()).orElse(null);
        if (sender == null) {
            logger.warn("Invalid sender ID: {}", transaction.getSenderId());
            return;
        }

        UserRecord recipient = userRepo.findById(transaction.getRecipientId()).orElse(null);
        if (recipient == null) {
            logger.warn("Invalid recipient ID: {}", transaction.getRecipientId());
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance for sender {}: {} < {}", sender.getName(), sender.getBalance(), transaction.getAmount());
            return;
        }

        // Get incentive from API
        Incentive incentive;
        try {
            incentive = restTemplate.postForObject("http://localhost:8080/incentive", transaction, Incentive.class);
            if (incentive == null) {
                incentive = new Incentive(0f);
            }
        } catch (Exception e) {
            logger.warn("Failed to retrieve incentive from API: {}", e.getMessage());
            incentive = new Incentive(0f);
        }

        // Valid transaction
        TransactionRecord tr = new TransactionRecord(sender, recipient, transaction.getAmount(), incentive.getAmount());
        transactionRepo.save(tr);
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentive.getAmount());
        userRepo.save(sender);
        userRepo.save(recipient);

        logger.info("Transaction processed successfully: {}", tr);
    }
}
