package com.xzzz.B3_design_mode.策略模式_Strategy.折扣例子;

import java.math.BigDecimal;

public class Client {
    public static void main(String[] args) {
         //1.创建老客户的报价策略
         IQuoteStrategy oldQuoteStrategy = new CustomerNew();

         //2.创建报价上下文对象，并设置具体的报价策略
         QuoteContext quoteContext = new QuoteContext(oldQuoteStrategy);

         //3.调用报价上下文的方法
         BigDecimal price = quoteContext.getPrice(new BigDecimal(100));

         System.out.println("折扣价为：" +price);
     }
}
