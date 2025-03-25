package com.learn.mydy.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.Producer;
import com.learn.mydy.entity.Captcha;
import com.learn.mydy.exception.BaseException;
import com.learn.mydy.mapper.CaptchaMapper;
import com.learn.mydy.service.CaptchaService;
import com.learn.mydy.service.EmailService;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.learn.mydy.constant.RedisConstant.EMAIL_CODE;
import static com.learn.mydy.constant.RedisConstant.EMAIL_CODE_TIME;

@Service
public class CaptchaServiceImpl extends ServiceImpl<CaptchaMapper, Captcha> implements CaptchaService {

    @Resource
    private Producer producer;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private EmailService emailService;

    @Override
    public BufferedImage getCaptcha(String uuId) {
        // 生成验证码
        String text = producer.createText();
        //保存到数据库
        Captcha captcha = new Captcha().setCode(text)
                .setUuid(uuId)
                .setExpireTime(DateUtil.offsetMinute(new Date(), 5));//设置过期时间为5分钟
        //TODO 这里是不是可以改进？
        save(captcha);
        //返回验证码图片
        return producer.createImage(text);
    }

    @Override
    public boolean validate(Captcha captcha) {
        //先校验图片验证码是否正确
        String email = captcha.getEmail();
        final String code1 = captcha.getCode();
        captcha = this.getOne(new LambdaQueryWrapper<Captcha>().eq(Captcha::getUuid, captcha.getUuid()));
        if (captcha == null) throw new BaseException("uuId为空");

        this.remove(new LambdaQueryWrapper<Captcha>().eq(Captcha::getUuid, captcha.getUuid()));
        if(!captcha.getCode().equals(code1)){
            throw new BaseException("code错误");
        }
        if(captcha.getExpireTime().getTime()<=System.currentTimeMillis()){
            throw new BaseException("uuid过期");
        }
        if (!code1.equals(captcha.getCode())){
            return false;
        }
        //生成邮箱验证码
        String code2 = getSixCode();
        stringRedisTemplate.opsForValue().set(EMAIL_CODE+email,code2,EMAIL_CODE_TIME, TimeUnit.SECONDS);
        //发送验证码到邮箱
        emailService.send(email,"注册验证码:"+code2+",验证码5分钟之内有效");
        return true;
    }

    private String getSixCode() {
        //生成6位随机数
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int code = (int) (Math.random()*10);
            builder.append(code);
        }
        return builder.toString();
    }

}
