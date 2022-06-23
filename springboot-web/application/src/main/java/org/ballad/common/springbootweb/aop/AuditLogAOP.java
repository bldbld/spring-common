package org.ballad.common.springbootweb.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballad.common.springbootweb.mybatisplus.bean.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditLogAOP {

    @Value("${spring.application.name}")
    private String applicationName; //从配置文件获得服务名


    @Pointcut("@@annotation(enableAuditLog)")
    public void loggerAop1() {}

    @Pointcut("@within(enableAuditLog)")
    public void loggerAop2() {}

    // spel表达式解析器
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    // 参数名发现器
    private DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Before(value = "loggerAop1() || loggerAop2()")
    public void getAutiLogInfo(JoinPoint joinPoint, AuditLog enableAuditLog) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (enableAuditLog == null) {
            enableAuditLog = signature.getMethod().getAnnotation(AuditLog.class);
        }



        // 构建日志存储对象
        Log auditlog = Log.builder().applicationName(applicationName).createTime(LocalDateTime.now()).build();

        auditlog.setUserId("");  // 从上下文获取当前操作的用户信息
        auditlog.setUserNickname("");

        // 设置操作的接口方法名
        auditlog.setInterfaceName(signature.getDeclaringTypeName() + "." + signature.getName());

        Object[] params = joinPoint.getArgs();
        Annotation[][] annotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Object param = params[i];
            Annotation[] paramAnn = annotations[i];
            System.out.println("paramAnn: "+ paramAnn);
            //参数为空，直接下一个参数
            if(param == null || paramAnn.length == 0){
                continue;
            }
            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为Test.class
                if(annotation.annotationType().equals(PostMapping.class)){
                    //校验该参数，验证一次退出该注解
                    System.out.println("PostMapping: "+ annotation.toString());


                    break;
                }
            }
        }

        // 获得日志注解上自定义的日志信息
        String logInfo = enableAuditLog.logInfo();

        // Spel 表达式解析日志信息
        // 获得方法参数名数组
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(signature.getMethod());
        if (parameterNames != null && parameterNames.length > 0) {
            EvaluationContext context = new StandardEvaluationContext();

            //获取方法参数值
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                context.setVariable(parameterNames[i], args[i]); // 替换spel里的变量值为实际值， 比如 #user -->  user对象
            }

            // 解析出实际的日志信息
            String opeationInfo = spelExpressionParser.parseExpression(logInfo).getValue(context).toString();
            auditlog.setOperationInfo(opeationInfo);
        }

        System.out.println("Before: "+ auditlog.toString());
        // 打印日志信息
        // log.info(auditlog.toString());

        //TODO 这时可以将日志信息auditlog进行异步存储,比如写入到文件通过logstash增量的同步到Elasticsearch或者DB
    }

    @AfterReturning(value = "@annotation(enableAuditLog) || @within(enableAuditLog)", returning = "resultObj")
    public void afterReturningProcessAutiLogInfo(JoinPoint joinPoint, AuditLog enableAuditLog, Object resultObj){
        // 切面方法执行成功后调用此方法
        System.out.println("AfterReturning");
    }

    @AfterThrowing(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
    public void ARExControllerOpLog(JoinPoint joinPoint, AuditLog enableAuditLog){
        // 切面方法执行异常后调用此方法
        System.out.println("Exception");
    }

    @After(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
    public void AfterControllerOpLog(JoinPoint joinPoint, AuditLog enableAuditLog){
        // 切面方法执行后，无论成功与否都会调用此方法
        System.out.println("After");
    }

    @Around(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
    public void WrapperControllerOpLog(ProceedingJoinPoint proceedingJoinPoint, AuditLog enableAuditLog) throws Throwable{
        // 将切到的方法包装起来，通过proceed执行业务代码，可在执行前后添加切面编程代码
        proceedingJoinPoint.proceed();
        System.out.println("Around");
    }
}
