package com.yjxxt.controller;

import com.yjxxt.annotation.RequiredPermission;
import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.pojo.Role;
import com.yjxxt.query.RoleQuery;
import com.yjxxt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("findRoles")
    @ResponseBody
    public List<Map<String,Object>> selectRoles(int userId){
        return roleService.findRoles(userId);
    }

    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @RequestMapping("toRoleGrantPage")
    public String toRoleGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code = "60")
    public Map<String,Object> list(RoleQuery roleQuery){
        return roleService.findRoleByParam(roleQuery);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(Role role){
        roleService.addRole(role);
        return success("角色添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(Role role){
        roleService.changeRole(role);
        return success("角色修改成功");
    }

    @RequestMapping("toAddOrUpdate")
    public String addOrUpdate(Integer roleId, Model model){
        if(roleId!=null){
            Role role = roleService.selectByPrimaryKey(roleId);
            model.addAttribute("role",role);
        }
        return "role/addUpdate";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Role role){
        roleService.removeRoleById(role);
        return success("角色删除成功");
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mids ){
        roleService.addGrant(roleId,mids);
        return success("授权成功了");
    }
}
