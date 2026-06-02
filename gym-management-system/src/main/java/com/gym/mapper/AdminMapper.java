package com.gym.mapper;

import com.gym.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {

    Admin selectByAccountAndPassword(Admin admin);

}
