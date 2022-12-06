package org.ballad.common.springbootweb.mybatisplus.querywrapper;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ballad.common.springbootweb.mybatisplus.bean.User;
import org.ballad.common.springbootweb.mybatisplus.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户信息查询封装类
 */
@Component
public class UserQueryWrapper {

    /**
     * Wrapper 使用示例
     *
     * @param userMapper
     * @param name
     * @return
     */
    public List<User> getList(UserMapper userMapper, String name){
        //构建一个查询的wrapper
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.like(StringUtils.isNotBlank(name),"name",name);
        wrapper.orderByDesc("id");
        List<User> list = userMapper.selectList(wrapper);
        return list;
    }




}
