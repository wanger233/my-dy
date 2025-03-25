package com.learn.mydy.entity.user;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.learn.mydy.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@TableName("permission")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Permission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long pId;

    private String path;

    private String href;

    private String icon;

    private String name;

    private Integer isMenu;

    private String target;

    private Integer sort;

    private Integer state;



}
