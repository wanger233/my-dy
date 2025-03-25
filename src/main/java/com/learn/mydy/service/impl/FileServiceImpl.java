package com.learn.mydy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.mydy.config.QiNiuConfig;
import com.learn.mydy.entity.File;
import com.learn.mydy.mapper.FileMapper;
import com.learn.mydy.service.FileService;

import javax.annotation.Resource;

public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Resource
    private QiNiuConfig qiNiuConfig;

    @Override
    public String getToken() {
        return qiNiuConfig.videoUploadToken();
    }
}
