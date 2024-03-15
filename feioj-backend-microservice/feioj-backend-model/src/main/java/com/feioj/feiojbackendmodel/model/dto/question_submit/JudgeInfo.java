package com.feioj.feiojbackendmodel.model.dto.question_submit;

import lombok.Data;

/**
 * 判断信息
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private  String message;

    /**
     * 消耗内存
     *
     */
    private  Long memory;

    /**
     * 消耗时间
     */
    private  Long time;
}
