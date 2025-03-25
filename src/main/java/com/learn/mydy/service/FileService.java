package com.learn.mydy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.mydy.entity.File;

public interface FileService extends IService<File> {


    String getToken();
}
