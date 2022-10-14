package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_patient_record")
public class PatientRecord {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("patient_name")
    private String patientName;
    @TableField("patient_num")
    private String patientNumber;
    @TableField("record_num")
    private String recordNum;
    @TableField("c_time")
    private String cTime;

}
