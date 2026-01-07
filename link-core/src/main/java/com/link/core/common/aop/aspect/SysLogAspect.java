package com.link.core.common.aop.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.link.core.common.aop.annotation.LogAnnotation;
import com.link.core.common.satoken.SaTokenUtil;
import com.link.core.common.satoken.constant.SaTokenConstant;
import com.link.core.entity.SysLog;
import com.link.core.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 日志切面
 *
 * @author wenbin
 * @version V1.0
 * @date 2020年3月18日
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {
    @Lazy
    @Resource
    private LogService logService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 此处的切点是注解的方式
     * 只要出现 @LogAnnotation注解都会进入
     */
    @Pointcut("@annotation(com.link.core.common.aop.annotation.LogAnnotation)")
    public void logPointCut() {

    }

    /**
     * 环绕增强,相当于MethodInterceptor
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        try {
            saveSysLog(point, time);
        } catch (Exception e) {
            log.error("sysLog,exception:{}", e, e);
        }

        return result;
    }

    /**
     * 把日志保存
     */
    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog sysLog = new SysLog();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        if (logAnnotation != null) {
            //注解上的描述
            sysLog.setOperation(logAnnotation.title() + "-" + logAnnotation.action());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        log.info("请求{}.{}耗时{}毫秒", className, methodName, time);
        try {
            //请求的参数
            Object[] args = joinPoint.getArgs();
            String params = null;
            if (args.length != 0) {
                // 是否包含MultipartFile 参数
                MultipartFile multipartFile = (MultipartFile) Arrays.stream(args).filter(arg -> arg instanceof MultipartFile).findFirst().orElse(null);
                // 去掉 HttpServletResponse和 MultipartFile 参数
                params = JSON.toJSONString(Arrays.stream(args).filter(arg -> !(arg instanceof HttpServletResponse) && !(arg instanceof MultipartFile)).toArray());
                // 如果包含MultipartFile 参数，只保存文件名
                if (multipartFile != null) {
                    JSONArray jsonArray = JSON.parseArray(params);
                    if (jsonArray.size() > 0) {
                        jsonArray.getJSONObject(0).put("file", multipartFile.getOriginalFilename());
                        params = JSON.toJSONString(jsonArray);
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("file", multipartFile.getOriginalFilename());
                        params = JSON.toJSONString(jsonObject);
                    }
                }
            }

            sysLog.setParams(params);
            //设置IP地址
            sysLog.setIp(ServletUtil.getClientIP(httpServletRequest));
            log.info("Ip{}，接口地址{}，请求方式{}，入参：{}", sysLog.getIp(), httpServletRequest.getRequestURL(), httpServletRequest.getMethod(), sysLog.getParams());

            //用户名
            String userId = StpUtil.getLoginIdAsString();
            JSONObject userInfo = new JSONObject(StpUtil.getSession().get(SaTokenConstant.SESSION_KEY_USER));
            sysLog.setUserId(userId);
            sysLog.setUsername(userInfo.getStr("username"));
            sysLog.setTime((int) time);

            String organRelation = SaTokenUtil.getSession(SaTokenConstant.SESSION_KEY_ORGAN_RELATION).toString();
            sysLog.setOrganRelation(organRelation);
            log.info(sysLog.toString());
            logService.saveLog(sysLog);
        } catch (Exception e) {
            log.error("sysLog,exception:{}", e, e);
        }
    }
}
