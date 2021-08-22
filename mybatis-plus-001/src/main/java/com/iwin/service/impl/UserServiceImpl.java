package com.iwin.service.impl;

import com.iwin.entity.User;
import com.iwin.mapper.UserMapper;
import com.iwin.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author iwin
 * @since 2021-08-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
