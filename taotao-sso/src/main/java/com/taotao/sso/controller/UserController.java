package com.taotao.sso.controller;

import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Title:UserController
 * Description:
 * Copyright (c) white 2016/3/16
 * Version:1.0
 */
@RequestMapping("user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 检查数据是否可用
     * @param param
     * @param type
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ("check/{param}/{type}"),method = RequestMethod.GET)
    public ResponseEntity<Boolean> check(@PathVariable("param") String param, @PathVariable("type") Integer type) throws Exception {
        try {
            Boolean bool = this.userService.check(param, type);
            return  ResponseEntity.ok(bool);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
/**
 *  用户注册
 */
    @RequestMapping(value = ("register"), method = RequestMethod.POST)
    public ResponseEntity<Void> register(User user){
        this.userService.register(user);
        try {
            //201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            //500
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 用户登录
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public ResponseEntity<String > login(@RequestParam("u") String userName,@RequestParam("p") String password){
        try {
            String ticket=this.userService.login(userName,password);
            if (ticket == null) {
                return ResponseEntity.ok(null);
            }
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 通过ticket查询用户信息
     * @param ticket
     * @return
     */
    @RequestMapping(value = "{ticket}" ,method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByTicket(@PathVariable("ticket") String ticket){
        try {
            User user = this.userService.queryUserByTicket(ticket);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
