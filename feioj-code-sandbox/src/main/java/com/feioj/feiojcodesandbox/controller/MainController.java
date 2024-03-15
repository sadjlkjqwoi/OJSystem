package com.feioj.feiojcodesandbox.controller;

import com.feioj.feiojcodesandbox.JavaNativeCodeSandbox;
import com.feioj.feiojcodesandbox.model.ExcuteCodeRequest;
import com.feioj.feiojcodesandbox.model.ExcuteCodeResponse;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HTTP;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("/")
public class MainController {
    @Resource
   private JavaNativeCodeSandbox javaNativeCodeSandbox;
    private static final String HTTP_REQUESR_SECRET="000";
    private static final String HTTP_REQUEST_HEADER="auth";

    /**
     * 执行代码，提供沙箱API
     * @param excuteCodeRequest
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/executeCode")
    public ExcuteCodeResponse excuteCode(@RequestBody ExcuteCodeRequest excuteCodeRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws InterruptedException {
        //对请求进行鉴权
        String Head=httpServletRequest.getHeader(HTTP_REQUEST_HEADER);
        if (!Head.equals(HTTP_REQUESR_SECRET)){
            httpServletResponse.setStatus(403);
            return  null;
        }
        if (excuteCodeRequest==null){
            throw  new RuntimeException("请求参数为空");
        }

        return  javaNativeCodeSandbox.excuteCode(excuteCodeRequest);
    }
}
