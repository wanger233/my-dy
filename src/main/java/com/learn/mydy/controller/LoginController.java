package com.learn.mydy.controller;

import com.learn.mydy.dto.login.CheckDTO;
import com.learn.mydy.dto.login.FindPWDTO;
import com.learn.mydy.dto.R;
import com.learn.mydy.dto.login.RegisterDTO;
import com.learn.mydy.entity.Captcha;
import com.learn.mydy.entity.user.User;
import com.learn.mydy.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/luckyjourney/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/captcha.jpg/{uuId}")
    public R captcha(HttpServletResponse response, @PathVariable String uuId) throws IOException {
        //获取图形验证码
        return loginService.captcha(uuId, response);
    }

    @PostMapping("/getCode")
    public R getCode(@RequestBody @Validated Captcha captcha) {
        //校验图片验证码 并且 获取邮箱验证码
        return loginService.getCode(captcha);
    }

    @PostMapping("/register")
    public R register(@RequestBody @Validated RegisterDTO registerDTO) {
        //注册
        return loginService.register(registerDTO);
    }
    @PostMapping("/login")
    public R login(@RequestBody @Validated User user) {
        //登录
        return loginService.login(user);
    }

    @PostMapping("/check")
    public R check(@RequestBody @Validated CheckDTO checkDTO) {
        //验证邮箱验证码
        return loginService.check(checkDTO);
    }
    @PostMapping("/findPassword")
    public R findPassword(@RequestBody @Validated FindPWDTO findPWDTO) {
        //找回密码
        return loginService.findPassword(findPWDTO);
    }
}
