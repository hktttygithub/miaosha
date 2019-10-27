package com.flashsale.excception;

import com.flashsale.result.CodeMsg;
import com.flashsale.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
@ControllerAdvice

public class GlobalExceptionHandler {
    //处理ajax请求的异常
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e) {
        System.out.println("异常拦截！");
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        } else {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    //处理需要跳转页面的异常
//    @ExceptionHandler(value = ForwardException.class)
//    public String exceptionHandler(HttpServletRequest request, Exception e, Model model){
//        model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//        return "miaosha_fail";
//    }

    @ExceptionHandler(value = ForwardException.class)
    public void MiaoshaOver(){

    }
}
