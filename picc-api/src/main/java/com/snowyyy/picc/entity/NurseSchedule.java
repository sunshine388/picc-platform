package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_nurse_schedule")
public class NurseSchedule {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("nurse_name")
    private String nurseName;
    @TableField("job_number")
    private String jobNumber;
    @TableField("appointment_date")
    private String appointmentDate;
    @TableField("forenoon")
    private String forenoon;
    @TableField("forenoon_number")
    private String forenoonNumber;
    @TableField("forenoon_limit")
    private String forenoonLimit;
    @TableField("afternoon")
    private String afternoon;
    @TableField("afternoon_number")
    private String afternoonNumber;
    @TableField("afternoon_limit")
    private String afternoonLimit;
}
