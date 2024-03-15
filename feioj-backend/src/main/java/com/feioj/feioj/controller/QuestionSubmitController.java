package com.feioj.feioj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feioj.feioj.annotation.AuthCheck;
import com.feioj.feioj.common.BaseResponse;
import com.feioj.feioj.common.ErrorCode;
import com.feioj.feioj.common.ResultUtils;
import com.feioj.feioj.constant.UserConstant;
import com.feioj.feioj.exception.BusinessException;
import com.feioj.feioj.model.dto.postthumb.PostThumbAddRequest;
import com.feioj.feioj.model.dto.question.QuestionAddRequest;
import com.feioj.feioj.model.dto.question.QuestionQueryRequest;
import com.feioj.feioj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.feioj.feioj.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.feioj.feioj.model.entity.Question;
import com.feioj.feioj.model.entity.QuestionSubmit;
import com.feioj.feioj.model.entity.User;
import com.feioj.feioj.model.vo.QuestionSubmitVo;
import com.feioj.feioj.service.PostThumbService;
import com.feioj.feioj.service.QuestionSubmitService;
import com.feioj.feioj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 */
@RestController
//@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {


    @Resource
    private UserService userService;

    @Resource
    private QuestionSubmitService questionSubmitService;
//    /**
//     *提交题目
//     * @param questionSubmitAddRequest
//     * @param request
//     * @return 提交记录id
//     */
//    @PostMapping("/")
//    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
//                                         HttpServletRequest request) {
//            if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 登录才能提交
//        final User loginUser = userService.getLoginUser(request);
//        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
//        return ResultUtils.success(result);
//    }
//
//    /**
//     * 分页获取列表（仅管理员）
//     *
//     * @param questionSubmitQueryRequest
//     * @return
//     */
//    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Page<QuestionSubmitVo>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
//        long current = questionSubmitQueryRequest.getCurrent();
//        long size = questionSubmitQueryRequest.getPageSize();
//        QueryWrapper<QuestionSubmit> queryWrappers=questionSubmitService.getQueryWrapper(questionSubmitQueryRequest);
//        Page<QuestionSubmit> questionPage = questionSubmitService.page(new Page<>(current, size),
//                queryWrappers);
//        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionPage,request));
//    }

}
