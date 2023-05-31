package com.zlhhh.networksecurity.service;

import com.zlhhh.networksecurity.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlhhh.networksecurity.entity.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlhhh
 * @since 2023-05-28
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);
    User register(UserDTO userDTO);
}
