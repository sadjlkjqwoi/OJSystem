package com.feioj.feioj.judge.codesandbox.model;

import com.feioj.feioj.model.dto.question_submit.JudgeInfo;
import io.swagger.models.auth.In;
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
