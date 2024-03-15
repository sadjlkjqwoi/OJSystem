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
 * 题目
 * @TableName question
 */
@TableName(value ="question")
@Data
public class Question implements Serializable {
    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 标签列表（json 数组）
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 題目答案
     */
    @TableField(value = "answer")
    private String answer;

    /**
     * 題目提交数
     */
    @TableField(value = "submitNum")
    private Integer submitNum;

    /**
     * 题目通过数
     */
    @TableField(value = "acceptedNum")
    private Integer acceptedNum;

    /**
     * 判题用例（json 数组）
     */
    @TableField(value = "judgeCase")
    private String judgeCase;

    /**
     * 判题配置（json对象）
     */
    @TableField(value = "judgeConfig")
    private String judgeConfig;

    /**
     * 点赞数
     */
    @TableField(value = "thumbNum")
    private Integer thumbNum;

    /**
     * 收藏数
     */
    @TableField(value = "favourNum")
    private Integer favourNum;

    /**
     * 创建用户 id
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
