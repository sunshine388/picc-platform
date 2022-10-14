package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_admin_manager")
public class Manager {
    private static final long serialVersionUID = 1L;
    @TableId(value = "mg_id",type = IdType.AUTO)
    private String id;
    @TableField("mg_name")
    private String name;
    @TableField("mg_pwd")
    private String passWord;
    @TableField("mg_ctime")
    private String createTime;
    @TableField("mg_phone")
    private String phone;
    @TableField("mg_email")
    private String email;
    @TableField("mg_state")
    private String state;

}
