package com.codingshuttle.airBnbApp.strategy;

import com.codingshuttle.airBnbApp.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);

}
