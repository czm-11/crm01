package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.pojo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    int countUserRoleNum(int userId);

    int deleteUserRoleByUserId(int userId);
}