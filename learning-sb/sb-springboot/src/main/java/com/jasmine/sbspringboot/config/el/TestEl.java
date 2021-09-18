package com.jasmine.sbspringboot.config.el;

import lombok.Data;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Component
public class TestEl {

    public static void main(String[] args) {
// Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

//  The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla");

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'_' + name");
        EvaluationContext context = new StandardEvaluationContext(tesla);

        String name = (String) exp.getValue(context);
        System.out.println(name);

        tesla.setName("王云飞");
        String name1 = (String) exp.getValue(context);
        System.out.println(name1);


    }

    @Data
    public static class Inventor {
        private String name;

        public Inventor(String name) {
            this.name = name;
        }
    }

}
