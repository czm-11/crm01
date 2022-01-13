package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.pojo.SaleChance;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.service.SaleChanceService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private UserService userService;

    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> sayList(SaleChanceQuery saleChanceQuery){
        Map<String, Object> map = saleChanceService.queryByParamsForTable(saleChanceQuery);
        return map;

    }

    @RequestMapping("addUpdateDialog")
    public String addOrUpdate(Integer id, Model model){
        //判断
        if(id!=null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(SaleChance saleChance, HttpServletRequest req){
        //获取登录用户的id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        String trueName= userService.selectByPrimaryKey(userId).getTrueName();
        //创建人
        saleChance.setCreateMan(trueName);
        //添加操作
        saleChanceService.addSaleChance(saleChance);
        //返回目标对象
        return success("创建成功！！！");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(SaleChance saleChance){
        saleChanceService.updateByPrimaryKeySelective(saleChance);
        return success("修改成功了");
    }

    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo deletes(Integer[] ids){
        saleChanceService.removeSaleChanceIds(ids);
        return success("批量删除成功了");
    }


}
