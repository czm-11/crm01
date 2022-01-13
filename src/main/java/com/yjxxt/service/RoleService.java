package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.jdi.event.StepEvent;
import com.yjxxt.base.BaseService;
import com.yjxxt.mapper.ModuleMapper;
import com.yjxxt.mapper.PermissionMapper;
import com.yjxxt.mapper.RoleMapper;
import com.yjxxt.pojo.Permission;
import com.yjxxt.pojo.Role;
import com.yjxxt.query.RoleQuery;
import com.yjxxt.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Autowired(required = false)
    private RoleMapper roleMapper;
    @Autowired(required = false)
    private PermissionMapper permissionMapper;
    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    public List<Map<String,Object>> findRoles(int userId){
        return roleMapper.selectRoles(userId);
    }

    public Map<String,Object> findRoleByParam(RoleQuery roleQuery){
        //实例化map
        Map<String,Object> map=new HashMap<>();
        //开启分页
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> rList=new PageInfo<>(selectByParams(roleQuery));
        //准备数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",rList.getTotal());
        map.put("data",rList.getList());
        return map;
    }

    public void addRole(Role role){
        //角色名非空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空");
        //角色名唯一
        Role temp=roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp!=null,"角色名已经存在");
        //默认参数
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        //添加成功与否
        AssertUtil.isTrue(insertHasKey(role)<1,"添加失败了");
    }

    public void changeRole(Role role){
        //验证当前对象是否存在
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp==null,"待修改记录不存在");
        //角色名唯一
        Role temp2=roleMapper.selectRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp2==null && !(temp2.getId().equals(role.getId())),"角色名已经存在");
        //默认参数
        role.setUpdateDate(new Date());
        //是否修改成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"修改失败了");
    }

    public void removeRoleById(Role role){
        //验证
        AssertUtil.isTrue(role.getId()==null||selectByPrimaryKey(role.getId())==null,"请选择删除的数据");
        //设定默认值
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        //判断是否成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除失败了");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId,Integer[] mids){
        AssertUtil.isTrue(roleId==null,"请选择角色");
        AssertUtil.isTrue(mids==null||mids.length==0,"最少选择一个资源");

        //统计当前角色的资源数量
        int count=permissionMapper.countRoleModulesByRoleId(roleId);
        //删除角色的资源信息
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deleteRoleModulesByRoleId(roleId)!=count,"角色资源分配失败");
        }
        List<Permission> pList=new ArrayList<>();
        //遍历mids
        for(Integer mid:mids){
            //实例化对象
            Permission permission=new Permission();
            permission.setRoleId(roleId);
            permission.setModuleId(mid);
            //权限码
            permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
            //默认属性
            permission.setCreateDate(new Date());
            permission.setUpdateDate(new Date());
            pList.add(permission);
        }
        //验证
        AssertUtil.isTrue(permissionMapper.insertBatch(pList)!=pList.size(),"授权失败");
    }

}
