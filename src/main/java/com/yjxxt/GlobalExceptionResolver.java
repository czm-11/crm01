package com.yjxxt;

import com.alibaba.fastjson.JSON;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) {

        //未登录异常
        if(e instanceof NoLoginException){
            ModelAndView mav=new ModelAndView("redirect:/index");
            return mav;
        }

        //实例化对象
        ModelAndView mav=new ModelAndView("error");
        //存储数据
        mav.addObject("code",400);
        mav.addObject("msg","参数异常了");
        //判断
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod)handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断是否有ResponseBody
            if(responseBody==null){
                //返回视图名称
                if(e instanceof ParamsException){
                    ParamsException pe=(ParamsException)e;
                    mav.addObject("code",pe.getCode());
                    mav.addObject("msg",pe.getMsg());
                }
            }else {

                //返回json
                ResultInfo info=new ResultInfo();
                info.setCode(300);
                info.setMsg("参数异常了");
                if(e instanceof ParamsException){
                    ParamsException pe=(ParamsException)e;
                    info.setCode(pe.getCode());
                    info.setMsg(pe.getMsg());
                }

                resp.setContentType("application/json;charset=utf-8");

                PrintWriter pw=null;
                try {
                    pw= resp.getWriter();
                    pw.write(JSON.toJSONString(info));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    if(pw!=null){
                        pw.close();
                    }
                }
                return null;
            }

        }
        return mav;
    }
}
