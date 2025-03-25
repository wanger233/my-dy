package com.learn.mydy.service.video;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.mydy.entity.video.Video;

public interface VideoService extends IService<Video> {
    void publishVideo(Video video);
}
