package org.ballad.common.springbootweb.aop;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义日志审计注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AuditLog {

    /**
     * 日志信息
     *
     * @return
     */
    String logInfo();
}
