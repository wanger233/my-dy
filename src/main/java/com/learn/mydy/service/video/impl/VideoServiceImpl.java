package com.learn.mydy.service.video.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.learn.mydy.config.QiNiuConfig;
import com.learn.mydy.constant.AuditStatus;
import com.learn.mydy.entity.task.VideoTask;
import com.learn.mydy.entity.video.Type;
import com.learn.mydy.entity.video.Video;
import com.learn.mydy.exception.BaseException;
import com.learn.mydy.mapper.video.VideoMapper;
import com.learn.mydy.service.video.VideoService;
import com.learn.mydy.utils.UserHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.UUID;

public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Resource
    private LoadingCache<String,String> localCache;

    @Override
    public void publishVideo(Video video) {
        //获取信息，权限校验
        Long userId = UserHolder.get();
        Long videoId = video.getId();
        Video oldVideo = null;
        //对于更新操作，视频的修改权限校验，不能改视频源URL和封面，只能改视频标题和描述
        if (videoId!=null){
            oldVideo = getOne(new LambdaQueryWrapper<Video>()
                    .eq(Video::getId, videoId)
                    .eq(Video::getUserId, userId));
            if (oldVideo!=null){
                if (!(video.buildVideoUrl().equals(oldVideo.buildVideoUrl()))
                || !(video.buildCoverUrl().equals(oldVideo.buildCoverUrl()))){
                    throw new BaseException("视频源URL不能修改");
                }
            }
        }
        //业务数据校验，分类是否存在，标签数量是否超过上限
        Long typeId = video.getTypeId();
        Type type = typeService.getById(typeId);
        if (type==null){
            throw new BaseException("分类不存在");
        }
        if (video.buildLabel().size() > 5) {
            throw new BaseException("标签最多只能选择5个");
        }
        //状态设置  是待审核还是通过等等
        video.setAuditStatus(AuditStatus.PROCESS);
        video.setUserId(userId);
        //如果是更新，
        boolean isAdd = videoId == null ? true : false;
        if (!isAdd){
            video.setYv(null);
            video.setVideoType(null);
            video.setLabelNames(null);
            video.setUrl(null);
            video.setCover(null);
        }else {
        //如果是新增，
            // 如果没设置封面,我们帮他设置一个封面
            if (ObjectUtils.isEmpty(video.getCover())) {
                video.setCover(fileService.generatePhoto(video.getUrl(), userId));
            }
            video.setYv("YV" + UUID.randomUUID().toString().replace("-", "").substring(8));
        }
        //视频时长处理 填充视频时长 (若上次发布视频不存在Duration则会尝试获取)
        if (isAdd || !StringUtils.hasLength(oldVideo.getDuration())) {
            final String uuid = UUID.randomUUID().toString();
            localCache.put(uuid, String.valueOf(true));
            try {
                Long url = video.getUrl();
                if (url == null || url == 0) url = oldVideo.getUrl();
                final String fileKey = fileService.getById(url).getFileKey();
                final String duration = FileUtil.getVideoDuration(QiNiuConfig.CNAME + "/" + fileKey + "?uuid=" + uuid);
                video.setDuration(duration);
            } finally {
                localCache.invalidate(uuid);
            }
        }
        //持久化，调用审核服务
        this.saveOrUpdate(video);

        final VideoTask videoTask = new VideoTask();
        videoTask.setOldVideo(video);
        videoTask.setVideo(video);
        videoTask.setIsAdd(isAdd);
        videoTask.setOldState(isAdd ? true : video.getOpen());
        videoTask.setNewState(true);
        videoPublishAuditService.audit(videoTask, false);
    }
}
