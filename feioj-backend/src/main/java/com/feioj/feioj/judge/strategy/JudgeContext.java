package com.feioj.feioj.judge.strategy;

import com.feioj.feioj.model.dto.question.JudgeCase;
import com.feioj.feioj.model.dto.question_submit.JudgeInfo;
import com.feioj.feioj.model.entity.Question;
import com.feioj.feioj.model.entity.QuestionSubmit;
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
