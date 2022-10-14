package com.snowyyy.picc.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("message_detail")
public class MessageDetail {
    private static final long serialVersionUID = 1L;
    @TableId(value = "message_id", type = IdType.AUTO)
    private String mesageId;
    @TableField("from_openid")
    private String fromOpenid;
    @TableField("content")
    private String content;
    @TableField("to_openid")
    private String toOpenid;
    @TableField("send_time")
    private String sendTime;
}