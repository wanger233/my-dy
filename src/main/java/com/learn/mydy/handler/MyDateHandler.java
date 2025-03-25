package com.learn.mydy.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @description: 自动填充时间字段
 *  用于 @TableField
 */
@Configuration
public class MyDateHandler implements MetaObjectHandler {
    //数据库中对应的是datetime类型

    @Override
    public void insertFill(MetaObject metaObject) {
        // 设置创建时间和更新时间
        this.setFieldValByName("gmtCreated", new Date(), metaObject);
        this.setFieldValByName("gmtUpdated", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 设置更新时间
        this.setFieldValByName("gmtUpdated", new Date(), metaObject);    }
}
