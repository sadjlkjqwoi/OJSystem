package com.feioj.feioj.model.vo;

import cn.hutool.json.JSONUtil;
import com.feioj.feioj.model.dto.question.JudgeConfig;
import com.feioj.feioj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 返回前端信息
 */
@Data
public class QuestionVo {
    /**
     * id
     */

    private Long id;

    /**
     * 标题
     */

    private String title;

    /**
     * 内容
     */

    private String content;

    /**
     * 标签列表（json 数组）
     */

    private List<String> tags;


    /**
     * 題目提交数
     */

    private Integer submitNum;

    /**
     * 题目通过数
     */

    private Integer acceptedNum;

    /**
     * 判题配置（json对象）
     */

    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */

    private Integer thumbNum;

    /**
     * 收藏数
     */

    private Integer favourNum;


    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 更新时间
     */

    private Date updateTime;


    /**
     * 创建用户信息
     */
    private UserVO userVO;




    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVo questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        question.setTags(JSONUtil.toJsonStr(tagList));
        JudgeConfig judgeConfigVO=questionVO.judgeConfig;
        question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfigVO));
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVo objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVo questionVO = new QuestionVo();
        BeanUtils.copyProperties(question, questionVO);
        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        questionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        return questionVO;
    }
    private static final long serialVersionUID = 1L;
}
