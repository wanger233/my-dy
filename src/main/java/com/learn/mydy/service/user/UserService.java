package com.learn.mydy.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.mydy.dto.login.FindPWDTO;
import com.learn.mydy.dto.login.RegisterDTO;
import com.learn.mydy.entity.user.User;

public interface UserService extends IService<User> {
    Boolean register(RegisterDTO registerDTO);

    Boolean findPassword(FindPWDTO findPWDTO);
}
