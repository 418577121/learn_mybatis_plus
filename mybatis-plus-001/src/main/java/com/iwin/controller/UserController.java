package com.iwin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iwin.entity.User;
import com.iwin.mapper.UserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author iwin
 * @since 2021-08-22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/getAll")
    public List<User> getAll() {
        //构造条件
        QueryWrapper<User> query = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "iwin");
        params.put("age", 18);
        params.put("email", null);

        //query.allEq(params,false);
        query.allEq((k, v) -> !k.equals("name"), params, false);
        List<User> list = userMapper.selectList(query);
        System.out.println(list);
        return list;
    }

}

