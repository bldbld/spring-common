package org.ballad.common.springbootweb.aop;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ballad
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({AuditLogAop.class}) // 注入AOP切面到容器
public @interface EnableAuditLog {

}
