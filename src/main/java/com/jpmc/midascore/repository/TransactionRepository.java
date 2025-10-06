package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionRecord, Long> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionRecord t WHERE t.sender.id = :userId")
    float getTotalSent(long userId);

    @Query("SELECT COALESCE(SUM(t.amount + t.incentive), 0) FROM TransactionRecord t WHERE t.recipient.id = :userId")
    float getTotalReceived(long userId);
}
