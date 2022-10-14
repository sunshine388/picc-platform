package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_patient_maintain")
public class PatientMaintain {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("patient_name")
    private String patientName;
    @TableField("patient_num")
    private String patientNumber;
    @TableField("maintain_date")
    private String maintainDate;
    @TableField("pipeline_unobstructed")
    private String pipelineUnobstructed;
    @TableField("change_dressing")
    private String changeDressing;
    @TableField("replace_connector")
    private String replaceConnector;
    @TableField("resistance")
    private String resistance;
    @TableField("companion")
    private String companion;
    @TableField("treatment_process")
    private String treatmentProcess;

    @TableField("blood_return")
    private String bloodReturn;
    @TableField("catheter_exposed_length")
    private String catheterExposedLength;
    @TableField("nurse_jobnumber")
    private String nurseJobnumber;
    @TableField("maintain_num")
    private String maintainNum;
    @TableField("record_num")
    private String recordNum;

}
