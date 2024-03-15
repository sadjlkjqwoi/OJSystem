package com.feioj.feiojbackendquestionservice.controller.inner;

import com.feioj.feiojbackendmodel.model.entity.Question;
import com.feioj.feiojbackendmodel.model.entity.QuestionSubmit;
import com.feioj.feiojbackendquestionservice.service.QuestionService;
import com.feioj.feiojbackendquestionservice.service.QuestionSubmitService;
import com.feioj.feiojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionSubmitService questionSubmitService;
    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId){
        System.out.println("2222"+questionId);
        System.out.println(questionService.getById(questionId));
        return questionService.getById(questionId);
    }
    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId")long questionSubmitId){
     return    questionSubmitService.getById(questionSubmitId);
    }
    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit){
     return    questionSubmitService.updateById(questionSubmit);
    }
    @GetMapping("/question/update")
    @Override
     public boolean updateQuestion(@RequestParam ("updatequestionId") Long updatequestionId,@RequestParam("acceptedNum") int acceptedNum){

        Question question= new Question();
        question.setId(updatequestionId);
        question.setAcceptedNum(acceptedNum);

        return    questionService.updateById(question);
    }



}
