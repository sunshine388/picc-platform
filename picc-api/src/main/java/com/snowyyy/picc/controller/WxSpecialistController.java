package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Patient;
import com.snowyyy.picc.entity.Specialist;
import com.snowyyy.picc.entity.WxUser;
import com.snowyyy.picc.mapper.SpecialistMapper;
import com.snowyyy.picc.mapper.WxUserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/specialist")
@Api(tags = "微信外院专家接口")
public class WxSpecialistController {
    @Autowired
    private SpecialistMapper specialistMapper;
    @Autowired
    private WxUserMapper userMapper;

    @ApiOperation(value = "外院专家信息提交")
    @PostMapping("/create")
    @ResponseBody
    public GlobalResult specialist(@RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "phone", required = false) String phone,
                              @RequestParam(value = "sex", required = false) String sex,
                              @RequestParam(value = "skey", required = false) String skey,
                              @RequestParam(value = "workUnit", required = false) String workUnit){
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
        int count=this.specialistMapper.selectCount(new QueryWrapper<Specialist>().between("create_time", c1.getTimeInMillis(),c2.getTimeInMillis()));
        String count1=count<10?"0"+count:String.valueOf(count);
        String specialistNumber="WY"+year1+""+month1+""+day1+""+count1;

        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        String message;
        Specialist useritem=this.specialistMapper.selectOne(new QueryWrapper<Specialist>().eq("user_id", user.getOpenId()));
        if(useritem==null){
            Specialist nurse=new Specialist();
            nurse.setName(username);
            nurse.setUserId(user.getOpenId());
            nurse.setSex(sex);
            nurse.setPhone(phone);
            nurse.setSpecialistNumber(specialistNumber);
            nurse.setWorkUnit(workUnit);
            nurse.setState("0");
            nurse.setCreateTime(String.valueOf(date.getTime()));
            int a = specialistMapper.insert(nurse);
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
            Specialist nurse = this.specialistMapper.selectById(useritem.getId());
            nurse.setName(username);
            nurse.setUserId(user.getOpenId());
            nurse.setSex(sex);
            nurse.setPhone(phone);
            nurse.setWorkUnit(workUnit);
            nurse.setState("0");
            nurse.setCreateTime(String.valueOf(date.getTime()));
            int a = specialistMapper.updateById(nurse);
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
    public GlobalResult specialistUpdate(@RequestParam(value = "username", required = false) String username,
                                    @RequestParam(value = "phone", required = false) String phone,
                                    @RequestParam(value = "sex", required = false) String sex,
                                    @RequestParam(value = "skey", required = false) String skey,
                                    @RequestParam(value = "workUnit", required = false) String workUnit){

        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        String message;
        Specialist useritem=this.specialistMapper.selectOne(new QueryWrapper<Specialist>().eq("user_id", user.getOpenId()));
        if(useritem!=null){
            Specialist nurse = this.specialistMapper.selectById(useritem.getId());
            nurse.setName(username);
            nurse.setSex(sex);
            nurse.setPhone(phone);
            nurse.setWorkUnit(workUnit);
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
        }else {
            GlobalResult result = GlobalResult.build(200, "出错啦", null);
            return result;
        }
    }
}
