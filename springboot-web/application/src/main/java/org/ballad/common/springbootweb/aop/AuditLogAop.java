package org.ballad.common.springbootweb.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballad.common.springbootweb.mybatisplus.bean.Log;
import org.ballad.common.springbootweb.mybatisplus.mapper.LogMapper;
import org.ballad.common.springbootweb.mybatisplus.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditLogAop {

    private static final Logger logger = LoggerFactory.getLogger("org.ballad.common.springbootweb");

    @Autowired
    private LogMapper logMapper;

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

    /**
     * Spel表达式解析器
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    /**
     * 参数名发现器
     */
    private DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("loggerAopController() || loggerAopWithin() || loggerAopAnnotation()")
    public void getAutiLogInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Enter Around");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        AuditLog enableAuditLog = signature.getMethod().getAnnotation(AuditLog.class);

        Object result = null;
        try {
            result = joinPoint.proceed();
            AuditLogIgnore ignoreLog = signature.getMethod().<AuditLogIgnore>getAnnotation(AuditLogIgnore.class);
            if (ignoreLog == null) {
                log(signature, enableAuditLog, joinPoint.getArgs(), null);
            }
        } catch (Throwable e) {
            log(signature, enableAuditLog, joinPoint.getArgs(), e);
            throw e;
        }
    }

    protected void log(MethodSignature signature, AuditLog enableAuditLog, Object[] args, Throwable exception) {
        // 构建日志存储对象
        Log auditlog = Log.builder().applicationName(applicationName).createTime(LocalDateTime.now()).build();

        // 从上下文获取当前操作的用户信息
        auditlog.setUserId("");
        auditlog.setUserNickname("");

        // 获取请求信息
        String clientCode = this.request.getHeader("User-Agent");
        String serverIp = this.request.getLocalAddr();
        String clientIp = this.request.getRemoteAddr();
        String requestMaping = this.request.getRequestURI();
        String tokenId = this.request.getHeader("x-auth-token");
        String uri = (this.request.getRequestURL() == null) ? "" : this.request.getRequestURL().toString();
        String requestBody = this.request.getQueryString();

        // 设置操作的接口方法名
        auditlog.setInterfaceName(signature.getDeclaringTypeName() + "." + signature.getName());


        Annotation[][] annotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {

            Annotation[] paramAnn = annotations[i];
            System.out.println("paramAnn: " + paramAnn);
            //参数为空，直接下一个参数
            if (args == null || paramAnn.length == 0) {
                continue;
            }
            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为Test.class
                if (annotation.annotationType().equals(PostMapping.class)) {
                    //校验该参数，验证一次退出该注解
                    System.out.println("PostMapping: " + annotation.toString());
                    break;
                }
            }
        }

        // 获得日志注解上自定义的日志信息
        String logInfo = enableAuditLog.logInfo();

        String operationInfo = null;
        // 获得方法参数名数组
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(signature.getMethod());
        if (parameterNames != null && parameterNames.length > 0) {
            EvaluationContext context = new StandardEvaluationContext();

            for (int i = 0; i < args.length; i++) {
                // 替换spel里的变量值为实际值， 比如 #user -->  user对象
                context.setVariable(parameterNames[i], args[i]);
            }

            // Spel 表达式解析日志信息
            // 解析出实际的日志信息
            operationInfo = spelExpressionParser.parseExpression(logInfo).getValue(context).toString();
            auditlog.setOperationInfo(operationInfo);
        }

        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(auditlog.getUserId()).append("\t");
        logBuilder.append(auditlog.getUserNickname()).append("\t");
        logBuilder.append(signature.getMethod().getName()).append("\t");
        logBuilder.append(requestMaping).append("\t");
        logBuilder.append(uri).append("\t");
        logBuilder.append(requestBody).append("\t");
        logBuilder.append(clientIp).append("\t");
        logBuilder.append(clientCode).append("\t");
        logBuilder.append(serverIp).append("\t");
        logBuilder.append(tokenId).append("\t");
        logBuilder.append(operationInfo);

        if (exception != null) {
            logger.error(logBuilder.toString(), exception);
        } else {
            logger.debug(logBuilder.toString());
            // log.info(logBuilder.toString());
        }
        System.out.println(logBuilder.toString());
        //TODO 这时可以将日志信息auditlog进行异步存储,比如写入到文件通过logstash增量的同步到Elasticsearch或者DB

        logMapper.insert(auditlog);
    }

// 以下为其他用法测试

//    @Before(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
//    public void beforeAutiLogInfo(JoinPoint joinPoint, AuditLog enableAuditLog) {
//        System.out.println("Before");
//    }

//    @AfterReturning(value = "@annotation(enableAuditLog) || @within(enableAuditLog)", returning = "resultObj")
//    public void afterReturningProcessAutiLogInfo(JoinPoint joinPoint, AuditLog enableAuditLog, Object resultObj) {
//        // 切面方法执行成功后调用此方法
//        System.out.println("AfterReturning");
//    }
//
//    @AfterThrowing(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
//    public void ARExControllerOpLog(JoinPoint joinPoint, AuditLog enableAuditLog) {
//        // 切面方法执行异常后调用此方法
//        System.out.println("Exception");
//    }
//
//    @After(value = "@annotation(enableAuditLog) || @within(enableAuditLog)")
//    public void AfterControllerOpLog(JoinPoint joinPoint, AuditLog enableAuditLog) {
//        // 切面方法执行后，无论成功与否都会调用此方法
//        System.out.println("After");
//    }

//
//    @Pointcut("execution(*  *..*.*.controller..*.*(..))")
//    public void controller() {
//    }

//    @Around(value = "loggerAopAnnotation() || loggerAopWithin()")
//    public Object WrapperControllerOpLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        // 将切到的方法包装起来，通过proceed执行业务代码，可在执行前后添加切面编程代码
//        Object result =  proceedingJoinPoint.proceed();
//        System.out.println("Around");
//        return result;
//    }
}
