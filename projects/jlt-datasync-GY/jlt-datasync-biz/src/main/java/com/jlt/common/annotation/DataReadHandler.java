package com.jlt.common.annotation;

import java.lang.annotation.*;

/**
 * @author Billy.Zhang
 * @date 2018/9/26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataReadHandler {
    /**
     * value = jlt_job_detail.table_name
     *
     * @return
     */
    String value() default "";
}
