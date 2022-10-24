package com.jasmine.B3_design_mode.策略模式_Strategy.折扣例子;

import java.math.BigDecimal;

public class CustomerMVP implements IQuoteStrategy {
    @Override
    public BigDecimal getPrice(BigDecimal originalPrice) {
        System.out.println("哇偶！MVP客户享受7折优惠！！！");
        originalPrice = originalPrice.multiply(new BigDecimal(0.7)).setScale(2,BigDecimal.ROUND_HALF_UP);
        return originalPrice;
    }
}
