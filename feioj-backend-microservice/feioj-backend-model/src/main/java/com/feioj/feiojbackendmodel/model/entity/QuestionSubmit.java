package com.feioj.feiojbackendmodel.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交表
 * @TableName question_submit
 */
@TableName(value ="question_submit")
@Data
public class QuestionSubmit implements Serializable {
    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编程语言
     */
    @TableField(value = "language")
    private String language;

    /**
     * 用户代码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 判题信息（json 对象）
     */
    @TableField(value = "judgeInfo")
    private String judgeInfo;

    /**
     * 判题状态（0-待判题  1-判题中 2-判题成功 3-判题失败）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 题目id
     */
    @TableField(value = "questionId")
    private Long questionId;

    /**
     * 用户id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;
    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
