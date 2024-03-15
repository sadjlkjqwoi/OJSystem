package com.feioj.feiojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.feioj.feiojbackendmodel.model.dto.question_submit.JudgeInfo;
import com.feioj.feiojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 返回前端信息
 */
@Data
public class QuestionSubmitVo {

    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（0-待判题  1-判题中 2-判题成功 3-判题失败）
     */
    private Integer status;

    /**
     * 题目id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long questionId;

    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 提交用户信息
     */
    private UserVO userVO;

    /**
     * 提交题目信息
     */
    private  QuestionVo questionVo;



    /**
     * 包装类转对象
     *
     * @param questionSubmitVo
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVo questionSubmitVo) {
        if (questionSubmitVo == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVo, questionSubmit);
        JudgeInfo judgeInfoVO=questionSubmitVo.judgeInfo;
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoVO));
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVo objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVo questionSubmitVo = new QuestionSubmitVo();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVo);
        questionSubmitVo.setJudgeInfo(JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class));
        return questionSubmitVo;
    }
    private static final long serialVersionUID = 1L;
}
