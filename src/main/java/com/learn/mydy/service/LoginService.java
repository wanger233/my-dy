package com.learn.mydy.service;

import com.learn.mydy.dto.R;
import com.learn.mydy.dto.login.CheckDTO;
import com.learn.mydy.dto.login.FindPWDTO;
import com.learn.mydy.dto.login.RegisterDTO;
import com.learn.mydy.entity.Captcha;
import com.learn.mydy.entity.user.User;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    R captcha(String uuId, HttpServletResponse response);

    R getCode(Captcha captcha);

    R register(RegisterDTO registerDTO);

    R login(User user);

    R check(CheckDTO checkDTO);

    R findPassword(FindPWDTO findPWDTO);
}
