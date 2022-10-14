package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Nurse;
import com.snowyyy.picc.entity.NursePatientBinding;
import com.snowyyy.picc.entity.Patient;
import com.snowyyy.picc.mapper.NurseMapper;
import com.snowyyy.picc.mapper.NursePatientBindingMapper;
import com.snowyyy.picc.mapper.PatientMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/binding")
@Api(tags = "绑定信息接口")
public class AdminBindingController {

    @Autowired
    private NursePatientBindingMapper bindingMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private NurseMapper nurseMapper;

    @ApiOperation(value = "获取绑定信息列表")
    @GetMapping("/get_binding")
    @ResponseBody
    public GlobalResult binding(@RequestParam(value = "query", required = false) String query,
                                @RequestParam(value = "pagenum", required = false) String num,
                                @RequestParam(value = "pagesize", required = false) String size) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        QueryWrapper<NursePatientBinding> queryWrapper = new QueryWrapper<>();
        queryWrapper.like( "nurse_name", query).or().like("patient_name", query);
        List<NursePatientBinding> bindingList = this.bindingMapper.selectList(queryWrapper);
        if (bindingList == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            for (NursePatientBinding binding : bindingList) {
                JSONObject item = new JSONObject();
                item.put("id", binding.getId());
                item.put("jobnumber", binding.getJobNumber());
                item.put("nursename", binding.getNurseName());
                item.put("patientnumber", binding.getPatientNumber());
                item.put("patientname", binding.getPatientName());
                item.put("create_time", binding.getCreateTime());
                array.add(item);
            }
            String message = "获取用户绑定信息列表成功";
            data.put("total", bindingList.size());
            data.put("pagenum", Integer.parseInt(num));
            data.put("bindings", array);
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }

    @ApiOperation(value = "根据id获取绑定信息")
    @GetMapping("/get_bindingById/{id}")
    @ResponseBody
    public GlobalResult get_bindingById(@PathVariable(value = "id") String id) {
        String message;
        NursePatientBinding binding = this.bindingMapper.selectById(id);
        if (binding != null) {
            JSONObject data = new JSONObject();
            data.put("id", binding.getId());
            data.put("jobnumber", binding.getJobNumber());
            data.put("nursename", binding.getNurseName());
            data.put("patientnumber", binding.getPatientNumber());
            data.put("patientname", binding.getPatientName());
            data.put("create_time", binding.getCreateTime());
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        } else {
            message = "获取失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }
    @ApiOperation(value = "根据护士工号获取绑定信息")
    @GetMapping("/get_bindingByJobNumber")
    @ResponseBody
    public GlobalResult get_bindingByJobNumber(@RequestParam(value = "jobnumber") String jobnumber) {
        String message;
        List<NursePatientBinding> binding = this.bindingMapper.selectList(new QueryWrapper<NursePatientBinding>().eq("job_number",jobnumber));
        if (binding != null) {
            JSONArray data = new JSONArray();
            for(NursePatientBinding item:binding) {
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                object.put("jobnumber", item.getJobNumber());
                object.put("nursename", item.getNurseName());
                object.put("patientnumber", item.getPatientNumber());
                object.put("patientname", item.getPatientName());
                object.put("create_time", item.getCreateTime());
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
    @ApiOperation(value = "编辑绑定信息")
    @PutMapping("/edit_binding/{id}")
    @ResponseBody
    public GlobalResult edit_binding(@PathVariable(value = "id", required = false) String id,
                                     @RequestBody Map<String, Object> reqMap) {
        String message;
        NursePatientBinding binding = this.bindingMapper.selectById(id);
        binding.setJobNumber(reqMap.get("jobnumber").toString());
        binding.setNurseName(reqMap.get("nursename").toString());
        binding.setPatientNumber(reqMap.get("patientnumber").toString());
        binding.setPatientName(reqMap.get("patientname").toString());
        int a = bindingMapper.updateById(binding);
        if (a == 1) {
            message = "用户绑定信息修改成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            message = "用户绑定信息修改失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

    @ApiOperation(value = "删除绑定信息")
    @DeleteMapping("/delete_binding/{id}")
    @ResponseBody
    public GlobalResult delete_binding(@PathVariable(value = "id") String id) {
        String message;
        int a = bindingMapper.deleteById(id);
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

    @ApiOperation(value = "上传绑定信息")
    @PostMapping("/create_binding")
    @ResponseBody
    public GlobalResult create_binding(@RequestParam(value = "patientnum") String patientnum,
                                       @RequestParam(value = "jobnumber") String jobnumber) {
        String message;
        Date date = new Date();
        Patient patient=patientMapper.selectOne(new QueryWrapper<Patient>().eq("patient_num",patientnum));
        Nurse nurse=nurseMapper.selectOne(new QueryWrapper<Nurse>().eq("job_number",jobnumber));
        NursePatientBinding binding = new NursePatientBinding();
        binding.setJobNumber(nurse.getJobNumber());
        binding.setNurseName(nurse.getName());
        binding.setPatientNumber(patient.getPatientNumber());
        binding.setPatientName(patient.getName());
        binding.setCreateTime(String.valueOf(date.getTime()));
        int a = bindingMapper.insert(binding);
        if (a == 1) {
            message = "绑定成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            message = "绑定失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }
    @ApiOperation(value = "根据患者编号获取患者信息(护士端绑定操作)")
    @GetMapping("/get_patientByPatientNumber")
    @ResponseBody
    public GlobalResult get_patientByPatientNumber(@RequestParam(value = "patientnumber") String number) {
        String message;
        NursePatientBinding binding = this.bindingMapper.selectOne(new QueryWrapper<NursePatientBinding>().eq("patient_num",number));
        if(binding==null){
            Patient patient = this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("patient_num",number));
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
                message = "未查询到该患者信息";
                GlobalResult result = GlobalResult.build(400, message, null);
                return result;
            }
        }else{
            message = "该患者已有绑定";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;

        }
    }

}
