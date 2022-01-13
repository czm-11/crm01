package com.yjxxt.service;

import com.yjxxt.base.BaseService;
import com.yjxxt.mapper.PermissionMapper;
import com.yjxxt.pojo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;

    public List<String> queryUserHasRolesHasPermissions(Integer userId){
        return permissionMapper.selectUserHasRolesHasPermissions(userId);
    }

}
