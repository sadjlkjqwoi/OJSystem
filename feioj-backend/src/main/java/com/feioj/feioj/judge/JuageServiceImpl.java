package com.feioj.feioj.judge;

import cn.hutool.json.JSONUtil;
import com.feioj.feioj.common.ErrorCode;
import com.feioj.feioj.exception.BusinessException;
import com.feioj.feioj.judge.codesandbox.Codesandbox;
import com.feioj.feioj.judge.codesandbox.CodesandboxFactory;
import com.feioj.feioj.judge.codesandbox.CodesandboxProxy;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeRequest;
import com.feioj.feioj.judge.codesandbox.model.ExcuteCodeResponse;
import com.feioj.feioj.judge.strategy.DefaultJudgeStrategy;
import com.feioj.feioj.judge.strategy.JudgeContext;
import com.feioj.feioj.model.dto.question.JudgeCase;
import com.feioj.feioj.model.dto.question.JudgeConfig;
import com.feioj.feioj.model.dto.question_submit.JudgeInfo;
import com.feioj.feioj.model.entity.Question;
import com.feioj.feioj.model.entity.QuestionSubmit;
import com.feioj.feioj.model.enums.JudgeInfoMessage;
import com.feioj.feioj.model.enums.QuestionSubmitStatusEnum;
import com.feioj.feioj.model.vo.QuestionSubmitVo;
import com.feioj.feioj.service.QuestionService;
import com.feioj.feioj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JuageServiceImpl implements JudgeService{
    @Value("${codesandbox.type:remote}")
    private String type;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionSubmitService questionSubmitService;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {

        //1.根据提交记录的id,获取题目提交的信息
        QuestionSubmit questionSubmit=questionSubmitService.getById(questionSubmitId);
        if (questionSubmit==null){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交不存在");
        }
        Question question=questionService.getById(questionSubmit.getQuestionId());
        if (question==null){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        //2.判断题目的状态并且更新题目的状态，避免重复判题
        if (questionSubmit.getStatus()!= QuestionSubmitStatusEnum.WAITING.getValue()){
            throw  new BusinessException(ErrorCode.OPERATION_ERROR,"题目正在判题中");
        }
        QuestionSubmit questionSubmitUpdate=new QuestionSubmit();
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionSubmitUpdate.setId(questionSubmitId);
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        String code=questionSubmit.getCode();
        String language=questionSubmit.getLanguage();

        //String字符串转换成JSON数组
        List<JudgeCase> judgeCaseList= JSONUtil.toList(question.getJudgeCase(),JudgeCase.class);
        List<String> inputList=judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExcuteCodeRequest excuteCodeRequest=ExcuteCodeRequest
                .builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        //3.提交信息到沙箱中执行
        Codesandbox codesandbox= CodesandboxFactory.newInstance(type);
        //4.调用代理模式

        codesandbox=new CodesandboxProxy(codesandbox);

        ExcuteCodeResponse excuteCodeResponse=codesandbox.excuteCode(excuteCodeRequest);

        List<String> outputList=excuteCodeResponse.getOutputList();

        //5.代码沙箱判断完成后，返回判断信息
        JudgeManager judgeManager=new JudgeManager();
        JudgeContext judgeContext=new JudgeContext();
        judgeContext.setJudgeInfo(excuteCodeResponse.getJudgeInfo());
        judgeContext.setQuestion(question);
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCase(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo=judgeManager.doJudge(judgeContext);
        //更新题目的状态信息
        questionSubmitUpdate=new QuestionSubmit();
      if (judgeInfo.getMessage().equals(JudgeInfoMessage.ACCEPTED.getValue())){
          questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
          Question updateQuestion=new Question();
          Question oldQuestion=questionService.getById(questionSubmit.getQuestionId());
          updateQuestion.setAcceptedNum(oldQuestion.getAcceptedNum()+1);
          updateQuestion.setId(questionSubmit.getQuestionId());
          Boolean updateQuestionState=questionService.updateById(updateQuestion);
          if (!updateQuestionState){
              throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
          }
      }
      else {
          questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
      }
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交状态更新错误");
        }
        QuestionSubmit questionSubmitResult=questionSubmitService.getById(questionSubmitId);
        System.out.println("提交记录"+questionSubmitResult);
      return questionSubmitResult;
    }
}
