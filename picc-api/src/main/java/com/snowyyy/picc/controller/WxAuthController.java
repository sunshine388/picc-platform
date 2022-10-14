package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snowyyy.picc.common.GlobalResult;
import com.snowyyy.picc.entity.Specialist;
import com.snowyyy.picc.mapper.NurseMapper;
import com.snowyyy.picc.mapper.PatientMapper;
import com.snowyyy.picc.mapper.SpecialistMapper;
import com.snowyyy.picc.mapper.WxUserMapper;
import com.snowyyy.picc.entity.WxUser;
import com.snowyyy.picc.entity.Patient;
import com.snowyyy.picc.entity.Nurse;
import com.snowyyy.picc.common.WechatUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@CrossOrigin
@Controller
@RequestMapping("/auth")
@Api(tags = "微信身份验证接口")
public class WxAuthController {

    @Autowired
    private WxUserMapper userMapper;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private NurseMapper nurseMapper;
    @Autowired
    private SpecialistMapper specialistMapper;

    @ApiOperation(value = "微信登录")
    @PostMapping("/login_by_weixin")
    @ResponseBody
    public GlobalResult user_login(@RequestParam(value = "code", required = false) String code,
                                   @RequestParam(value = "rawData", required = false) String rawData,
                                   @RequestParam(value = "signature", required = false) String signature,
                                   @RequestParam(value = "encrypteData", required = false) String encrypteData,
                                   @RequestParam(value = "iv", required = false) String iv) {
        // 用户非敏感信息：rawData
        // 签名：signature
        JSONObject rawDataJson = JSON.parseObject(rawData);
        // 1.接收小程序发送的code
        // 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        // 3.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");

        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature.equals(signature2)) {
            return GlobalResult.build(500, "签名校验失败", null);
        }
        // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
        WxUser user = this.userMapper.selectById(openid);
        // uuid生成唯一key，用于维护微信小程序用户与服务端的会话
        String skey = UUID.randomUUID().toString();
        if (user == null) {
            // 用户信息入库
            String nickName = rawDataJson.getString("nickName");
            String avatarUrl = rawDataJson.getString("avatarUrl");
            String gender = rawDataJson.getString("gender");
            String city = rawDataJson.getString("city");
            String country = rawDataJson.getString("country");
            String province = rawDataJson.getString("province");

            user = new WxUser();
            user.setOpenId(openid);
            user.setSkey(skey);
            user.setCreateTime(new Date());
            user.setLastVisitTime(new Date());
            user.setSessionKey(sessionKey);
            user.setCity(city);
            user.setProvince(province);
            user.setCountry(country);
            user.setAvatarUrl(avatarUrl);
            user.setGender(Integer.parseInt(gender));
            user.setNickName(nickName);

            this.userMapper.insert(user);
        } else {
            // 已存在，更新用户登录时间
            user.setLastVisitTime(new Date());
            // 重新设置会话skey
            user.setSkey(skey);
            this.userMapper.updateById(user);
        }
        //encrypteData比rowData多了appid和openid
        //JSONObject userInfo = WechatUtil.getUserInfo(encrypteData, sessionKey, iv);
        //6. 把新的skey返回给小程序
        GlobalResult result = GlobalResult.build(200, null, skey);
        return result;
    }

    @ApiOperation(value = "患者身份验证")
    @GetMapping("/patientRoleCheck")
    @ResponseBody
    public GlobalResult patientRoleCheck(@RequestParam(value = "skey", required = false) String skey){
        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        JSONObject data=new JSONObject();
        if(user!=null){
            String openid=user.getOpenId();
            Patient patient=this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("user_id", openid));
            if(patient==null){
                GlobalResult result = GlobalResult.build(200, "该用户还未完善个人信息", null);
                return result;
            }else{
                data.put("id", patient.getId());
                data.put("patientnumber", patient.getPatientNumber());
                data.put("username", patient.getName());
                data.put("sex", patient.getSex());
                data.put("phone", patient.getPhone());
                data.put("birthday", patient.getBirthday());
                data.put("city", patient.getCity());
                data.put("culture", patient.getCulture());
                data.put("create_time", patient.getCreateTime());
                GlobalResult result = GlobalResult.build(200, "成功获得该用户资料信息", data);
                return result;
            }
        }else {
            GlobalResult result = GlobalResult.build(400, null, null);
            return result;
        }
    }
    @ApiOperation(value = "护士身份验证")
    @GetMapping("/nurseRoleCheck")
    @ResponseBody
    public GlobalResult nurseRoleCheck(@RequestParam(value = "skey", required = false) String skey){
        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        JSONObject data=new JSONObject();
        if(user!=null){
            String openid=user.getOpenId();
            Nurse nurse=this.nurseMapper.selectOne(new QueryWrapper<Nurse>().eq("user_id", openid));
            if(nurse==null){
                GlobalResult result = GlobalResult.build(200, "该用户还未完善个人信息", null);
                return result;
            }else{
                data.put("id", nurse.getId());
                data.put("jobnumber", nurse.getJobNumber());
                data.put("username", nurse.getName());
                data.put("sex", nurse.getSex());
                data.put("phone", nurse.getPhone());
                data.put("department", nurse.getDepartment());
                data.put("state", nurse.getState());
                data.put("create_time", nurse.getCreateTime());
                GlobalResult result = GlobalResult.build(200, "成功获得还用户资料信息", data);
                return result;
            }
        }else {
            GlobalResult result = GlobalResult.build(400, null, null);
            return result;
        }
    }
    @ApiOperation(value = "外院专家身份验证")
    @GetMapping("/specialistRoleCheck")
    @ResponseBody
    public GlobalResult specialistRoleCheck(@RequestParam(value = "skey", required = false) String skey){
        WxUser user = this.userMapper.selectOne(new QueryWrapper<WxUser>().eq("skey", skey));
        JSONObject data=new JSONObject();
        if(user!=null){
            String openid=user.getOpenId();
            Specialist nurse=this.specialistMapper.selectOne(new QueryWrapper<Specialist>().eq("user_id", openid));
            if(nurse==null){
                GlobalResult result = GlobalResult.build(200, "该用户还未完善个人信息", null);
                return result;
            }else{
                data.put("id", nurse.getId());
                data.put("specialistNumber", nurse.getSpecialistNumber());
                data.put("username", nurse.getName());
                data.put("sex", nurse.getSex());
                data.put("phone", nurse.getPhone());
                data.put("workUnit", nurse.getWorkUnit());
                data.put("state", nurse.getState());
                data.put("create_time", nurse.getCreateTime());
                GlobalResult result = GlobalResult.build(200, "成功获得还用户资料信息", data);
                return result;
            }
        }else {
            GlobalResult result = GlobalResult.build(400, null, null);
            return result;
        }
    }
}
