package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.mapper.SaleChanceMapper;
import com.yjxxt.pojo.SaleChance;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Autowired(required = false)
    private SaleChanceMapper saleChanceMapper;

    /**
     * 条件查询列表
     * code
     * msg
     * count
     * data
     */
    public Map<String,Object> sayList(SaleChanceQuery saleChanceQuery){
        //实例化分页单位
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //开始分页
        PageInfo<SaleChance> pageInfo = new PageInfo<>(selectByParams(saleChanceQuery));
        //构建map
        Map<String,Object> map=new HashMap<>();
        map.put("code",200);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //验证
        checkSaleChanceParm(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //state 0,1(0---未分配，1---已分配)
        //未分配
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
        }
        //已经分配
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //设置默认值 state,devResult(0--未开发，1--开发中，2--开发成功了，3--开发失败了)
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setIsValid(1);
        //createDate updateDate,分配时间
        //添加是否成功
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败了");
    }

    private void checkSaleChanceParm(String customerName, String linkMan, String linkPhone) {
        //客户名非空
        //联系人非空
        //联系电话非空，11为合法手机号
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名称");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入联系人电话");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"请输入合法的手机号");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void changeSaleChance(SaleChance saleChance){
        //验证
        SaleChance temp = selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null,"待修改的记录不存在");
        checkSaleChanceParm (saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //未分配
        if(StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //已分配
        if(StringUtils.isNotBlank(temp.getAssignMan())&& StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
        }
        //设置默认值
        saleChance.setUpdateDate(new Date());
        //修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"修改失败了");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeSaleChanceIds(Integer[] ids){
        AssertUtil.isTrue(ids==null || ids.length==0,"请选择要删除的数据");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)!=ids.length,"批量删除失败了");
    }
}
