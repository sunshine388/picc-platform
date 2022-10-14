package com.snowyyy.picc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("picc_patient_appointment")
public class PatientAppointment {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    @TableField("patient_num")
    private String patientNumber;
    @TableField("patient_name")
    private String patientName;
    @TableField("nurse_name")
    private String nurseName;
    @TableField("job_number")
    private String jobNumber;
    @TableField("appointment_date")
    private String appointmentDate;
    @TableField("appointment_time")
    private String appointmentTime;
    @TableField("type")
    private String type;
    @TableField("status")
    private String status;
    @TableField("c_time")
    private String ctime;
    @TableField("record_id")
    private String recordId;
    @TableField("schedule_id")
    private String scheduleId;
}
