package com.snowyyy.picc.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("message_list")
public class MessageList {
    private static final long serialVersionUID = 1L;
    @TableId(value = "list_id", type = IdType.AUTO)
    private String listId;
    @TableField("from_openid")
    private String fromOpenid;
    @TableField("from_name")
    private String fromName;
    @TableField("from_headImage")
    private String fromHeadImage;
    @TableField("from_state")
    private String fromState;
    @TableField("last_content")
    private String lastContent;
    @TableField("to_openid")
    private String toOpenid;
    @TableField("send_time")
    private String sendTime;
}