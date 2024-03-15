package com.feioj.feiojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feioj.feiojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.feioj.feiojbackendmodel.model.entity.Question;
import com.feioj.feiojbackendmodel.model.entity.QuestionSubmit;
import com.feioj.feiojbackendmodel.model.vo.QuestionVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author 23176
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-01-21 09:34:19
*/
@FeignClient(value = "feioj-backend-question-service",path = "/api/question/inner")
public interface QuestionFeignClient {
        @GetMapping("/get/id")
        Question getQuestionById(@RequestParam("questionId") long questionId);
        @GetMapping("/question_submit/get/id")
        QuestionSubmit getQuestionSubmitById(@RequestParam("questionId")long questionSubmitId);
        @PostMapping("/question_submit/update")
        boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
        @GetMapping("/question/update")
        boolean updateQuestion(@RequestParam ("updatequestionId") Long updatequestionId,@RequestParam("acceptedNum") int acceptedNum);


}
