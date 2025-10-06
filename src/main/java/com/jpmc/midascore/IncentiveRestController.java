package com.jpmc.midascore;

import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IncentiveRestController {

    @PostMapping("/incentive")
    public Incentive getIncentive(@RequestBody Transaction transaction) {
        float incentiveAmount = transaction.getAmount() * 0.1f;
        return new Incentive(incentiveAmount);
    }
}
