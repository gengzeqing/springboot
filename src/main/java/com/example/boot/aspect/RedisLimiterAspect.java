package com.example.boot.aspect;

import com.alibaba.fastjson.JSON;
import com.example.boot.annotation.RedisLimiter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class RedisLimiterAspect {

    public static final Logger LOG = LoggerFactory.getLogger(RedisLimiterAspect.class);

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private DefaultRedisScript<List> redisScript;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<List>();
        redisScript.setResultType(List.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("limit.lua")));
    }

    @Pointcut("execution(public * com.example.boot.mvc.*.*(..))")
    public void pointcut() {}


    @Around("pointcut()")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        RedisLimiter redisLimiter = signature.getMethod().getDeclaredAnnotation(RedisLimiter.class);
        if (redisLimiter == null) {
            // 正常执行方法
            return proceedingJoinPoint.proceed();
        }
        // 获取配置的速率
        double value = redisLimiter.value();
        System.out.println("获取配置的速率 : " + value);

        // 设置lua的KEYS[1]
        // 获取当前时间戳的单位秒
        String key = "IP:" + System.currentTimeMillis() / 1000;
        LOG.info("------【请求时间:{}】",key);
        List<String> keyList = Lists.newArrayList(key);
        // 设置lua的ARGV[1]
        List<String> argvList = Lists.newArrayList(String.valueOf(value));
        List execute = redisTemplate.execute(redisScript, keyList, String.valueOf(value));
        if (StringUtils.equals(execute.get(0).toString(),"0")) {
            //fullback();
            //return null;
            throw new Exception("服务器挤爆了...稍后操作");
        }
        return proceedingJoinPoint.proceed();
    }

    private void fullback() {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0L);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = Thread.currentThread().getName();
        out.print(JSON.toJSONString(name + " ------ " + "服务器挤爆了...稍后操作。"));
        out.flush();
        out.close();
    }

}

























