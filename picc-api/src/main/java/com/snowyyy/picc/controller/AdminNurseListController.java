package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Nurse;
import com.snowyyy.picc.entity.NurseSchedule;
import com.snowyyy.picc.mapper.NurseMapper;
import com.snowyyy.picc.mapper.NurseScheduleMapper;
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
@RequestMapping("/nurses")
@Api(tags = "管理中心护士接口")
public class AdminNurseListController {

    @Autowired
    private NurseMapper nurseMapper;
    @Autowired
    private NurseScheduleMapper nurseScheduleMapper;

    @ApiOperation(value = "获取护士列表")
    @GetMapping("/get_nurses")
    @ResponseBody
    public GlobalResult get_nurses(@RequestParam(value = "query", required = false) String query,
                                     @RequestParam(value = "pagenum", required = false) String num,
                                     @RequestParam(value = "type", required = false) String type) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<Nurse> nurses = this.nurseMapper.selectList(new QueryWrapper<Nurse>().like("name", query));
        if (nurses == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            if ("1".equals(type)) {
                for (Nurse nurse : nurses) {
                    if("1".equals(nurse.getState())) {
                        JSONObject item = new JSONObject();
                        item.put("id", nurse.getId());
                        item.put("username", nurse.getName());
                        item.put("jobnumber", nurse.getJobNumber());
                        item.put("sex", nurse.getSex());
                        item.put("phone", nurse.getPhone());
                        item.put("department", nurse.getDepartment());
                        item.put("create_time", nurse.getCreateTime());
                        item.put("state", "已通过");
                        array.add(item);
                    }
                }
            } else {
                for (Nurse nurse : nurses) {
                    if("-1".equals(nurse.getState())) {
                        JSONObject item = new JSONObject();
                        item.put("id", nurse.getId());
                        item.put("username", nurse.getName());
                        item.put("jobnumber", nurse.getJobNumber());
                        item.put("sex", nurse.getSex());
                        item.put("phone", nurse.getPhone());
                        item.put("department", nurse.getDepartment());
                        item.put("create_time", nurse.getCreateTime());
                        item.put("state", "未通过");
                        item.put("status", nurse.getState());
                        array.add(item);
                    } else if("0".equals(nurse.getState())) {
                        JSONObject item = new JSONObject();
                        item.put("id", nurse.getId());
                        item.put("username", nurse.getName());
                        item.put("jobnumber", nurse.getJobNumber());
                        item.put("sex", nurse.getSex());
                        item.put("phone", nurse.getPhone());
                        item.put("department", nurse.getDepartment());
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
        data.put("nurses", array);
        GlobalResult result = GlobalResult.build(200, message, data);
        return result;
    }

    @ApiOperation(value = "根据id获取护士信息")
    @GetMapping("/get_nurseById/{id}")
    @ResponseBody
    public GlobalResult get_nurseById(@PathVariable(value = "id") String id) {
        String message;
        Nurse nurse = this.nurseMapper.selectById(id);
        if (nurse != null) {
            JSONObject data = new JSONObject();
            data.put("id", nurse.getId());
            data.put("username", nurse.getName());
            data.put("jobnumber", nurse.getJobNumber());
            data.put("sex", nurse.getSex());
            data.put("phone", nurse.getPhone());
            data.put("department", nurse.getDepartment());
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
    @PutMapping("/edit_nurse/{id}")
    @ResponseBody
    public GlobalResult edit_nurse(@PathVariable(value = "id", required = false) String id,
                                     @RequestBody Map<String, Object> reqMap) {
        String message;
        Nurse nurse = this.nurseMapper.selectById(id);
        nurse.setName(reqMap.get("username").toString());
        nurse.setJobNumber(reqMap.get("jobnumber").toString());
        nurse.setSex(reqMap.get("sex").toString());
        nurse.setPhone(reqMap.get("phone").toString());
        nurse.setDepartment(reqMap.get("department").toString());
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
    }

    @ApiOperation(value = "删除护士")
    @DeleteMapping("/delete_nurse/{id}")
    @ResponseBody
    public GlobalResult delete_nurse(@PathVariable(value = "id") String id) {
        String message;
        int a = nurseMapper.deleteById(id);
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
    @PutMapping("/change_nurseState/{id}/{state}")
    @ResponseBody
    public GlobalResult change_nurseState(@PathVariable(value = "id") String id,
                                          @PathVariable(value = "state") String state) {
        String message;
        Nurse nurse = new Nurse();
        nurse.setState(state);
        nurse.setId(id);
        int a = nurseMapper.updateById(nurse);
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

    @ApiOperation(value = "根据工号获取护士排期信息")
    @GetMapping("/get_Schedule_by_jobnumber")
    @ResponseBody
    public GlobalResult get_Schedule_by_jobnumber(@RequestParam(value = "jobnumber", required = false) String jobnumber) {

        String message;
        JSONArray array = new JSONArray();
        List<NurseSchedule> nurseSchedule = this.nurseScheduleMapper.selectList(new QueryWrapper<NurseSchedule>().eq("job_number", jobnumber));
        if (nurseSchedule != null) {
            for (NurseSchedule item : nurseSchedule) {
                JSONObject data = new JSONObject();
                String date1 = String.valueOf(new Date().getTime());
                String date2 = item.getAppointmentDate();
                if (date2.compareTo(date1) > 0) {
                    data.put("id", item.getId());
                    data.put("nurseName", item.getNurseName());
                    data.put("jobNumber", item.getJobNumber());
                    data.put("appointmentDate", item.getAppointmentDate());
                    data.put("forenoon", Boolean.parseBoolean(item.getForenoon()));
                    data.put("forenoonNumber", Integer.parseInt(item.getForenoonNumber()));
                    data.put("forenoonLimit", Integer.parseInt(item.getForenoonLimit()));
                    data.put("afternoon", Boolean.parseBoolean(item.getAfternoon()));
                    data.put("afternoonNumber", Integer.parseInt(item.getAfternoonNumber()));
                    data.put("afternoonLimit", Integer.parseInt(item.getAfternoonLimit()));
                    array.add(data);
                }
            }
            GlobalResult result = GlobalResult.build(200, "ok", array);
            return result;

        } else {
            message = "该护士暂无排期";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

    @ApiOperation(value = "修改护士排期信息")
    @PostMapping("/post_ScheduleByjobnumber")
    @ResponseBody
    public GlobalResult post_ScheduleByjobnumber(@RequestParam(value = "jobnumber", required = false) String jobnumber,
                                      @RequestParam(value = "nursename", required = false) String nursename,
                                      @RequestParam(value = "schedule", required = false) String schedule) {
        JSONArray jsonArray = JSONArray.fromObject(schedule);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject object=jsonArray.getJSONObject(i);
            String appointmentDate=object.getString("appointmentDate");
            String forenoon=object.getString("forenoon");
            String forenoonLimit=object.getString("forenoonLimit");
            String afternoon = object.getString("afternoon");
            String afternoonLimit=object.getString("afternoonLimit");
            //其他操作
            NurseSchedule result = this.nurseScheduleMapper.selectOne(new QueryWrapper<NurseSchedule>().eq("job_number", jobnumber).eq("appointment_date", appointmentDate));
            if(result!=null){
                result.setForenoon(forenoon);
                result.setForenoonLimit(forenoonLimit);
                result.setAfternoon(afternoon);
                result.setAfternoonLimit(afternoonLimit);
                nurseScheduleMapper.updateById(result);
            }else{
                NurseSchedule item = new NurseSchedule();
                item.setNurseName(nursename);
                item.setJobNumber(jobnumber);
                item.setAppointmentDate(appointmentDate);
                item.setForenoon(forenoon);
                item.setForenoonLimit(forenoonLimit);
                item.setAfternoon(afternoon);
                item.setAfternoonLimit(afternoonLimit);
                nurseScheduleMapper.insert(item);
            }
        }
        GlobalResult result = GlobalResult.build(200, "ok", null);
        return result;
    }
    @ApiOperation(value = "获取护士排期信息")
    @GetMapping("/get_Schedule")
    @ResponseBody
    public GlobalResult get_Schedule(@RequestParam(value = "type", required = false) String id,
                                     @RequestParam(value = "date", required = false) String date) {
        String message;
        JSONArray array = new JSONArray();
        if(id.equals("1")){
            List<NurseSchedule> nurseSchedule = this.nurseScheduleMapper.selectList(new QueryWrapper<NurseSchedule>().eq("appointment_date", date).eq("forenoon", "true"));
            if (nurseSchedule != null) {
                for (NurseSchedule item : nurseSchedule) {
                    JSONObject object=new JSONObject();
                    object.put("jobnumber",item.getJobNumber());
                    object.put("nursename",item.getNurseName());
                    object.put("date",item.getAppointmentDate());
                    object.put("state",item.getForenoon());
                    object.put("limit",item.getForenoonLimit());
                    array.add(object);
                }
                GlobalResult result = GlobalResult.build(200, "ok", array);
                return result;

            } else {
                message = "暂无数据";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
            }
        }else if(id.equals("2")){
            List<NurseSchedule> nurseSchedule = this.nurseScheduleMapper.selectList(new QueryWrapper<NurseSchedule>().eq("appointment_date", date).eq("afternoon", "true"));
            if (nurseSchedule != null) {
                for (NurseSchedule item : nurseSchedule) {
                    JSONObject object=new JSONObject();
                    object.put("jobnumber",item.getJobNumber());
                    object.put("nursename",item.getNurseName());
                    object.put("date",item.getAppointmentDate());
                    object.put("state",item.getAfternoon());
                    object.put("limit",item.getAfternoonLimit());
                    array.add(object);
                }
                GlobalResult result = GlobalResult.build(200, "ok", array);
                return result;

            } else {
                message = "暂无数据";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
            }
        }else{
            GlobalResult result = GlobalResult.build(400, null, null);
            return result;
        }
    }

    @ApiOperation(value = "根据开始时间和结束时间获取护士排期信息")
    @GetMapping("/get_ScheduleByTime")
    @ResponseBody
    public GlobalResult get_ScheduleByTime(@RequestParam(value = "start", required = false) String start,
                                     @RequestParam(value = "end", required = false) String end) {
        String message;
        JSONArray array = new JSONArray();
        List<NurseSchedule> nurseSchedule = this.nurseScheduleMapper.selectList(new QueryWrapper<NurseSchedule>().ge("appointment_date", start).lt("appointment_date", end));
        if (nurseSchedule != null) {
            for (NurseSchedule item : nurseSchedule) {
                    JSONObject object=new JSONObject();
                    object.put("jobnumber",item.getJobNumber());
                    object.put("nursename",item.getNurseName());
                    object.put("date",item.getAppointmentDate());
                    object.put("afternoon",item.getAfternoon());
                    object.put("forenoon",item.getForenoon());
                    array.add(object);
            }
                GlobalResult result = GlobalResult.build(200, "ok", array);
                return result;

        } else {
                message = "暂无数据";
                GlobalResult result = GlobalResult.build(200, message, null);
                return result;
        }
    }
    @ApiOperation(value = "更新护士排期信息")
    @PostMapping("/post_Schedule")
    @ResponseBody
    public GlobalResult post_Schedule(@RequestParam(value = "type", required = false) String id,
                                     @RequestParam(value = "date", required = false) String date,
                                      @RequestParam(value = "addList", required = false) String array) {
        JSONArray jsonArray = JSONArray.fromObject(array);
        String message;
        if(id.equals("1")){
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject object=jsonArray.getJSONObject(i);
                NurseSchedule nurseSchedule = this.nurseScheduleMapper.selectOne(new QueryWrapper<NurseSchedule>().eq("appointment_date", date).eq("job_number",object.getString("jobnumber")));
                if (nurseSchedule != null) {
                    nurseSchedule.setForenoon(object.getString("state"));
                    nurseSchedule.setForenoonLimit(object.getString("limit"));
                    nurseScheduleMapper.updateById(nurseSchedule);
                } else {
                    NurseSchedule newSchedule=new NurseSchedule();
                    newSchedule.setJobNumber(object.getString("jobnumber"));
                    newSchedule.setNurseName(object.getString("nursename"));
                    newSchedule.setAppointmentDate(object.getString("date"));
                    newSchedule.setForenoon(object.getString("state"));
                    newSchedule.setForenoonNumber("0");
                    newSchedule.setForenoonLimit(object.getString("limit"));
                    nurseScheduleMapper.insert(newSchedule);
                }
            }
        }else if(id.equals("2")){
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject object=jsonArray.getJSONObject(i);
                NurseSchedule nurseSchedule = this.nurseScheduleMapper.selectOne(new QueryWrapper<NurseSchedule>().eq("appointment_date", date).eq("job_number",object.getString("jobnumber")));
                if (nurseSchedule != null) {
                    nurseSchedule.setAfternoon(object.getString("state"));
                    nurseSchedule.setAfternoonLimit(object.getString("limit"));
                    nurseScheduleMapper.updateById(nurseSchedule);
                } else {
                    NurseSchedule newSchedule=new NurseSchedule();
                    newSchedule.setJobNumber(object.getString("jobnumber"));
                    newSchedule.setNurseName(object.getString("nursename"));
                    newSchedule.setAppointmentDate(object.getString("date"));
                    newSchedule.setAfternoon(object.getString("state"));
                    newSchedule.setAfternoonNumber("0");
                    newSchedule.setAfternoonLimit(object.getString("limit"));
                    nurseScheduleMapper.insert(newSchedule);
                }
            }
        }
        GlobalResult result = GlobalResult.build(200, null, null);
        return result;

    }

    @ApiOperation(value = "删除护士排期信息一个")
    @PostMapping("/delete_ScheduleOne")
    @ResponseBody
    public GlobalResult delete_ScheduleOne(@RequestParam(value = "type", required = false) String id,
                                      @RequestParam(value = "date", required = false) String date,
                                      @RequestParam(value = "jobnumber", required = false) String jobnumber) {
        String message;
        if(id.equals("1")){
            NurseSchedule nurseSchedule = this.nurseScheduleMapper.selectOne(new QueryWrapper<NurseSchedule>().eq("appointment_date", date).eq("job_number",jobnumber));
                if (nurseSchedule != null) {
                    nurseSchedule.setForenoon("false");
                    nurseSchedule.setForenoonLimit("6");
                    nurseScheduleMapper.updateById(nurseSchedule);

            }
        }else if(id.equals("2")){
            NurseSchedule nurseSchedule = this.nurseScheduleMapper.selectOne(new QueryWrapper<NurseSchedule>().eq("appointment_date", date).eq("job_number",jobnumber));
            if (nurseSchedule != null) {
                nurseSchedule.setAfternoon("false");
                nurseSchedule.setAfternoonLimit("6");
                nurseScheduleMapper.updateById(nurseSchedule);

            }
        }
        GlobalResult result = GlobalResult.build(200, null, null);
        return result;

    }
}
