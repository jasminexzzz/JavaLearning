package com.jasmine.设计模式.策略模式_Strategy.折扣例子;

import java.math.BigDecimal;

public class CustomerNew implements IQuoteStrategy {
    @Override
    public BigDecimal getPrice(BigDecimal originalPrice) {
        System.out.println("抱歉！新客户没有折扣！");
        return originalPrice;
    }
}
