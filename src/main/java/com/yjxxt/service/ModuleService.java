package com.yjxxt.service;

import com.yjxxt.base.BaseService;
import com.yjxxt.dto.TreeDto;
import com.yjxxt.mapper.ModuleMapper;
import com.yjxxt.mapper.PermissionMapper;
import com.yjxxt.pojo.Module;
import com.yjxxt.pojo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeDto> findModules(){
        return moduleMapper.selectModules();
    }

    public List<TreeDto> findModulesByRoleId(Integer roleId){
        //获取所有资源信息
        List<TreeDto> tList=moduleMapper.selectModules();
        //获取房钱角色拥有的资源信息
        List<Integer> roleHasModule=permissionMapper.selectModuleByRoleId(roleId);
        //遍历
        for(TreeDto treeDto:tList){
            if(roleHasModule.contains(treeDto.getId())){
                treeDto.setChecked(true);
            }
        }
        return tList;
    }
}
