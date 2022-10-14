package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_patient_extubation")
public class PatientExtubation {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("patient_name")
    private String patientName;
    @TableField("patient_num")
    private String patientNumber;
    @TableField("extubation_date")
    private String extubationDate;
    @TableField("indwelling_time")
    private String indwellingTime;
    @TableField("reason")
    private String reason;
    @TableField("type")
    private String type;
    @TableField("end_intact")
    private String endIntact;
    @TableField("process_smooth")
    private String processSmooth;
    @TableField("extubation_num")
    private String extubationNum;
    @TableField("record_num")
    private String recordNum;
    @TableField("nurse_jobnumber")
    private String nurseJobnumber;
}
