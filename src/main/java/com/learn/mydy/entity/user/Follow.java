package com.learn.mydy.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("`follow`")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 关注id
    private Long followId;

    // 粉丝id
    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreated;


}
