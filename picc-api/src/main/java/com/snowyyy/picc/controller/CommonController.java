package com.snowyyy.picc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Department;
import com.snowyyy.picc.entity.Nurse;
import com.snowyyy.picc.entity.Opinion;
import com.snowyyy.picc.mapper.*;
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
@RequestMapping("/common")
@Api(tags = "公共接口")
public class CommonController {

    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private WxUserMapper userMapper;
    @Autowired
    private NurseMapper nurseMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private OpinionMapper opinionMapper;

    @ApiOperation(value = "获取部门信息")
    @GetMapping("/get_department")
    @ResponseBody
    public GlobalResult getDepartment(){
        JSONArray array = new JSONArray();
        List<Department> department = this.departmentMapper.selectList(new QueryWrapper<Department>());
        for (Department item : department) {
            array.add(item.getDepartment());
        }
        GlobalResult result = GlobalResult.build(200, "ok", array);
        return result;
    }

    @ApiOperation(value = "根据部门获取护士信息")
    @GetMapping("/get_nurse_By_department")
    @ResponseBody
    public GlobalResult getNurseByDepartment(@RequestParam(value = "department", required = false) String department){
        List<Nurse> nurseList = this.nurseMapper.selectList(new QueryWrapper<Nurse>().eq("department",department));
        JSONArray array = new JSONArray();
        String message;
        if(nurseList!=null){
            for(Nurse nurse : nurseList){
                if("1".equals(nurse.getState())) {
                    JSONObject object = new JSONObject();
                    object.put("nursename", nurse.getName());
                    object.put("jobnumber", nurse.getJobNumber());
                    array.add(object);
                }
            }
            message = "ok";
            GlobalResult result = GlobalResult.build(200, message, array);
            return result;

        }else{
            message = "该部门暂时没有护士信息";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }
}
