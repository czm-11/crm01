package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.exceptions.ParamsException;
import com.yjxxt.model.UserModel;
import com.yjxxt.pojo.User;
import com.yjxxt.query.UserQuery;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    public UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){
        ResultInfo info=new ResultInfo();
            UserModel userModel = userService.userLogin(userName, userPwd);
            info.setCode(200);
            info.setMsg("登录成功");
            info.setResult(userModel);
        return info;
    }


    @PostMapping("update")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req,String oldPassword,String newPassword,String confirmPwd){
        ResultInfo info=new ResultInfo();
        //获取cookie中的userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //修改密码操作
        userService.changeUserPwd(userId,oldPassword,newPassword,confirmPwd);
        return info;
    }

    @RequestMapping("toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }

    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    @RequestMapping("addOrUpdatePage")
    public String addOrUpdatePage(Integer id, Model model){

        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/addupdate";
    }

    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req){
        //获取用户id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法查询
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user",user);
        //跳转
        return "user/setting";
    }

    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user){
        ResultInfo info=new ResultInfo();
        userService.updateByPrimaryKeySelective(user);
        return info;
    }

    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String,Object>> findSales(){
        List<Map<String, Object>> list = userService.querySales();
        return list;
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(UserQuery userQuery){
        return userService.findUserByParams(userQuery);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(User user){
        userService.addUser(user);
        return success("用户添加成功");
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public ResultInfo update(User user){
        userService.changeUser(user);
        return success("用户修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] ids){
        userService.removeUserIds(ids);
        return success("批量删除用户OK");
    }
}
