package com.learn.mydy.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("favorites_video")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FavoritesVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long favoritesId;

    //TODO 这里只有一个视频id吗？
    private Long videoId;

    // 冗余字段
    private Long userId;

}
