package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_patient")
public class Patient {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("user_id")
    private String userId;
    @TableField("patient_num")
    private String patientNumber;
    @TableField("name")
    private String name;
    @TableField("sex")
    private String sex;
    @TableField("birthday")
    private String birthday;
    @TableField("culture")
    private String culture;
    @TableField("city")
    private String city;
    @TableField("phone")
    private String phone;
    @TableField("create_time")
    private String createTime;
    @TableField("address")
    private String address;

}
