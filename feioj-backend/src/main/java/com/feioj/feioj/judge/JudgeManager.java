package com.feioj.feioj.judge;

import com.feioj.feioj.judge.strategy.DefaultJudgeStrategy;
import com.feioj.feioj.judge.strategy.JavaLanguageStrategy;
import com.feioj.feioj.judge.strategy.JudgeContext;
import com.feioj.feioj.judge.strategy.JudgeStrategy;
import com.feioj.feioj.model.dto.question_submit.JudgeInfo;

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
