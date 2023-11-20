package com.personal.blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 聊天记录
 * </p>
 *
 * @author 作者
 * @since 2023-10-01
 */
@Getter
@Setter
@TableName("blog_chatting_records")
@ToString
public class BlogChattingRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发送方用户id
     */
    private Long senderUserId;

    /**
     * 接收方用户id
     */
    private Long acceptorUserId;

    /**
     * 聊天内容id
     */
    private Long chattingRecordsContentMappingId;

    /**
     * 0:未读，1:已读
     */
    private Integer readeStatus;

    /**
     *
     * 0:在推送列表里，1：不在
     */
    private Integer active;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 0:未删除，1:已删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private BlogUser senderUser;

    @TableField(exist = false)
    private BlogUser acceptorUser;


    @TableField(exist = false)
    private BlogChattingRecordsContentMapping chattingRecordsContentMapping;
}
