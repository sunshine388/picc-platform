package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Patient;
import com.snowyyy.picc.mapper.PatientMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/patients")
@Api(tags = "管理中心患者接口")
public class AdminPatientListController {

    @Autowired
    private PatientMapper patientMapper;

    @ApiOperation(value = "获取患者信息列表")
    @GetMapping("/get_patients")
    @ResponseBody
    public GlobalResult patient(@RequestParam(value = "query", required = false) String query,
                                   @RequestParam(value = "pagenum", required = false) String num,
                                   @RequestParam(value = "pagesize", required = false) String size) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<Patient> patients = this.patientMapper.selectList(new QueryWrapper<Patient>().like("name", query));
        if (patients == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            for (Patient patient : patients) {
                JSONObject item = new JSONObject();
                item.put("id", patient.getId());
                item.put("patientnumber", patient.getPatientNumber());
                item.put("username", patient.getName());
                item.put("sex", patient.getSex());
                item.put("phone", patient.getPhone());
                item.put("birthday", patient.getBirthday());
                item.put("city", patient.getCity());
                item.put("address", patient.getAddress());
                item.put("culture", patient.getCulture());
                item.put("create_time", patient.getCreateTime());
                array.add(item);
            }
            String message = "获取病人列表成功";
            data.put("total", patients.size());
            data.put("pagenum", Integer.parseInt(num));
            data.put("patients", array);
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }

    @ApiOperation(value = "根据id获取患者信息")
    @GetMapping("/get_patientById/{id}")
    @ResponseBody
    public GlobalResult get_patientById(@PathVariable(value = "id") String id) {
        String message;
        Patient patient = this.patientMapper.selectById(id);
        if (patient != null) {
            JSONObject data = new JSONObject();
            data.put("id", patient.getId());
            data.put("patientnumber", patient.getPatientNumber());
            data.put("username", patient.getName());
            data.put("sex", patient.getSex());
            data.put("phone", patient.getPhone());
            data.put("birthday", patient.getBirthday());
            data.put("city", patient.getCity());
            data.put("address", patient.getAddress());
            data.put("culture", patient.getCulture());
            data.put("create_time", patient.getCreateTime());
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        } else {
            message = "获取失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }
    @ApiOperation(value = "修改患者信息")
    @PutMapping("/edit_patient/{id}")
    @ResponseBody
    public GlobalResult edit_patient(@PathVariable(value = "id", required = false) String id,
                                   @RequestBody Map<String, Object> reqMap) {
        String message;
        Patient patient = this.patientMapper.selectById(id);
        patient.setName(reqMap.get("username").toString());
        patient.setSex(reqMap.get("sex").toString());
        patient.setPhone(reqMap.get("phone").toString());
        patient.setBirthday(reqMap.get("birthday").toString());
        patient.setCity(reqMap.get("city").toString());
        patient.setCity(reqMap.get("address").toString());
        patient.setCulture(reqMap.get("culture").toString());
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
    }

    @ApiOperation(value = "删除患者")
    @DeleteMapping("/delete_patient/{id}")
    @ResponseBody
    public GlobalResult delete_patient(@PathVariable(value = "id") String id) {
        String message;
        int a = patientMapper.deleteById(id);
        if (a == 1) {
            message = "删除成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            message = "删除失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }
}
