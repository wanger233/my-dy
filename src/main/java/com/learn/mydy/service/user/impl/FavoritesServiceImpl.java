package com.learn.mydy.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.mydy.entity.user.Favorites;
import com.learn.mydy.mapper.user.FavoritesMapper;
import com.learn.mydy.service.user.FavoritesService;
import org.springframework.stereotype.Service;

@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, Favorites> implements FavoritesService {
}
