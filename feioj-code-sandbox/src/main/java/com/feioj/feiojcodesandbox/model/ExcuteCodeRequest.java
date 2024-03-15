package com.feioj.feiojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcuteCodeRequest {
    /**
     * 输入用例
     */
    private  List<String> inputList;
    /**
     * 代码语言
     */
    private String language;

    /**
     * 输入代码
     *
     */
    private String code;
}
