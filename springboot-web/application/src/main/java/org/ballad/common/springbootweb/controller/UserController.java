package org.ballad.common.springbootweb.controller;

import org.ballad.common.springbootweb.aop.AuditLog;
import org.ballad.common.springbootweb.mybatisplus.bean.User;
import org.ballad.common.springbootweb.mybatisplus.mapper.UserMapper;
import org.ballad.common.springbootweb.mybatisplus.querywrapper.UserQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息操作接口
 *
 * @author bldbld
 */
@RestController
@RequestMapping("/sample")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserQueryWrapper userQueryWrapper;


    @AuditLog(logInfo = "'新增用户:'+ #user.name")
    @PostMapping("useraccess")
    public @ResponseBody String useraccess(@RequestBody User user){
        return "";
    }

    @AuditLog(logInfo = "返回用户:")
    @PostMapping("getuser")
    public String getuser(){
        return "User:AAA";
    }

    @AuditLog(logInfo = "查询总数")
    @PostMapping("getcount")
    public String getCount(){
        return String.valueOf(userMapper.getCount());
    }

    @AuditLog(logInfo = "'查询用户:'+ #user.name")
    @PostMapping("getuserbynamelike")
    public String getUserListByNameLike(@RequestBody String name){
        List<User> list = userQueryWrapper.getList(userMapper,name);
        return String.valueOf(list.size());
    }
}
