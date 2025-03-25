package com.learn.mydy.limit;

import com.learn.mydy.exception.LimiterException;
import com.learn.mydy.utils.UserHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;

import static com.learn.mydy.constant.RedisConstant.VIDEO_LIMIT;

@Aspect
public class LimiterAop {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 拦截
     * @param joinPoint
     * @param limiter
     * @return
     * @throws Throwable
     */
    @Before("@annotation(limiter)")
    public Object restriction(ProceedingJoinPoint joinPoint, Limit limiter) throws Throwable {
        final Long userId = UserHolder.get();
        final int limitCount = limiter.limit();
        final String msg = limiter.msg();
        final long time = limiter.time();
        // 缓存是否存在
        String key = VIDEO_LIMIT + userId;
        final Object o1 = stringRedisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(o1)){
            stringRedisTemplate.opsForValue().set(key, String.valueOf(1),time);
        }else {
            if (Integer.parseInt(o1.toString()) > limitCount){
                throw new LimiterException(msg);
            }
            stringRedisTemplate.opsForValue().increment(key,1);
        }
        Object o = joinPoint.proceed();
        return o;
    }


}