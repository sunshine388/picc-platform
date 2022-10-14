package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Nurse;
import com.snowyyy.picc.entity.PatientAppointment;
import com.snowyyy.picc.entity.PatientInformation;
import com.snowyyy.picc.entity.WxUser;
import com.snowyyy.picc.mapper.NurseMapper;
import com.snowyyy.picc.mapper.PatientAppointmentMapper;
import com.snowyyy.picc.mapper.PatientInformationMapper;
import com.snowyyy.picc.mapper.WxUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;


@CrossOrigin
@Controller
@RequestMapping("/nurse")
@Api(tags = "微信护士接口")
public class WxNurseController {
    @Autowired
    private NurseMapper nurseMapper;
    @Autowired
    private WxUserMapper userMapper;
    @Autowired
    private PatientAppointmentMapper patientAppointmentMapper;
    @Autowired
    private PatientInformationMapper patientInformationMapper;

    @ApiOperation(value = "护士信息提交")
    @PostMapping("/create")
    @ResponseBody
    public GlobalResult nurse(@RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "sex", required = false) String sex,
                                @RequestParam(value = "skey", required = false) String skey,
                                @RequestParam(value = "jobNumber", required = false) String jobNumber,
                                @RequestParam(value = "department", required = false) String department){

        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        Date date = new Date();
        String message;
        Nurse useritem=this.nurseMapper.selectOne(new QueryWrapper<Nurse>().eq("user_id", user.getOpenId()));
        if(useritem==null){
            Nurse nurse=new Nurse();
            nurse.setName(username);
            nurse.setUserId(user.getOpenId());
            nurse.setSex(sex);
            nurse.setPhone(phone);
            nurse.setJobNumber(jobNumber);
            nurse.setDepartment(department);
            nurse.setState("0");
            nurse.setCreateTime(String.valueOf(date.getTime()));
            int a = nurseMapper.insert(nurse);
            if (a == 1) {
                message = "用户信息提交成功";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
            } else {
                message = "用户信息提交失败1";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }else {
            Nurse nurse = this.nurseMapper.selectById(useritem.getId());
            nurse.setName(username);
            nurse.setUserId(user.getOpenId());
            nurse.setSex(sex);
            nurse.setPhone(phone);
            nurse.setJobNumber(jobNumber);
            nurse.setDepartment(department);
            nurse.setState("0");
            nurse.setCreateTime(String.valueOf(date.getTime()));
            int a = nurseMapper.updateById(nurse);
            if (a == 1) {
                message = "用户信息提交成功";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
            } else {
                message = "用户信息提交失败2";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }

    }
    @ApiOperation(value = "护士信息更新")
    @PostMapping("/update")
    @ResponseBody
    public GlobalResult nurseUpdate(@RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "sex", required = false) String sex,
                              @RequestParam(value = "skey", required = false) String skey,
                              @RequestParam(value = "jobNumber", required = false) String jobNumber,
                              @RequestParam(value = "department", required = false) String department){

        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        String message;
        Nurse useritem=this.nurseMapper.selectOne(new QueryWrapper<Nurse>().eq("user_id", user.getOpenId()));
        if(useritem!=null){
            Nurse nurse = this.nurseMapper.selectById(useritem.getId());
            nurse.setName(username);
            nurse.setSex(sex);
            nurse.setPhone(phone);
            nurse.setJobNumber(jobNumber);
            nurse.setDepartment(department);
            int a = nurseMapper.updateById(nurse);
            if (a == 1) {
                message = "用户信息修改成功";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
            } else {
                message = "用户信息修改失败";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }else {
            GlobalResult result = GlobalResult.build(200, "出错啦", null);
            return result;
        }
    }
    @ApiOperation(value = "护士更新患者信息")
    @PostMapping("/updatePatientInformation")
    @ResponseBody
    public GlobalResult updatePatientInformation(@RequestParam(value = "patientname", required = false) String patientname,
                                    @RequestParam(value = "patientnum", required = false) String patientnum,
                                                 @RequestParam(value = "animalHeat", required = false) String animalHeat,
                                                 @RequestParam(value = "drugAllergyHistory", required = false) String drugAllergyHistory,
                                                 @RequestParam(value = "catheterType", required = false) String catheterType,
                                                 @RequestParam(value = "previousHistory", required = false) String previousHistory){

        PatientInformation patient = this.patientInformationMapper.selectOne(new QueryWrapper<PatientInformation>().eq("patient_number",patientnum));
        if(patient!=null){
            patient.setAnimalHeat(animalHeat);
            patient.setDrugAllergyHistory(drugAllergyHistory);
            patient.setCatheterType(catheterType);
            patient.setPreviousHistory(previousHistory);
            patientInformationMapper.updateById(patient);
            GlobalResult result = GlobalResult.build(200, "修改成功", null);
            return result;
        }else {
            PatientInformation newpatient=new PatientInformation();
            newpatient.setPatientName(patientname);
            newpatient.setPatientNumber(patientnum);
            newpatient.setAnimalHeat(animalHeat);
            newpatient.setDrugAllergyHistory(drugAllergyHistory);
            newpatient.setCatheterType(catheterType);
            newpatient.setPreviousHistory(previousHistory);
            patientInformationMapper.insert(newpatient);

            GlobalResult result = GlobalResult.build(200, "提交成功", null);
            return result;
        }
    }
    @ApiOperation(value = "根据护士工号获取预约信息")
    @GetMapping("/get_appointmentByJobnumber")
    @ResponseBody
    public GlobalResult getAppointmentByJobnumber(@RequestParam(value = "jobnumber", required = false) String jobnumber){
        List<PatientAppointment> appointment = this.patientAppointmentMapper.selectList(new QueryWrapper<PatientAppointment>().eq("job_number", jobnumber));
        JSONArray data=new JSONArray();
        if(appointment!=null){
            for(PatientAppointment item:appointment){
                JSONObject object=new JSONObject();
                object.put("id",item.getId());
                object.put("patientName",item.getPatientName());
                object.put("patientNumber",item.getPatientNumber());
                object.put("appointmentDate",item.getAppointmentDate());
                object.put("appointmentTime",item.getAppointmentTime());
                object.put("type",item.getType());
                object.put("status",item.getStatus());
                object.put("recordId",item.getRecordId());
                object.put("ctime",item.getCtime());
                object.put("scheduleId",item.getScheduleId());
                data.add(object);
            }
            GlobalResult result = GlobalResult.build(200, "获取成功", data);
            return result;

        }else {
            GlobalResult result = GlobalResult.build(200, "没有预约信息", null);
            return result;

        }
    }
    @ApiOperation(value = "更新患者信息")
    @PostMapping("/update_appointment")
    @ResponseBody
    public GlobalResult updateAppointment(@RequestParam(value = "id", required = false) String appointmentid,
                                          @RequestParam(value = "status", required = false) String status){

        PatientAppointment appointment = this.patientAppointmentMapper.selectById(appointmentid);
        if(appointment!=null){
            appointment.setStatus(status);
            int a = patientAppointmentMapper.updateById(appointment);
            if (a == 1) {
                GlobalResult result = GlobalResult.build(200, "修改状态成功", null);
                return result;
            } else {
                GlobalResult result = GlobalResult.build(400, "修改失败", null);
                return result;
            }
        }else {
            GlobalResult result = GlobalResult.build(400, "出错啦", null);
            return result;
        }
    }
}
