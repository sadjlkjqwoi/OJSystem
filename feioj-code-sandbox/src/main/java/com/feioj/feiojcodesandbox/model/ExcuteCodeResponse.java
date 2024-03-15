package com.feioj.feiojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcuteCodeResponse {

    private List<String> outputList;
    /**
     * 接口信息
     */
    private  String message;
    /**
     * 执行状态
     * =
     */
    private Integer status;


    /**
     * 判断信息
     */
    private JudgeInfo judgeInfo;
}
