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

import java.util.Date;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/opinion")
@Api(tags = "意见接口")
public class OpinionController {
    @Autowired
    private OpinionMapper opinionMapper;
    @Autowired
    private WxUserMapper userMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private NurseMapper nurseMapper;
    @Autowired
    private SpecialistMapper specialistMapper;

    @ApiOperation(value = "获取所有意见")
    @GetMapping("/get_opinion")
    @ResponseBody
    public GlobalResult getOpinion(){
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<Opinion> opinion = this.opinionMapper.selectList(new QueryWrapper<Opinion>());
        if(opinion!=null){
            for(Opinion item:opinion){
                JSONObject object=new JSONObject();
                String sex="";
                String phone="";
                if(item.getRole().equals("患者")){
                    Patient patient=this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("user_id",item.getUserId()));
                    if(patient!=null) {
                        sex = patient.getSex();
                        phone = patient.getPhone();
                    }
                }else if(item.getRole().equals("本院职工")){
                    Nurse nurse=this.nurseMapper.selectOne(new QueryWrapper<Nurse>().eq("user_id",item.getUserId()));
                    if(nurse!=null) {
                        sex = nurse.getSex();
                        phone = nurse.getPhone();
                    }
                }else if(item.getRole().equals("外院专家")){
                    Specialist specialist=this.specialistMapper.selectOne(new QueryWrapper<Specialist>().eq("user_id",item.getUserId()));
                    if(specialist!=null) {
                        sex = specialist.getSex();
                        phone = specialist.getPhone();
                    }
                }
                object.put("id",item.getId());
                object.put("sex",sex);
                object.put("phone",phone);
                object.put("username",item.getUsername());
                object.put("role",item.getRole());
                object.put("problem",item.getProblem());
                object.put("ctime",item.getCtime());
                object.put("whether_to_solve",item.getWhetherToSolve());
                object.put("solution",item.getSolution());
                array.add(object);
            }
            data.put("total", opinion.size());
            data.put("list", array);
            GlobalResult result = GlobalResult.build(200, "ok", data);
            return result;
        }else {
            GlobalResult result = GlobalResult.build(200, "暂无意见", null);
            return result;
        }
    }
    @ApiOperation(value = "意见解决信息")
    @PutMapping("/update_opinion/{id}/{solution}")
    @ResponseBody
    public GlobalResult updateOpinion(@PathVariable(value = "id", required = false) String id,
                                      @PathVariable(value = "solution", required = false) String solution){
        String message;
        Opinion opinion = this.opinionMapper.selectById(id);
        if(opinion!=null){
            opinion.setSolution(solution);
            opinion.setWhetherToSolve("已回复");
            int a = opinionMapper.updateById(opinion);
            message="提交成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else{
            message="提交失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

    @ApiOperation(value = "意见解决")
    @PutMapping("/change_opinionState/{id}/{whether_to_solve}")
    @ResponseBody
    public GlobalResult changeOpinionState(@PathVariable(value = "id", required = false) String id,
                                      @PathVariable(value = "whether_to_solve", required = false) String whether_to_solve){
        String message;
        Opinion opinion = this.opinionMapper.selectById(id);
        if(opinion!=null){
            opinion.setSolution(whether_to_solve);
            int a = opinionMapper.updateById(opinion);
            message="修改成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else{
            message="修改失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }
    @ApiOperation(value = "根据skey获取用户意见信息")
    @GetMapping("/get_opinionBySkey")
    @ResponseBody
    public GlobalResult getOpinionByUserId(@RequestParam(value = "skey", required = false) String skey){
        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        List<Opinion> opinion = this.opinionMapper.selectList(new QueryWrapper<Opinion>().eq("user_id",user.getOpenId()));
        String message;
        JSONArray array = new JSONArray();
        if (opinion == null) {
            message = "该用户没有提交意见";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            for(Opinion item:opinion){
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                object.put("role", item.getRole());
                object.put("username", item.getUsername());
                object.put("problem", item.getProblem());
                object.put("ctime", item.getCtime());
                object.put("whetherToSolve",item.getWhetherToSolve());
                object.put("solution",item.getSolution());
                array.add(object);
            }
            message = "获取成功";
            GlobalResult result = GlobalResult.build(200, message, array);
            return result;
        }
    }
    @ApiOperation(value = "提交意见信息")
    @PostMapping("/create_opinion")
    @ResponseBody
    public GlobalResult createOpinion(@RequestParam(value = "skey", required = false) String skey,
                                      @RequestParam(value = "role", required = false) String role,
                                      @RequestParam(value = "username", required = false) String username,
                                      @RequestParam(value = "problem", required = false) String problem){
        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        String message;
        Date date = new Date();
        Opinion opinion=new Opinion();
        opinion.setUserId(user.getOpenId());
        opinion.setRole(role);
        opinion.setUsername(username);
        opinion.setProblem(problem);
        opinion.setWhetherToSolve("未回复");
        opinion.setCtime(String.valueOf(date.getTime()));
        int a = opinionMapper.insert(opinion);
        if (a == 1) {
            message = "提交成功";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            message = "提交失败";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        }
    }

}
