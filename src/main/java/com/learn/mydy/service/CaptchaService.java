package com.learn.mydy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.mydy.entity.Captcha;

import java.awt.image.BufferedImage;

public interface CaptchaService extends IService<Captcha> {

    BufferedImage getCaptcha(String uuId);

    boolean validate(Captcha captcha);
}