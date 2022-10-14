package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("picc_wx_user")
public class WxUser {
    private static final long serialVersionUID = 1L;
    @TableId(value = "open_id",type = IdType.INPUT)
    private String openId;
    @TableField("skey")
    private String skey;
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    @TableField("last_visit_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastVisitTime;
    @TableField("session_key")
    private String sessionKey;
    @TableField("city")
    private String city;
    @TableField("province")
    private String province;
    @TableField("country")
    private String country;
    @TableField("avatar_url")
    private String avatarUrl;
    @TableField("gender")
    private Integer gender;
    @TableField("nick_name")
    private String nickName;
}
