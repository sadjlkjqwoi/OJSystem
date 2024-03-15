package com.feioj.feiojbackendjudgeservice.judge;


import com.feioj.feiojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
