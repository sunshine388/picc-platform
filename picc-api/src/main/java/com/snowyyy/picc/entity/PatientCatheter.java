package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_patient_catheter")
public class PatientCatheter {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("patient_name")
    private String patientName;
    @TableField("patient_num")
    private String patientNumber;
    @TableField("disease_type")
    private String diseaseType;
    @TableField("first_catheter")
    private String firstCatheter;
    @TableField("catheter_time")
    private String catheterTime;
    @TableField("catheter_department")
    private String catheterDepartment;
    @TableField("catheter_opportunity")
    private String catheterOpportunity;
    @TableField("catheter_model")
    private String catheterModel;
    @TableField("catheter_specification")
    private String catheterSpecification;
    @TableField("catheter_batchnumber")
    private String catheterBatchnumber;
    @TableField("catheter_length")
    private String catheterLength;
    @TableField("puncture_method")
    private String punctureMethod;
    @TableField("catheter_site")
    private String catheterSite;
    @TableField("catheter_arm")
    private String catheterArm;
    @TableField("catheter_vein")
    private String catheterVein;
    @TableField("catheter_tip_position")
    private String catheterTipPosition;
    @TableField("catheter_insertion_length")
    private String catheterInsertionLength;
    @TableField("catheter_exposed_length")
    private String catheterExposedLength;
    @TableField("arm_circumference")
    private String armCircumference;
    @TableField("chemotherapy_number")
    private String chemotherapyNumber;

    @TableField("catheter_brand")
    private String catheterBrand;
    @TableField("nurse_jobnumber")
    private String nurseJobnumber;
    @TableField("catheter_num")
    private String catheterNum;
    @TableField("record_num")
    private String recordNum;
    @TableField("catheter_hospital")
    private String catheterHospital;
    @TableField("number_of_catheterization")
    private String numberOfCatheterization;
    @TableField("create_time")
    private String createTime;
}
