package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Specialist;
import com.snowyyy.picc.mapper.SpecialistMapper;
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
@RequestMapping("/specialists")
@Api(tags = "管理中心外院专家接口")
public class AdminSpecialistController {

    @Autowired
    private SpecialistMapper specialistMapper;

    @ApiOperation(value = "获取护士列表")
    @GetMapping("/get_specialists")
    @ResponseBody
    public GlobalResult get_specialists(@RequestParam(value = "query", required = false) String query,
                                   @RequestParam(value = "pagenum", required = false) String num,
                                   @RequestParam(value = "type", required = false) String type) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<Specialist> nurses = this.specialistMapper.selectList(new QueryWrapper<Specialist>().like("name", query));
        if (nurses == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            if ("1".equals(type)) {
                for (Specialist nurse : nurses) {
                    if("1".equals(nurse.getState())) {
                        JSONObject item = new JSONObject();
                        item.put("id", nurse.getId());
                        item.put("username", nurse.getName());
                        item.put("specialistNumber", nurse.getSpecialistNumber());
                        item.put("sex", nurse.getSex());
                        item.put("phone", nurse.getPhone());
                        item.put("workUnit", nurse.getWorkUnit());
                        item.put("create_time", nurse.getCreateTime());
                        item.put("state", "已通过");
                        array.add(item);
                    }
                }
            } else {
                for (Specialist nurse : nurses) {
                    if("-1".equals(nurse.getState())) {
                        JSONObject item = new JSONObject();
                        item.put("id", nurse.getId());
                        item.put("username", nurse.getName());
                        item.put("specialistNumber", nurse.getSpecialistNumber());
                        item.put("sex", nurse.getSex());
                        item.put("phone", nurse.getPhone());
                        item.put("workUnit", nurse.getWorkUnit());
                        item.put("create_time", nurse.getCreateTime());
                        item.put("state", "未通过");
                        item.put("status", nurse.getState());
                        array.add(item);
                    } else if("0".equals(nurse.getState())) {
                        JSONObject item = new JSONObject();
                        item.put("id", nurse.getId());
                        item.put("username", nurse.getName());
                        item.put("specialistNumber", nurse.getSpecialistNumber());
                        item.put("sex", nurse.getSex());
                        item.put("phone", nurse.getPhone());
                        item.put("workUnit", nurse.getWorkUnit());
                        item.put("create_time", nurse.getCreateTime());
                        item.put("state", "审核中");
                        item.put("status", nurse.getState());
                        array.add(item);
                    }
                }
            }
        }
        String message = "获取护士列表成功";
        data.put("total", array.size());
        data.put("pagenum", Integer.parseInt(num));
        data.put("specialists", array);
        GlobalResult result = GlobalResult.build(200, message, data);
        return result;
    }

    @ApiOperation(value = "根据id获取护士信息")
    @GetMapping("/get_specialistById/{id}")
    @ResponseBody
    public GlobalResult get_specialistById(@PathVariable(value = "id") String id) {
        String message;
        Specialist nurse = this.specialistMapper.selectById(id);
        if (nurse != null) {
            JSONObject data = new JSONObject();
            data.put("id", nurse.getId());
            data.put("username", nurse.getName());
            data.put("specialistNumber", nurse.getSpecialistNumber());
            data.put("sex", nurse.getSex());
            data.put("phone", nurse.getPhone());
            data.put("workUnit", nurse.getWorkUnit());
            data.put("create_time", nurse.getCreateTime());
            data.put("status", nurse.getState());
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        } else {
            message = "获取失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }


    @ApiOperation(value = "修改护士信息")
    @PutMapping("/edit_specialist/{id}")
    @ResponseBody
    public GlobalResult edit_specialist(@PathVariable(value = "id", required = false) String id,
                                   @RequestBody Map<String, Object> reqMap) {
        String message;
        Specialist nurse = this.specialistMapper.selectById(id);
        nurse.setName(reqMap.get("username").toString());
        nurse.setSpecialistNumber(reqMap.get("specialistNumber").toString());
        nurse.setSex(reqMap.get("sex").toString());
        nurse.setPhone(reqMap.get("phone").toString());
        nurse.setWorkUnit(reqMap.get("workUnit").toString());
        int a = specialistMapper.updateById(nurse);
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

    @ApiOperation(value = "删除护士")
    @DeleteMapping("/delete_specialist/{id}")
    @ResponseBody
    public GlobalResult delete_specialist(@PathVariable(value = "id") String id) {
        String message;
        int a = specialistMapper.deleteById(id);
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

    @ApiOperation(value = "审核护士")
    @PutMapping("/change_specialistState/{id}/{state}")
    @ResponseBody
    public GlobalResult change_specialistState(@PathVariable(value = "id") String id,
                                          @PathVariable(value = "state") String state) {
        String message;
        Specialist nurse = new Specialist();
        nurse.setState(state);
        nurse.setId(id);
        int a = specialistMapper.updateById(nurse);
        if (a == 1) {
            message = "用户状态修改成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            message = "用户状态修改失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

}
