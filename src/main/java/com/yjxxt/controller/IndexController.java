package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.mapper.PermissionMapper;
import com.yjxxt.pojo.Permission;
import com.yjxxt.pojo.User;
import com.yjxxt.service.PermissionService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class  IndexController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest req){
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //根据用户id查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        req.setAttribute("user",user);
        //将用户的权限存储到session
        List<String> permissions = permissionService.queryUserHasRolesHasPermissions(userId);
        System.out.println(permissions);
        req.getSession().setAttribute("permissions",permissions);
        return "main";
    }

    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
}
