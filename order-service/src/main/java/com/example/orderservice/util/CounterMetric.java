package com.example.orderservice.util;



import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CounterMetric {
    String name();

    String[] tags() default {};

    String totalSuffix() default ".total";
    String successSuffix() default ".success";
    String errorSuffix() default ".error";
}
