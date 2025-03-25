package com.learn.mydy.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Captcha implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @NotBlank(message = "uuid为空")
    private String uuid;

    /**
     * 验证码
     */
    @NotBlank(message = "code为空")
    private String code;

    /**
     * 过期时间
     */
    private Date expireTime;

    @TableField(exist = false)
    @Email
    private String email;

}
