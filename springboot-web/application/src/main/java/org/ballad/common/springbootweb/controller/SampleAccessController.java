package org.ballad.common.springbootweb.controller;

import org.ballad.common.springbootweb.aop.AuditLog;
import org.ballad.common.springbootweb.mybatisplus.bean.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sample")
public class SampleAccessController {

    @AuditLog(logInfo = "'新增用户:'+ #user.name")
    @PostMapping("useraccess")
    public @ResponseBody String useraccess(@RequestBody User user){
        return "";
    }

    @AuditLog(logInfo = "'返回用户:'+ #user.name")
    @PostMapping("getuser")
    public String getuser(){
        return "User:AAA";
    }
}
