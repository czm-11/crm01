package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.mapper.UserRoleMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.pojo.User;
import com.yjxxt.pojo.UserRole;
import com.yjxxt.query.UserQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.PhoneUtil;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    public UserModel userLogin(String userName,String userPwd){
        checkUserLoginParam(userName,userPwd);
        //用户是否存在
        User temp=userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp==null,"用户不存在");
        //用户密码是否正确
        checkUserPwd(userPwd,temp.getUserPwd());
        //构建返回对象
        return BuilderUserInfo(temp);
    }

    private UserModel BuilderUserInfo(User user) {
        UserModel userModel=new UserModel();
        //id加密
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void checkUserLoginParam(String userName, String userPwd) {
        //用户名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        //密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }

    private void checkUserPwd(String userPwd, String userPwd1) {
        userPwd= Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"用户密码错误");
    }


    public void changeUserPwd(Integer userId,String oldPassword,String newPassword,String confirmPwd){
        //用户登录了才能修改  userId
        User user = userMapper.selectByPrimaryKey(userId);
        //密码验证
        checkPasswordParams(user,oldPassword,newPassword,confirmPwd);
        //修改密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //是否修改成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败了");

    }

    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPwd) {
        AssertUtil.isTrue(user==null,"用户未登录或者不存在");
        //原始密码非空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        //原始密码是否正确
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确");
        //新密码非空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        //新密码与原密码不能相同
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码和原密码不能相同");
        //确认密码非空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空");
        //确认密码与新密码一致
        AssertUtil.isTrue(!newPassword.equals(confirmPwd),"确认密码和新密码要一致");
    }

    public List<Map<String,Object>> querySales(){
        return userMapper.selectSales();
    }

    public Map<String,Object> findUserByParams(UserQuery userQuery){
        //实例化map
        Map<String,Object> map=new HashMap<>();
        //初始化分页单位
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        //开始分页
        PageInfo<User> plist = new PageInfo<>(selectByParams(userQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //验证
        checkUser(user);
        //设置默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //密码加密
        user.setUserPwd(Md5Util.encode("123456"));
        //验证是否添加成功
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加失败了");
        relaionUserRole(user.getId(),user.getRoleIds());
        System.out.println(user.getId()+"---"+user.getRoleIds());
    }

    private void relaionUserRole(Integer userId, String roleIds) {
        //准备集合存储对象
        List<UserRole> urlist=new ArrayList<>();
        //userId,roleId
        AssertUtil.isTrue(StringUtils.isBlank(roleIds),"请选择角色信息");
        //统计用户当前角色
        int count= userRoleMapper.countUserRoleNum(userId);
        //删除用户角色
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色删除失败");
        }
        String[] roleStrId=roleIds.split(",");
        //循环遍历
        for(String rid:roleStrId){
            //准备对象
            UserRole userRole=new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.parseInt(rid));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            //存放到集合
            urlist.add(userRole);
        }
        //批量添加
        AssertUtil.isTrue(userRoleMapper.insertBatch(urlist)!=urlist.size(),"用户角色分配失败");

    }

    private void checkUser(User user) {
        //用户名非空
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()),"用户名不能为空");
        //用户名唯一
        User temp = userMapper.selectUserByName(user.getUserName());

        if(user.getId()==null){//添加操作
            AssertUtil.isTrue(temp!=null,"用户名已存在");
        }else {//修改操作
            AssertUtil.isTrue(temp!=null&&!temp.getId().equals(user.getId()),"用户名已被使用");
        }
        //邮箱非空
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()),"邮箱不能为空");
        //手机号非空
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()),"手机号不能为空");
        //手机号合法
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()),"请输入合法手机号");
    }

    public void changeUser(User user){
        //更具id获取用户信息
        User temp = userMapper.selectByPrimaryKey(user.getId());
        //判断
        AssertUtil.isTrue(temp==null,"待修改的记录不存在");
        //验证参数
        checkUser(user);
        //设定默认值
        user.setUpdateDate(new Date());
        //判断修改是否成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败了");
        relaionUserRole(user.getId(),user.getRoleIds());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeUserIds(Integer[] ids){
        AssertUtil.isTrue(ids==null||ids.length==0,"请选择要删除的数据");

        for(Integer userId:ids){
            //统计用户当前角色
            int count= userRoleMapper.countUserRoleNum(userId);
            //删除用户角色
            if(count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色删除失败");
            }
        }

        AssertUtil.isTrue(userMapper.deleteBatch(ids)<1,"删除失败了");
    }

}
