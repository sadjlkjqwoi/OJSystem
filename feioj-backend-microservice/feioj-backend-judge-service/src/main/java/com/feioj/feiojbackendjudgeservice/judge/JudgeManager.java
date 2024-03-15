package com.feioj.feiojbackendjudgeservice.judge;


import com.feioj.feiojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.feioj.feiojbackendjudgeservice.judge.strategy.JavaLanguageStrategy;
import com.feioj.feiojbackendjudgeservice.judge.strategy.JudgeContext;
import com.feioj.feiojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.feioj.feiojbackendmodel.model.codesandbox.JudgeInfo;

public class JudgeManager {


    public JudgeInfo doJudge(JudgeContext judgeContext) {

        String language=judgeContext.getQuestionSubmit().getLanguage();

        JudgeStrategy judgeStrategy=new DefaultJudgeStrategy();
        if (language.equals("java")){

            judgeStrategy=new JavaLanguageStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}
