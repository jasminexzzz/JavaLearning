package com.xzzz.sbspringboot.config.el;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Component
@Aspect
@Slf4j
public class CustomElAspect {

    private ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();

//    @Pointcut("@annotation(com.jasmine.learingsb.config.el.CustomElAnnotation)")
//    private void exceptionHandleMethod() {
//    }

    @Around("@annotation(customElAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, CustomElAnnotation customElAnnotation) throws Throwable {
        log.error("捕获异常");
        String attachmentId = getAttachmentId(joinPoint); // 获取
        // 处理异常情况下的业务
        Object object = joinPoint.proceed(joinPoint.getArgs());
        return object;
    }

//    @AfterThrowing(value = "exceptionHandleMethod()", throwing = "ex")
//    public void doThrowing(JoinPoint joinPoint, Throwable ex) {
//        log.error("捕获异常");
//        String attachmentId = getAttachmentId(joinPoint); // 获取
//        // 处理异常情况下的业务
//    }

    private CustomElAnnotation getDistributeExceptionHandler(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(CustomElAnnotation.class);
    }

    private String getAttachmentId(JoinPoint joinPoint) {
        CustomElAnnotation handler = getDistributeExceptionHandler(joinPoint);
        if (joinPoint.getArgs() == null) {
            return null;
        }
        EvaluationContext evaluationContext = evaluator.createEvaluationContext(joinPoint.getTarget(), joinPoint.getTarget().getClass(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        AnnotatedElementKey methodKey = new AnnotatedElementKey(((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getTarget().getClass());
        return evaluator.condition(handler.attachmentId(), methodKey, evaluationContext, String.class);
    }
}
