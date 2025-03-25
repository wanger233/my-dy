package com.learn.mydy.entity.video;

import com.baomidou.mybatisplus.annotation.TableName;
import com.learn.mydy.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("video_star")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class VideoStar extends BaseEntity {

    private static final long serialVersionUID = 1L;


    private Long videoId;

    private Long userId;


}
