package com.feioj.feioj.Judge.codesandbox;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.feioj.feioj.judge.codesandbox.Codesandbox;
import com.feioj.feioj.judge.codesandbox.CodesandboxFactory;
import com.feioj.feioj.judge.codesandbox.CodesandboxProxy;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeRequest;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeResponse;
import com.feioj.feioj.model.enums.QuestSubmitLanguageEnum;
import com.feioj.feioj.model.enums.QuestionSubmitStatusEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
public class CodesandTest {
    @Value("remote")
    private  String type;
    @Test
    void excuteCodeByValue(){
        System.out.println(type);
            String code="int main{}";
            String language= QuestSubmitLanguageEnum.JAVA.getValue();
            List<String> inputList= Arrays.asList("1 2","3,6");
            Codesandbox codesandbox= CodesandboxFactory.newInstance(type);
            ExcuteCodeRequest excuteCodeRequest=ExcuteCodeRequest
                    .builder()
                    .code(code)
                    .language(language)
                    .inputList(inputList)
                    .build();
            ExcuteCodeResponse excuteCodeResponse=codesandbox.excuteCode(excuteCodeRequest);
    }
    @Test
    void excuteCodeByValueProxy(){
        System.out.println(type);
        String code="public class Main {\n" +
                "    public static\n" +
                "    void main(String[] args) {\n" +
                "        int a=Integer.parseInt(args[0]);\n" +
                "        int b=Integer.parseInt(args[1]);\n" +
                "        System.out.println(a+b);\n" +
                "    }\n" +
                "}\n";
        String language= QuestSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList= Arrays.asList("1 2","3 6");
        Codesandbox codesandbox= CodesandboxFactory.newInstance(type);
        //代理模式
        codesandbox= new CodesandboxProxy(codesandbox);
        ExcuteCodeRequest excuteCodeRequest=ExcuteCodeRequest
                .builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExcuteCodeResponse excuteCodeResponse=codesandbox.excuteCode(excuteCodeRequest);
        Assertions.assertNotNull(excuteCodeResponse);
    }
    public static void main(String[] args) {

        String type="example";
        Codesandbox codesandbox= CodesandboxFactory.newInstance(type);
            String code="int main{}";
            String language= QuestSubmitLanguageEnum.JAVA.getValue();
            List<String> inputList= Arrays.asList("1 2","3,6");
            ExcuteCodeRequest excuteCodeRequest=ExcuteCodeRequest
                    .builder()
                    .code(code)
                    .language(language)
                    .inputList(inputList)
                    .build();
            ExcuteCodeResponse excuteCodeResponse=codesandbox.excuteCode(excuteCodeRequest);

    }
}
