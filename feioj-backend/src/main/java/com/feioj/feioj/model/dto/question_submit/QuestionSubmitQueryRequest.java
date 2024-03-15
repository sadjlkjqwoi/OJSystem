package com.feioj.feioj.model.dto.question_submit;

import com.feioj.feioj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest  extends PageRequest implements Serializable {

    /**
     * 编程语言
     */

    private String language;

    /**
     * 提交状态
     */

    private Integer status;

    /**
     * 题目id
     */
    private Long questionId;


    /**
     * 用户id
     */
    private  Long userId;

    private static final long serialVersionUID = 1L;
}
