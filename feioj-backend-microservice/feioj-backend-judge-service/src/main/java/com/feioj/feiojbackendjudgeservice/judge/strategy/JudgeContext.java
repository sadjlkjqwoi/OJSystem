package com.feioj.feiojbackendjudgeservice.judge.strategy;


import com.feioj.feiojbackendmodel.model.codesandbox.JudgeInfo;
import com.feioj.feiojbackendmodel.model.dto.question.JudgeCase;
import com.feioj.feiojbackendmodel.model.entity.Question;
import com.feioj.feiojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

@Data
public class JudgeContext {
private JudgeInfo judgeInfo;
private List<String> outputList;
private List<String> inputList;
private Question question;
private List<JudgeCase> judgeCase;
private QuestionSubmit questionSubmit;
}
