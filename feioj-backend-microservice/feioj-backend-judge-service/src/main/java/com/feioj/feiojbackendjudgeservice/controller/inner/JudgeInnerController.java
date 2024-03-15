package com.feioj.feiojbackendjudgeservice.controller.inner;

import com.feioj.feiojbackendjudgeservice.judge.JudgeService;
import com.feioj.feiojbackendmodel.model.entity.QuestionSubmit;
import com.feioj.feiojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 该接口仅提供服务内部调用
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {
    @Autowired
    private JudgeService judgeService;
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
