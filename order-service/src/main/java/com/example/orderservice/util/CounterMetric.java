package com.example.orderservice.util;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CounterMetric {
    String name();

    String[] tags() default {};

    String totalSuffix() default "_total";
    String successSuffix() default "_success";
    String errorSuffix() default "_error";
}
