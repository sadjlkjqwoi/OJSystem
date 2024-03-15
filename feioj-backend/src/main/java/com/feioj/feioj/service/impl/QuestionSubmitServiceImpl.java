package com.feioj.feioj.service.impl;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feioj.feioj.common.ErrorCode;
import com.feioj.feioj.constant.CommonConstant;
import com.feioj.feioj.exception.BusinessException;
import com.feioj.feioj.judge.JudgeService;
import com.feioj.feioj.model.dto.question.QuestionQueryRequest;
import com.feioj.feioj.model.dto.question_submit.QuestionSubmitAddRequest;
import com.feioj.feioj.model.dto.question_submit.QuestionSubmitQueryRequest;
import com.feioj.feioj.model.entity.Question;
import com.feioj.feioj.model.entity.QuestionSubmit;
import com.feioj.feioj.model.entity.User;
import com.feioj.feioj.model.enums.QuestSubmitLanguageEnum;
import com.feioj.feioj.model.enums.QuestionSubmitStatusEnum;
import com.feioj.feioj.model.vo.QuestionSubmitVo;

import com.feioj.feioj.service.QuestionService;
import com.feioj.feioj.service.QuestionSubmitService;
import com.feioj.feioj.mapper.QuestionSubmitMapper;
import com.feioj.feioj.service.UserService;
import com.feioj.feioj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author 23176
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2024-01-21 09:36:00
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;
    @Autowired
    @Lazy
    private JudgeService judgeService;
    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public QuestionSubmitVo doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
       //判断编程语言是否合法
        String language=questionSubmitAddRequest.getLanguage();
        QuestSubmitLanguageEnum questSubmitLanguageEnum=QuestSubmitLanguageEnum.getEnumByValue(language);
        if (questSubmitLanguageEnum==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言不合法");
        }
        long questionId= questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit=new QuestionSubmit();
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setUserId(userId);
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        //设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        Question questionUpdate=new Question();
        Question questionOld=questionService.getById(questionSubmit.getQuestionId());
        questionUpdate.setId(questionOld.getId());
        questionUpdate.setSubmitNum(questionOld.getSubmitNum()+1);
        Boolean update=questionService.updateById(questionUpdate);
        if (!update){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        boolean result=this.save(questionSubmit);
        if (!result){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }
       long questionSubmitIdId= questionSubmit.getId();
        System.out.println("提交id："+questionSubmitIdId);
        //异步执行执行判题服务
        final QuestionSubmit[] questionSubmitResult = new QuestionSubmit[1];
//        CompletableFuture.runAsync(()->{
//
//         questionSubmitResult[0] =  judgeService.doJudge(questionSubmitIdId);
//        });
        questionSubmitResult[0] =  judgeService.doJudge(questionSubmitIdId);
        System.out.println("判题信息："+questionSubmitResult[0]);
        return QuestionSubmitVo.objToVo(questionSubmitResult[0]) ;
    }
    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status)!=null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVo getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVo questionSubmitVo = QuestionSubmitVo.objToVo(questionSubmit);
        //如果用户不是本人或管理员，则需要脱敏
        if (loginUser.getId()!=questionSubmit.getUserId()&&!userService.isAdmin(loginUser)){
            questionSubmitVo.setCode(null);
        }
        return questionSubmitVo;
    }

    @Override
    public Page<QuestionSubmitVo> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVo> questionSubmitVoPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtil.isEmpty(questionSubmitList)){
            return questionSubmitVoPage ;
        }
        User loginUser=userService.getLoginUser(request);
        List<QuestionSubmitVo> questionSubmitVoList=questionSubmitList.stream().map(questionSubmit -> getQuestionSubmitVO(questionSubmit,loginUser)).collect(Collectors.toList());
        questionSubmitVoPage.setRecords(questionSubmitVoList);
        return questionSubmitVoPage;
    }
}




