package com.learn.mydy.dto.login;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CheckDTO {

    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "验证码不可为空")
    private Integer code;
}
