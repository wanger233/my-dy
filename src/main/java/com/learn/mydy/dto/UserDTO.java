package com.learn.mydy.dto;

import lombok.Data;

/**
 * @description:
 * @Author: Xhy
 * @CreateTime: 2023-10-25 15:40
 */
@Data
public class UserDTO {

    private Long id;

    private String nickName;

    private Long avatar;

    private Boolean sex;

    private String description;

    private Long follow;

    private Long fans;
}
