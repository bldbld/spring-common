package org.ballad.common.springbootweb.controller;

import org.ballad.common.springbootweb.aop.AuditLog;
import org.ballad.common.springbootweb.mybatisplus.bean.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sample")
public class SampleAccessController {

    @AuditLog(logInfo = "'判断用户权限:'+ #user.name")
    @PostMapping("useraccess")
    public @ResponseBody String useraccess(@RequestBody User user){
        return "";
    }

    @AuditLog(logInfo = "'新增管理员:'+ #user.username")
    @PostMapping("getuser")
    public String getuser(){
        return "User:AAA";
    }
}
