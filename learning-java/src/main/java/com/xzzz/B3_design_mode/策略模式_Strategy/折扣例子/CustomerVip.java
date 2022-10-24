package com.xzzz.B3_design_mode.策略模式_Strategy.折扣例子;

import java.math.BigDecimal;

public class CustomerVip implements IQuoteStrategy {
    @Override
    public BigDecimal getPrice(BigDecimal originalPrice) {
        System.out.println("恭喜！VIP客户享有8折优惠！");
        originalPrice = originalPrice.multiply(new BigDecimal(0.8)).setScale(2, BigDecimal.ROUND_HALF_UP);
        return originalPrice;
    }
}
