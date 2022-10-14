package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_nurse")
public class Nurse {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("user_id")
    private String userId;
    @TableField("job_number")
    private String jobNumber;
    @TableField("name")
    private String name;
    @TableField("sex")
    private String sex;
    @TableField("phone")
    private String phone;
    @TableField("department")
    private String department;
    @TableField("create_time")
    private String createTime;
    @TableField("state")
    private String state;

}
