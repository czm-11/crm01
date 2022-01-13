package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.dto.TreeDto;
import com.yjxxt.pojo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {


    public List<TreeDto> selectModules();
}