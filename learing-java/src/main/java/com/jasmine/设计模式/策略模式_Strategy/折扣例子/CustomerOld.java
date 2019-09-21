package com.jasmine.设计模式.策略模式_Strategy.折扣例子;

import java.math.BigDecimal;

public class CustomerOld implements IQuoteStrategy {
    @Override
    public BigDecimal getPrice(BigDecimal originalPrice) {
        System.out.println("恭喜！老客户享有9折优惠！");
        originalPrice = originalPrice.multiply(new BigDecimal(0.9)).setScale(2, BigDecimal.ROUND_HALF_UP);
        return originalPrice;
    }
}
