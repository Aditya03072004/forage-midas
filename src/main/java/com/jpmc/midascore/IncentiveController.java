package com.jpmc.midascore;

import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;

@Component
public class IncentiveController {

    public Incentive getIncentive(Transaction transaction) {
        float incentiveAmount = transaction.getAmount() * 0.1f;
        return new Incentive(incentiveAmount);
    }
}
