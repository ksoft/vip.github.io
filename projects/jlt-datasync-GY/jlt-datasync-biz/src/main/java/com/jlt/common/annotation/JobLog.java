package com.jlt.common.annotation;

import com.jlt.common.enums.JobType;

import java.lang.annotation.*;

/**
 * @author Billy.Zhang
 * @date 2018/9/26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobLog {
    /**
     * jobType
     * @return
     */
    JobType jobTye() default  JobType.READ;
}
