package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.pojo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    int countRoleModulesByRoleId(int roleId);

    int deleteRoleModulesByRoleId(int roleId);

    List<Integer> selectModuleByRoleId(Integer roleId);

    List<String> selectUserHasRolesHasPermissions(int userId);
}