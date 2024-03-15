package com.feioj.feioj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feioj.feioj.model.dto.question.QuestionQueryRequest;
import com.feioj.feioj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.feioj.feioj.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.feioj.feioj.model.entity.Question;
import com.feioj.feioj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feioj.feioj.model.entity.User;
import com.feioj.feioj.model.vo.QuestionSubmitVo;
import com.feioj.feioj.model.vo.QuestionVo;
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
    QuestionSubmitVo doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
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
