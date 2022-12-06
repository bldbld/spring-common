package org.ballad.common.springbootweb.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ballad.common.springbootweb.mybatisplus.bean.User;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 使用自定义SQL进行查询
     *
     * @return
     */
    @Select("select count(*) from user;")
    Integer getCount();
}
