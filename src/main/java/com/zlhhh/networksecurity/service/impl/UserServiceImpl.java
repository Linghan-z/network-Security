package com.zlhhh.networksecurity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zlhhh.networksecurity.common.Constants;
import com.zlhhh.networksecurity.entity.User;
import com.zlhhh.networksecurity.entity.dto.UserDTO;
import com.zlhhh.networksecurity.exception.ServiceException;
import com.zlhhh.networksecurity.mapper.UserMapper;
import com.zlhhh.networksecurity.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlhhh.networksecurity.utils.TokenUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlhhh
 * @since 2023-05-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if (one != null) {  // 业务异常
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword().toString());
            userDTO.setToken(token);

            return userDTO;
        } else {
            // 失败会抛出异常，抛出异常之后会被全局异常捕获器GlobalExceptionHandler捕获到，捕获完之后返回一个error
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {

        User one = getUserInfo(userDTO);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDTO, one, true);
            save(one);  // 把 copy 之后的用户对象存入数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    private  User getUserInfo(UserDTO userDTO) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, userDTO.getUsername());
        lambdaQueryWrapper.eq(User::getPassword, userDTO.getPassword());
        User one;
        try {
            one = getOne(lambdaQueryWrapper);  // 从数据库查询用户信息

        } catch (Exception e) {
            e.printStackTrace();  //  打印异常的堆栈
            LOG.error(e);
            // sql查询发生的错误（sql系统），返回错误信息
            throw  new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

}
