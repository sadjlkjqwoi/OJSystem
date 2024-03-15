package com.feioj.feiojbackendmodel.model.dto.question_submit;

import lombok.Data;

/**
 * 题目提交信息
 */
@Data
public class QuestionSubmitAddRequest {

    /**
     * 编程语言
     */

    private String language;

    /**
     * 用户代码
     */

    private String code;

    /**
     * 题目id
     */
    private Long questionId;


    private static final long serialVersionUID = 1L;
}
