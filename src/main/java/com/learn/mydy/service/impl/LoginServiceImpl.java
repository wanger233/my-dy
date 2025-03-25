package com.learn.mydy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.learn.mydy.dto.R;
import com.learn.mydy.dto.login.CheckDTO;
import com.learn.mydy.dto.login.FindPWDTO;
import com.learn.mydy.dto.login.RegisterDTO;
import com.learn.mydy.entity.Captcha;
import com.learn.mydy.entity.user.User;
import com.learn.mydy.service.CaptchaService;
import com.learn.mydy.service.LoginService;
import com.learn.mydy.service.user.UserService;
import com.learn.mydy.service.user.impl.UserServiceImpl;
import com.learn.mydy.utils.JwtUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import static com.learn.mydy.constant.RedisConstant.EMAIL_CODE;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private CaptchaService captchaService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;


    @Override
    public R captcha(String uuId, HttpServletResponse response) {
        if (uuId == null || uuId.isEmpty()) {
            return R.error().message("验证码ID不能为空");
        }
        //获取图形验证码
        BufferedImage captcha = captchaService.getCaptcha(uuId);
        try {
            //设置响应头，告诉浏览器返回的是图片
            response.setContentType("image/jpeg");
            //禁止浏览器缓存
            response.setHeader("Cache-Control", "no-store, no-cache");
            ServletOutputStream outputStream = response.getOutputStream();
            //将验证码图片写入响应流
            ImageIO.write(captcha, "jpg", outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }

    @Override
    public R getCode(Captcha captcha) {

        //先校验图片验证码是否正确
        boolean isSuccess = captchaService.validate(captcha);
        //不正确，直接返回
        if (!isSuccess) {
            return R.error().message("验证码错误");
        }
        //正确，删除图形验证码
        captchaService.removeById(captcha.getUuid());
        //正确，生成邮箱验证码（已经在validate方法中生成并发送了）
        return R.ok().message("发送成功,请耐心等待");
    }

    @Override
    public R register(RegisterDTO registerDTO) {
        //注册
        //解析数据
        if (registerDTO == null || registerDTO.getEmail() == null || registerDTO.getPassword() == null) {
            return R.error().message("参数不能为空");
        }
        //保存到数据库 User，UserSub，favorites表要更新
        //调用它们的service接口来保存
        Boolean isSuccess = userService.register(registerDTO);
        //保存成功，返回成功
        if (!isSuccess) {
            return R.error().message("注册失败");
        }
        //保存成功，返回成功
        return R.ok().message("注册成功");
    }

    @Override
    public R login(User user) {
        //登录
        //查找user是否存在
        User user1 = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, user.getEmail())
                .eq(User::getPassword, user.getPassword())
        );
        //不存在，返回错误
        if (user1 == null) {
            return R.error().message("登录失败,用户名或密码错误");
        }
        //存在，生成token
        String token = JwtUtils.getJwtToken(user.getId(), user.getNickName());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("name", user.getNickName());
        map.put("token", token);
        //返回token
        return R.ok().data(map);
    }

    @Override
    public R check(CheckDTO checkDTO) {
        //验证邮箱验证码
        //1.获取邮箱验证码
        String code = checkDTO.getCode().toString();
        //2.查看缓存的验证码是否存在
        String email = checkDTO.getEmail();
        if (code == null || code.isEmpty()) {
            return R.error().message("验证码不能为空");
        }
        if (email == null || email.isEmpty()) {
            return R.error().message("邮箱不能为空");
        }
        String str = stringRedisTemplate.opsForValue().get(EMAIL_CODE + email);
        //不存在，直接返回
        if (str == null || str.isEmpty()) {
            return R.error().message("验证码已失效");
        }
        //存在，校验
        if (!code.equals(str)){
            return R.error().message("验证码错误");
        }
        //3.校验成功，删除缓存的验证码
        stringRedisTemplate.delete(EMAIL_CODE + email);
        return R.ok().message("验证成功");
    }

    @Override
    public R findPassword(FindPWDTO findPWDTO) {
        Boolean b = userService.findPassword(findPWDTO);
        return R.ok().message(b ? "修改成功" : "修改失败,验证码不正确");
    }
}
