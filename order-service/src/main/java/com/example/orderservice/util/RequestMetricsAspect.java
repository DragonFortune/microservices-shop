package com.example.orderservice.util;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "metrics.enabled", havingValue = "true", matchIfMissing = true)
public class RequestMetricsAspect {

    private final MeterRegistry meterRegistry;

    public Object logRequestMetrics(ProceedingJoinPoint joinPoint, CounterMetric metric) throws Throwable {
        String metricName = metric.name();
        String[] tags = metric.tags();

        String totalMetric = metricName + metric.totalSuffix();
        String successMetric = metricName + metric.successSuffix();
        String errorMetric = metricName + metric.errorSuffix();

        meterRegistry.counter(totalMetric, tags).increment();
        
        try {
            Object result = joinPoint.proceed();
            meterRegistry.counter(successMetric, tags).increment();
            return result;
        } catch (Throwable e) {
            meterRegistry.counter(errorMetric, tags).increment();
            throw  e;
        }
    }

}
