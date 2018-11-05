package com.taotao.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Title:UserService
 * Description:
 * Copyright (c) white 2016/3/16
 * Version:1.0
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private  static final Map<Integer, Boolean> TYPES=new HashMap<Integer ,Boolean>();

    private static final String REDIS_TICKET="TT_TICKET";

    static{
        TYPES.put(1,true);
        TYPES.put(2,true);
        TYPES.put(3,true);
    }

    /**
     * 检查数据是否可用
     * @param param
     * @param type
     * @return
     * @throws Exception
     */

    public  Boolean check(String param,Integer type)throws  Exception{
        if (!TYPES.containsKey(type)){
            //请求参数不合法
            throw  new Exception("请参数不合法！type类型:1.2.3");
        }
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(param);
                break;
            case 2:
                user.setPhone(param);
                break;
            case 3:
                user.setEmail(param);
                break;
            default:
                break;
        }
        return  this.userMapper.select(user).isEmpty();
    }

    /**
     * 用户注册
     * @param user
     */
    public  void  register(User user){
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setId(null);
        this.userMapper.insert(user);
    }

    /**
     * 用户登录
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    public String login(String userName ,String password)throws  Exception{
        User user = new User();
        user.setUsername(userName);
        User queryUser= this.userMapper.selectOne(user);
        if (!StringUtils.equals(queryUser.getPassword(), DigestUtils.md5Hex(password))) {
            return  null;
        }
        String ticket = DigestUtils.md5Hex(System.currentTimeMillis() + userName);
        this.redisService.set(REDIS_TICKET + ticket, MAPPER.writeValueAsString(queryUser) ,3600);
        return ticket;
    }

    /**
     * 通过ticket查询用户信息
     * @param ticket
     * @return
     * @throws Exception
     */
    public User queryUserByTicket(String ticket)throws Exception{
        String userStr = this.redisService.get(REDIS_TICKET + ticket);
        if (userStr == null) {
            return null;
        }
        this.redisService.expire(REDIS_TICKET+ticket,3600);
        return MAPPER.readValue(userStr,User.class);
    }

}
