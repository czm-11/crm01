package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    public List<Map<String,Object>> selectRoles(int userId);

    public Role selectRoleByName(String roleName);
}