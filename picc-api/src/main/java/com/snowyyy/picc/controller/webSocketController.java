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

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/consult")
@Api(tags = "会诊咨询接口")
public class webSocketController {
    @Autowired
    private NursePatientBindingMapper bindingMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private NurseMapper nurseMapper;
    @Autowired
    private SpecialistMapper specialistMapper;
    @Autowired
    private WxUserMapper userMapper;

    @ApiOperation(value = "根据护士工号获取患者信息带头像")
    @GetMapping("/get_patientByJobNumber")
    @ResponseBody
    public GlobalResult get_patientByJobNumber(@RequestParam(value = "jobnumber") String jobnumber) {
        String message;
        List<NursePatientBinding> binding = this.bindingMapper.selectList(new QueryWrapper<NursePatientBinding>().eq("job_number",jobnumber));
        if (binding != null) {
            JSONArray data = new JSONArray();
            for(NursePatientBinding item:binding) {
                Patient patient = patientMapper.selectOne(new QueryWrapper<Patient>().eq("patient_num",item.getPatientNumber()));
                WxUser user = userMapper.selectById(patient.getUserId());
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                object.put("jobnumber", item.getJobNumber());
                object.put("nursename", item.getNurseName());
                object.put("patientnumber", item.getPatientNumber());
                object.put("patientname", item.getPatientName());
                object.put("create_time", item.getCreateTime());
                object.put("avatar_url", user.getAvatarUrl());
                object.put("sex", patient.getSex());
                data.add(object);
            }
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        } else {
            message = "未查询到信息";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        }
    }
    @ApiOperation(value = "根据编号获取护士信息带头像")
    @GetMapping("/get_nurseByPatientNumber")
    @ResponseBody
    public GlobalResult get_nurseByPatientNumber(@RequestParam(value = "patientnum") String patientnum) {
        String message;
        NursePatientBinding binding = this.bindingMapper.selectOne(new QueryWrapper<NursePatientBinding>().eq("patient_num",patientnum));
        if (binding != null) {
            Nurse nurse = nurseMapper.selectOne(new QueryWrapper<Nurse>().eq("job_number",binding.getJobNumber()));
            WxUser user = userMapper.selectById(nurse.getUserId());
            JSONObject data = new JSONObject();
            data.put("id", binding.getId());
            data.put("jobnumber", binding.getJobNumber());
            data.put("nursename", binding.getNurseName());
            data.put("patientnumber", binding.getPatientNumber());
            data.put("patientname", binding.getPatientName());
            data.put("create_time", binding.getCreateTime());
            data.put("avatar_url", user.getAvatarUrl());
            data.put("sex", nurse.getSex());
            data.put("department", nurse.getDepartment());

            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        } else {
            message = "未查询到信息";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        }
    }
    @ApiOperation(value = "根据编号获取所有护士信息(除了绑定)带头像")
    @GetMapping("/get_nurseExceptBinding")
    @ResponseBody
    public GlobalResult get_nurseExceptBinding(@RequestParam(value = "patientnum") String patientnum) {
        String message;
        NursePatientBinding binding = this.bindingMapper.selectOne(new QueryWrapper<NursePatientBinding>().eq("patient_num",patientnum));
        if (binding != null) {
            JSONArray data = new JSONArray();
            List<Nurse> nurse = nurseMapper.selectList(new QueryWrapper<Nurse>().ne("job_number",binding.getJobNumber()).eq("state","1"));
            for(Nurse item : nurse){
                WxUser user = userMapper.selectById(item.getUserId());
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                object.put("jobnumber", item.getJobNumber());
                object.put("nursename", item.getName());
                object.put("create_time", item.getCreateTime());
                object.put("avatar_url", user.getAvatarUrl());
                object.put("sex", item.getSex());
                object.put("department", item.getDepartment());
                data.add(object);
            }
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        } else {
            JSONArray data = new JSONArray();
            List<Nurse> nurse = nurseMapper.selectList(new QueryWrapper<Nurse>().eq("state","1"));
            for(Nurse item : nurse){
                WxUser user = userMapper.selectById(item.getUserId());
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                object.put("jobnumber", item.getJobNumber());
                object.put("nursename", item.getName());
                object.put("create_time", item.getCreateTime());
                object.put("avatar_url", user.getAvatarUrl());
                object.put("sex", item.getSex());
                object.put("department", item.getDepartment());
                data.add(object);
            }
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }
    @ApiOperation(value = "获取所有护士信息带头像")
    @GetMapping("/get_AllNurse")
    @ResponseBody
    public GlobalResult get_AllNurse() {
        String message;
            JSONArray data = new JSONArray();
            List<Nurse> nurse = nurseMapper.selectList(new QueryWrapper<Nurse>().eq("state","1"));
            for(Nurse item : nurse){
                WxUser user = userMapper.selectById(item.getUserId());
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                object.put("jobnumber", item.getJobNumber());
                object.put("nursename", item.getName());
                object.put("create_time", item.getCreateTime());
                object.put("avatar_url", user.getAvatarUrl());
                object.put("sex", item.getSex());
                object.put("department", item.getDepartment());
                data.add(object);
            }
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;

    }
    @ApiOperation(value = "获取所有专家信息带头像")
    @GetMapping("/get_AllSpecialist")
    @ResponseBody
    public GlobalResult get_AllSpecialist() {
        String message;
        JSONArray data = new JSONArray();
        List<Specialist> nurse = specialistMapper.selectList(new QueryWrapper<Specialist>().eq("state","1"));
        for(Specialist item : nurse){
            WxUser user = userMapper.selectById(item.getUserId());
            JSONObject object = new JSONObject();
            object.put("id", item.getId());
            object.put("specialistNumber", item.getSpecialistNumber());
            object.put("specialistName", item.getName());
            object.put("create_time", item.getCreateTime());
            object.put("avatar_url", user.getAvatarUrl());
            object.put("sex", item.getSex());
            object.put("workUnit", item.getWorkUnit());
            data.add(object);
        }
        message = "获取成功";
        GlobalResult result = GlobalResult.build(200, message, data);
        return result;

    }
}
