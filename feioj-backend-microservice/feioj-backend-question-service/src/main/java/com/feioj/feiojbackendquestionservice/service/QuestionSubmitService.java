package com.feioj.feiojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feioj.feiojbackendmodel.model.dto.question_submit.QuestionSubmitAddRequest;
import com.feioj.feiojbackendmodel.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.feioj.feiojbackendmodel.model.entity.QuestionSubmit;
import com.feioj.feiojbackendmodel.model.entity.User;
import com.feioj.feiojbackendmodel.model.vo.QuestionSubmitVo;


import javax.servlet.http.HttpServletRequest;

/**
* @author 23176
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2024-01-21 09:36:00
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * 获取提交记录封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVo getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取提交记录封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVo> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request);

}
