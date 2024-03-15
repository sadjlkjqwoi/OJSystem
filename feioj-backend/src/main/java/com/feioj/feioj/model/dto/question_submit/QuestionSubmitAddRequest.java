package com.feioj.feioj.model.dto.question_submit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

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
