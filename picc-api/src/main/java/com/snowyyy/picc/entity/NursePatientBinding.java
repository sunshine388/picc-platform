package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("picc_patient_binding")
public class NursePatientBinding {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("job_number")
    private String jobNumber;
    @TableField("nurse_name")
    private String nurseName;
    @TableField("patient_num")
    private String patientNumber;
    @TableField("patient_name")
    private String patientName;
    @TableField("create_time")
    private String createTime;

}