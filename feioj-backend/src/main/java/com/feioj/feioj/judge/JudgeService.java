package com.feioj.feioj.judge;


import com.feioj.feioj.model.entity.QuestionSubmit;


public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
