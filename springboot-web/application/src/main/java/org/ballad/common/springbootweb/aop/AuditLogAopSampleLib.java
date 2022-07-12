package org.ballad.common.springbootweb.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 修改 @EnableAuditLog 中的导入AOP类来进行测试
 *
 */
@Aspect
@Component
public class AuditLogAopSampleLib {


    /**
     * 从配置文件获得服务名
     */
    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Pointcut("@annotation(org.ballad.common.springbootweb.aop.EnableAuditLog)")
    public void loggerAopAnnotation() {
    }

    @Pointcut("@within(org.ballad.common.springbootweb.aop.EnableAuditLog)")
    public void loggerAopWithin() {
    }

    @Pointcut("execution(*  *..*.*.controller..*.*(..))")
    public void loggerAopController() {
    }


    @Before(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
    public void beforeAutiLogInfo(JoinPoint joinPoint, AuditLog enableAuditLog) {
        System.out.println("Before");
    }

    @AfterReturning(value = "@annotation(enableAuditLog) || @within(enableAuditLog)", returning = "resultObj")
    public void afterReturningProcessAutiLogInfo(JoinPoint joinPoint, AuditLog enableAuditLog, Object resultObj) {
        // 切面方法执行成功后调用此方法
        System.out.println("AfterReturning");
    }

    @AfterThrowing(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
    public void ARExControllerOpLog(JoinPoint joinPoint, AuditLog enableAuditLog) {
        // 切面方法执行异常后调用此方法
        System.out.println("Exception");
    }

    @After(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
    public void AfterControllerOpLog(JoinPoint joinPoint, AuditLog enableAuditLog) {
        // 切面方法执行后，无论成功与否都会调用此方法
        System.out.println("After");
    }

    @Around(value = "loggerAopController()")
    public Object WrapperControllerOpLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 将切到的方法包装起来，通过proceed执行业务代码，可在执行前后添加切面编程代码
        Object result = proceedingJoinPoint.proceed();
        System.out.println("Around");
        return result;
    }
}
