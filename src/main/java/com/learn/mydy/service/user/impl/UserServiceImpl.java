package com.learn.mydy.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.mydy.constant.RedisConstant;
import com.learn.mydy.dto.login.FindPWDTO;
import com.learn.mydy.dto.login.RegisterDTO;
import com.learn.mydy.entity.user.Favorites;
import com.learn.mydy.entity.user.User;
import com.learn.mydy.mapper.user.UserMapper;
import com.learn.mydy.service.user.FavoritesService;
import com.learn.mydy.service.user.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

import static com.learn.mydy.constant.SysConstant.INIT_DESCRIBE;
import static com.learn.mydy.constant.SysConstant.INIT_FAVORITES_NAME;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Resource
    private FavoritesService favoritesService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Boolean register(RegisterDTO registerDTO) {

        //注册
        //1.判断邮箱是否已经注册
        String email = registerDTO.getEmail();
        User one = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));
        if (one != null) {
            //邮箱已注册
            return false;
        }
        //2.未注册，封装User对象，保存
        User user = new User()
                .setEmail(email)
                .setPassword(registerDTO.getPassword())
                .setNickName(registerDTO.getNickName())
                .setDescription(INIT_DESCRIBE);
        save(user);
        //3.创建默认收藏夹，保存
        Favorites favorites = new Favorites()
                .setName(INIT_FAVORITES_NAME)
                .setUserId(user.getId());
        favoritesService.save(favorites);
        //4.更新user信息
        user.setDefaultFavoritesId(favorites.getId());
        updateById(user);
        //5.返回结果
        return true;
    }

    @Override
    public Boolean findPassword(FindPWDTO findPWDTO) {

        // 从redis中取出
        final Object o = stringRedisTemplate.opsForValue().get(RedisConstant.EMAIL_CODE + findPWDTO.getEmail());
        if (o==null){
            return false;
        }
        // 校验
        if (Integer.parseInt(o.toString()) != findPWDTO.getCode()){
            return false;
        }
        // 修改
        final User user = new User();
        user.setEmail(findPWDTO.getEmail());
        user.setPassword(findPWDTO.getNewPassword());
        update(user,new UpdateWrapper<User>().lambda().set(User::getPassword,findPWDTO.getNewPassword()).eq(User::getEmail,findPWDTO.getEmail()));
        return true;
    }
}
