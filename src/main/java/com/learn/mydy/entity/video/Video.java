package com.learn.mydy.entity.video;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.learn.mydy.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Data
@TableName("video")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Video extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // YV ID 以YV+UUID
    private String yv;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    private Long url;

    private Long userId;

    private Long typeId;

    private Long cover;
    /**
     * 公开/私密，0：公开，1：私密，默认为0
     */
    private Boolean open;

    // 审核状态:通过,未通过,审核中
    private Integer auditStatus;

    // 审核状态的消息，当前嵌套在这里，应该有一个审核表?
    private String msg;

    // 审核快慢状态 0 慢速  1快速
    private Boolean auditQueueStatus;

    // 点赞数
    private Long startCount;

    // 分享数
    private Long shareCount;

    // 浏览次数
    private Long historyCount;

    // 收藏次数
    private Long favoritesCount;

    // 视频时长
    private String duration;


    private String labelNames;


}

