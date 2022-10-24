package com.xzzz.B3_design_mode.策略模式_Strategy.折扣例子;

import java.math.BigDecimal;

public interface IQuoteStrategy {
    //获取折后价的价格
    BigDecimal getPrice(BigDecimal originalPrice);
}
