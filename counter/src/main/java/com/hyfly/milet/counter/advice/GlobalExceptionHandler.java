package com.hyfly.milet.counter.advice;


import com.hyfly.milet.counter.module.res.CounterRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public CounterRes exceptionHandler(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return new CounterRes(CounterRes.FAIL, "发生错误", null);
    }

}
