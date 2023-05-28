package com.zlhhh.networksecurity.service.impl;

import com.zlhhh.networksecurity.entity.User;
import com.zlhhh.networksecurity.mapper.UserMapper;
import com.zlhhh.networksecurity.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
