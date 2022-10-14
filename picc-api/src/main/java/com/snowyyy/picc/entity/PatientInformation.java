package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_patient_information")
public class PatientInformation {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("patient_name")
    private String patientName;
    @TableField("patient_number")
    private String patientNumber;
    @TableField("next_time")
    private String nextTime;
    @TableField("has_catheter")
    private String hasCatheter;
    @TableField("catheter_type")
    private String catheterType;
    @TableField("animal_heat")
    private String animalHeat;
    @TableField("previous_history")
    private String previousHistory;
    @TableField("drug_allergy_history")
    private String drugAllergyHistory;
    @TableField("record_num")
    private String recordNum;
}