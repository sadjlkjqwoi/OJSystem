package com.feioj.feiojbackendjudgeservice.judge.codesandbox;


import com.feioj.feiojbackendjudgeservice.judge.codesandbox.example.ExampleCodesandbox;
import com.feioj.feiojbackendjudgeservice.judge.codesandbox.example.RemoteCodesandbox;
import com.feioj.feiojbackendjudgeservice.judge.codesandbox.example.ThirdParty;

/**
 * 代码沙箱工厂（根据参数判断使用哪种沙箱）
 */
public class CodesandboxFactory {
    /**
     * 创建代码沙箱示例
     * @param type
     * @return
     */
    public  static  Codesandbox newInstance(String type){
        System.out.println(type);
        switch (type){
            case "example":
               return new ExampleCodesandbox();
            case "remote":
                return  new RemoteCodesandbox();
            case "thirdParty":
                return  new ThirdParty();
            default:
                return new ExampleCodesandbox();
        }
    }
}
