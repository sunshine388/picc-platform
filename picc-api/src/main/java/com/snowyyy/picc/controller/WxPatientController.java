package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.*;
import com.snowyyy.picc.mapper.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@CrossOrigin
@Controller
@RequestMapping("/patient")
@Api(tags = "微信患者接口")
public class WxPatientController {
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private WxUserMapper userMapper;
    @Autowired
    private PatientAppointmentMapper patientAppointmentMapper;
    @Autowired
    private NurseScheduleMapper nurseScheduleMapper;
    @Autowired
    private PatientInformationMapper patientInformationMapper;

    @ApiOperation(value = "患者信息提交")
    @PostMapping("/create")
    @ResponseBody
    public GlobalResult patient(@RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "sex", required = false) String sex,
                                @RequestParam(value = "skey", required = false) String skey,
                                @RequestParam(value = "culture", required = false) String culture,
                                @RequestParam(value = "address", required = false) String address,
                                @RequestParam(value = "city", required = false) String city,
                                @RequestParam(value = "birthday", required = false) String birthday){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        String year1 = new SimpleDateFormat("yy", Locale.CHINESE).format(new Date());
        String month1=month<10?"0"+month:String.valueOf(month);
        String day1=day<10?"0"+day:String.valueOf(day);
        //获取今天0点时间
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
        c1.set(Calendar.MINUTE, 0); //将分钟至0
        c1.set(Calendar.SECOND,0); //将秒至0
        c1.set(Calendar.MILLISECOND, 0); //将毫秒至0
        //获取今天23:59点时间
        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
        c2.set(Calendar.MINUTE, 59); //将分钟至59
        c2.set(Calendar.SECOND,59); //将秒至59
        c2.set(Calendar.MILLISECOND, 999); //将毫秒至999
        int count=this.patientMapper.selectCount(new QueryWrapper<Patient>().between("create_time", c1.getTimeInMillis(),c2.getTimeInMillis()));
        String count1=count<10?"0"+count:String.valueOf(count);
        String patientnumber="HZ"+year1+""+month1+""+day1+""+count1;
        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        String message;
        Patient patient=new Patient();
        Patient useritem=this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("user_id", user.getOpenId()));
        if(useritem==null){
            patient.setName(username);
            patient.setUserId(user.getOpenId());
            patient.setPatientNumber(patientnumber);
            patient.setSex(sex);
            patient.setPhone(phone);
            patient.setBirthday(birthday);
            patient.setCity(city);
            patient.setCulture(culture);
            patient.setAddress(address);
            patient.setCreateTime(String.valueOf(date.getTime()));
            int a = patientMapper.insert(patient);
            if (a == 1) {
                message = "用户信息提交成功";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
            } else {
                message = "用户信息提交失败";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }else {
            GlobalResult result = GlobalResult.build(200, "该用户信息已存在", null);
            return result;
        }

    }

    @ApiOperation(value = "患者信息更新")
    @PostMapping("/update")
    @ResponseBody
    public GlobalResult patientUpdate(@RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "phone", required = false) String phone,
                                @RequestParam(value = "sex", required = false) String sex,
                                @RequestParam(value = "skey", required = false) String skey,
                                @RequestParam(value = "culture", required = false) String culture,
                                @RequestParam(value = "city", required = false) String city,
                                      @RequestParam(value = "address", required = false) String address,
                                @RequestParam(value = "birthday", required = false) String birthday){

        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        String patientnumber=phone;
        String message;
        Patient useritem=this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("user_id", user.getOpenId()));
        if(useritem!=null){
            Patient patient = this.patientMapper.selectById(useritem.getId());
            patient.setName(username);
            patient.setPatientNumber(patientnumber);
            patient.setSex(sex);
            patient.setPhone(phone);
            patient.setBirthday(birthday);
            patient.setCity(city);
            patient.setAddress(address);
            patient.setCulture(culture);
            int a = patientMapper.updateById(patient);
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
    @ApiOperation(value = "获取下次预约时间")
    @GetMapping("/get_nextMaintainTime")
    @ResponseBody
    public GlobalResult get_nextMaintainTime(@RequestParam(value = "patientnum", required = false) String patientnum){
        PatientInformation patientInformation =this.patientInformationMapper.selectOne(new QueryWrapper<PatientInformation>().eq("patient_number",patientnum));
        if(patientInformation !=null){
            JSONObject data=new JSONObject();
            data.put("date", patientInformation.getNextTime());
            data.put("hasCatheter", patientInformation.getHasCatheter());
            data.put("recordnum", patientInformation.getRecordNum());
            GlobalResult result = GlobalResult.build(200, "获取成功", data);
            return result;
        }else{
            GlobalResult result = GlobalResult.build(200, "该用户尚未置管", null);
            return result;
        }
    }
    @ApiOperation(value = "患者预约信息提交")
    @PostMapping("/create_appointment")
    @ResponseBody
    public GlobalResult create_appointment(@RequestParam(value = "patientnum", required = false) String patientnum,
                                           @RequestParam(value = "patientname", required = false) String patientname,
                                           @RequestParam(value = "nursename", required = false) String nursename,
                                           @RequestParam(value = "jobnumber", required = false) String jobnumber,
                                           @RequestParam(value = "date", required = false) String date,
                                           @RequestParam(value = "time", required = false) String time,
                                           @RequestParam(value = "type", required = false) String type,
                                           @RequestParam(value = "scheduleId", required = false) String scheduleId){

        PatientAppointment app=patientAppointmentMapper.selectOne(new QueryWrapper<PatientAppointment>().eq("patient_num",patientnum).eq("appointment_date",date).eq("appointment_time",time));
        if(app==null) {
            PatientAppointment appointment = new PatientAppointment();
            String message;
            appointment.setPatientNumber(patientnum);
            appointment.setPatientName(patientname);
            appointment.setNurseName(nursename);
            appointment.setJobNumber(jobnumber);
            appointment.setAppointmentDate(date);
            appointment.setAppointmentTime(time);
            appointment.setType(type);
            appointment.setStatus("0");
            appointment.setScheduleId(scheduleId);
            appointment.setCtime(String.valueOf(new Date().getTime()));
            int a = patientAppointmentMapper.insert(appointment);
            NurseSchedule schedule = nurseScheduleMapper.selectById(scheduleId);
            if (time.equals("上午")) {
                String num1 = String.valueOf(Integer.parseInt(schedule.getForenoonNumber()) + 1);
                schedule.setForenoonNumber(num1);
                int b1 = this.nurseScheduleMapper.updateById(schedule);

                if (a == 1 && b1 == 1) {
                    message = "提交成功";
                    GlobalResult result = GlobalResult.build(200, message, null);
                    return result;
                } else {
                    message = "提交失败";
                    GlobalResult result = GlobalResult.build(400, message, null);
                    return result;
                }
            } else if (time.equals("下午")) {
                String num2 = String.valueOf(Integer.parseInt(schedule.getAfternoonNumber()) + 1);
                schedule.setAfternoonNumber(num2);
                int b2 = this.nurseScheduleMapper.updateById(schedule);
                if (a == 1 && b2 == 1) {
                    message = "提交成功";
                    GlobalResult result = GlobalResult.build(200, message, null);
                    return result;
                } else {
                    message = "提交失败";
                    GlobalResult result = GlobalResult.build(400, message, null);
                    return result;
                }
            } else {
                message = "出错啦";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }else{
            GlobalResult result = GlobalResult.build(200, "已有预约", "false");
            return result;
        }
    }
    @ApiOperation(value = "根据患者编号获取预约信息")
    @GetMapping("/get_appointmentByPatientnumber")
    @ResponseBody
    public GlobalResult getAppointmentByJobnumber(@RequestParam(value = "patientnum", required = false) String patientnum){
        List<PatientAppointment> appointment = this.patientAppointmentMapper.selectList(new QueryWrapper<PatientAppointment>().eq("patient_num", patientnum));
        JSONArray data=new JSONArray();
        if(appointment!=null){
            for(PatientAppointment item:appointment){
                JSONObject object=new JSONObject();
                object.put("id",item.getId());
                object.put("nurseName",item.getNurseName());
                object.put("jobNumber",item.getJobNumber());
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

    @ApiOperation(value = "根据id取消预约信息")
    @PostMapping("/cancel_appointmentById")
    @ResponseBody
    public GlobalResult cancelAppointmentById(@RequestParam(value = "id", required = false) String id){
        PatientAppointment appointment = this.patientAppointmentMapper.selectOne(new QueryWrapper<PatientAppointment>().eq("id", id));
        if(appointment!=null){
            NurseSchedule schedule=nurseScheduleMapper.selectById(appointment.getScheduleId());
            appointment.setStatus("-1");
            int a=patientAppointmentMapper.updateById(appointment);
            String num=String.valueOf(Integer.parseInt(schedule.getForenoonNumber())-1);
            schedule.setAfternoonNumber(num);
            int b= nurseScheduleMapper.updateById(schedule);
            if(a==1&&b==1) {
                GlobalResult result = GlobalResult.build(200, "成功", "");
                return result;
            }else {
                GlobalResult result = GlobalResult.build(400, "出错啦", null);
                return result;
            }
        }else {
            GlobalResult result = GlobalResult.build(400, "出错啦", null);
            return result;
        }
    }
    @ApiOperation(value = "根据id删除预约记录")
    @DeleteMapping("/delete_appointmentById")
    @ResponseBody
    public GlobalResult deleteAppointmentById(@RequestParam(value = "id", required = false) String id){
        int a= patientAppointmentMapper.deleteById(id);
        if(a==1) {
             GlobalResult result = GlobalResult.build(200, "删除成功", "");
             return result;
        }else {
            GlobalResult result = GlobalResult.build(400, "删除失败", null);
            return result;
        }
    }
}
