package com.feioj.feiojcodesandbox.model;

import lombok.Data;

/**
 * 进程执行信息封装
 */
@Data
public class ExecuteMessage {
    private Integer exitValue;

    private String errorMessage;

    private String Message;
    private Long time;
    private Long memory;
}
