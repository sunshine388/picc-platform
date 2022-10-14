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

import java.util.Calendar;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/statistic")
@Api(tags = "管理中心统计分析接口")
public class AdminStatisticController {
    @Autowired
    private PatientCatheterMapper patientCatheterMapper;
    @Autowired
    private PatientMaintainMapper patientMaintainMapper;
    @Autowired
    private PatientExtubationMapper patientExtubationMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private NurseMapper nurseMapper;
    @Autowired
    private NursePatientBindingMapper bindingMapper;

    @ApiOperation(value = "获取统计信息")
    @GetMapping("/get_statistic")
    @ResponseBody
    public GlobalResult getStatistic(@RequestParam(value = "startDate", required = false) String startDate,
                                     @RequestParam(value = "endDate", required = false) String endDate){
        JSONObject data=new JSONObject();
        JSONArray array1=new JSONArray();
        JSONArray array2=new JSONArray();
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int year,month;
        for(int i=5;i>=0;i--){
            if(nowMonth-i>0){
                year=nowYear;
                month=nowMonth-i;
            }else{
                year=nowYear-1;
                month=12+nowMonth-i;
            }
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.YEAR, year);
            c1.set(Calendar.MONTH, month - 1);
            c1.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
            c1.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
            c1.set(Calendar.MINUTE, 0); //将分钟至0
            c1.set(Calendar.SECOND,0); //将秒至0
            c1.set(Calendar.MILLISECOND, 0); //将毫秒至0

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.YEAR, year);
            c2.set(Calendar.MONTH, month - 1);
            c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
            c2.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
            c2.set(Calendar.MINUTE, 59); //将分钟至59
            c2.set(Calendar.SECOND,59); //将秒至59
            c2.set(Calendar.MILLISECOND, 999); //将毫秒至999

            long firstDate=c1.getTimeInMillis();
            long lastDate=c2.getTimeInMillis();

            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //System.out.println(sdf.format(c1.getTime()));
            //System.out.println(firstDate);
            //System.out.println(sdf.format(c2.getTime()));
            //System.out.println(lastDate);
            int count1=patientCatheterMapper.selectCount(new QueryWrapper<PatientCatheter>().ge("catheter_time",firstDate).le("catheter_time",lastDate).eq("catheter_hospital","本院"));
            int count2=patientExtubationMapper.selectCount(new QueryWrapper<PatientExtubation>().ge("extubation_date",firstDate).le("extubation_date",lastDate));

            array1.add(count1);
            array2.add(count2);
        }
        data.put("catheter",array1);
        data.put("extubation",array2);
        GlobalResult result = GlobalResult.build(200, "ok", data);
        return result;
    }

    @ApiOperation(value = "获取统计信息")
    @GetMapping("/get_statisticNew")
    @ResponseBody
    public GlobalResult getStatisticNew(@RequestParam(value = "year") String year){
        Calendar calendar = Calendar.getInstance();
        String nowYear = calendar.get(Calendar.YEAR)+"";
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        JSONObject data=new JSONObject();
        JSONObject catheter=new JSONObject();
        JSONObject maintain=new JSONObject();
        JSONArray extubation=new JSONArray();
        JSONArray companion=new JSONArray();
        JSONObject companionBar=new JSONObject();
        JSONArray monthList=new JSONArray();
        JSONArray nurseList=new JSONArray();

        String arrayStr="[\"相关性血流感染\",\"导管堵塞\",\"静脉炎\",\"继发性导管异位\",\"局部皮肤湿疹\",\"皮肤过敏反应\",\"淋巴漏\"]";

        JSONArray catheterArray1=new JSONArray();
        JSONArray catheterArray2=new JSONArray();
        JSONArray maintainArray1=new JSONArray();
        JSONArray maintainArray2=new JSONArray();
        JSONArray companionBarArray1=new JSONArray();
        JSONArray companionBarArray2=JSONArray.fromObject(arrayStr);
        JSONArray companionBarArray3=new JSONArray();

        if(year.equals(nowYear)){
            for(int i=1;i<=nowMonth;i++){
                monthList.add(i+"月");
            }
        }else{
            for(int i=1;i<=12;i++) {
                monthList.add(i + "月");
            }
        }
        List<Nurse> nurses=nurseMapper.selectList(new QueryWrapper<Nurse>().like("state", "1") );
        for(Nurse nurse:nurses) {
            nurseList.add(nurse.getName());
        }

        for(int i=0;i<monthList.size();i++){
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.YEAR, Integer.parseInt(year));
            c1.set(Calendar.MONTH, i);
            c1.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
            c1.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
            c1.set(Calendar.MINUTE, 0); //将分钟至0
            c1.set(Calendar.SECOND,0); //将秒至0
            c1.set(Calendar.MILLISECOND, 0); //将毫秒至0

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.YEAR, Integer.parseInt(year));
            c2.set(Calendar.MONTH, i);
            c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
            c2.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
            c2.set(Calendar.MINUTE, 59); //将分钟至59
            c2.set(Calendar.SECOND,59); //将秒至59
            c2.set(Calendar.MILLISECOND, 999); //将毫秒至999

            long startDate=c1.getTimeInMillis();
            long endDate=c2.getTimeInMillis();
            int allCount1=patientCatheterMapper.selectCount(new QueryWrapper<PatientCatheter>().ge("catheter_time",startDate).le("catheter_time",endDate).eq("catheter_hospital","本院"));
            int allCount2=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate));
            catheterArray1.add(allCount1);
            maintainArray1.add(allCount2);
            companionBarArray1.add(allCount2);
            JSONArray array1=new JSONArray();
            JSONArray array2=new JSONArray();
            JSONArray array3=new JSONArray();
            array1.add(i+1+"月");
            array2.add(i+1+"月");
            array3.add(i+1+"月");
            for(Nurse nurse:nurses){
                int cathetercount=patientCatheterMapper.selectCount(new QueryWrapper<PatientCatheter>().ge("catheter_time",startDate).le("catheter_time",endDate).eq("nurse_jobnumber",nurse.getJobNumber()).eq("catheter_hospital","本院"));
                int maintaincount=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("nurse_jobnumber",nurse.getJobNumber()));
                array1.add(cathetercount);
                array2.add(maintaincount);
            }
            catheterArray2.add(array1);
            maintainArray2.add(array2);

            JSONArray extubationArray=new JSONArray();
            JSONArray companionArray=new JSONArray();
            int extubationCount1=patientExtubationMapper.selectCount(new QueryWrapper<PatientExtubation>().ge("extubation_date",startDate).le("extubation_date",endDate).eq("reason","正常拔管"));
            int extubationCount2=patientExtubationMapper.selectCount(new QueryWrapper<PatientExtubation>().ge("extubation_date",startDate).le("extubation_date",endDate).ne("reason","正常拔管"));
            JSONObject extubationObject1=new JSONObject();
            extubationObject1.put("value",extubationCount1);
            extubationObject1.put("name","正常拔管");
            JSONObject extubationObject2=new JSONObject();
            extubationObject2.put("value",extubationCount2);
            extubationObject2.put("name","非计划拔管");
            extubationArray.add(extubationObject1);
            extubationArray.add(extubationObject2);
            extubation.add(extubationArray);

            int companionCount1=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","无"));
            int companionCount2=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).ne("companion","无"));
            JSONObject companionObject1=new JSONObject();
            companionObject1.put("value",companionCount1);
            companionObject1.put("name","未发生并发症");
            JSONObject companionObject2=new JSONObject();
            companionObject2.put("value",companionCount2);
            companionObject2.put("name","发生并发症");
            companionArray.add(companionObject1);
            companionArray.add(companionObject2);
            companion.add(companionArray);


            int count1=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","相关性血流感染"));
            int count2=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","导管堵塞"));
            int count3=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","静脉炎"));
            int count4=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","继发性导管异位"));
            int count5=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","局部皮肤湿疹"));
            int count6=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","皮肤过敏反应"));
            int count7=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","淋巴漏"));
            array3.add(count1);
            array3.add(count2);
            array3.add(count3);
            array3.add(count4);
            array3.add(count5);
            array3.add(count6);
            array3.add(count7);
            companionBarArray3.add(array3);
        }

        catheter.put("allList",catheterArray1);
        catheter.put("seriesData",catheterArray2);
        maintain.put("allList",maintainArray1);
        maintain.put("seriesData",maintainArray2);
        companionBar.put("allList",companionBarArray1);
        companionBar.put("xAxisData",companionBarArray2);
        companionBar.put("seriesData",companionBarArray3);

        data.put("catheter",catheter);
        data.put("maintain",maintain);
        data.put("extubation",extubation);
        data.put("companion",companion);
        data.put("companionBar",companionBar);
        data.put("monthList",monthList);
        data.put("nurseList",nurseList);
        GlobalResult result = GlobalResult.build(200, "ok", data);
        return result;
    }
    @ApiOperation(value = "获取统计信息置管")
    @GetMapping("/get_statisticCatheter")
    @ResponseBody
    public GlobalResult getStatisticCatheter(@RequestParam(value = "year") String year){
        Calendar calendar = Calendar.getInstance();
        String nowYear = calendar.get(Calendar.YEAR)+"";
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        JSONObject data=new JSONObject();
        JSONObject catheter=new JSONObject();
        JSONArray monthList=new JSONArray();
        JSONArray nurseList=new JSONArray();

        JSONArray catheterArray1=new JSONArray();
        JSONArray catheterArray2=new JSONArray();

        if(year.equals(nowYear)){
            for(int i=1;i<=nowMonth;i++){
                monthList.add(i+"月");
            }
        }else{
            for(int i=1;i<=12;i++) {
                monthList.add(i + "月");
            }
        }
        List<Nurse> nurses=nurseMapper.selectList(new QueryWrapper<Nurse>().like("state", "1") );
        for(Nurse nurse:nurses) {
            nurseList.add(nurse.getName());
        }
        for(int i=0;i<monthList.size();i++){
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.YEAR, Integer.parseInt(year));
            c1.set(Calendar.MONTH, i);
            c1.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
            c1.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
            c1.set(Calendar.MINUTE, 0); //将分钟至0
            c1.set(Calendar.SECOND,0); //将秒至0
            c1.set(Calendar.MILLISECOND, 0); //将毫秒至0
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.YEAR, Integer.parseInt(year));
            c2.set(Calendar.MONTH, i);
            c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
            c2.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
            c2.set(Calendar.MINUTE, 59); //将分钟至59
            c2.set(Calendar.SECOND,59); //将秒至59
            c2.set(Calendar.MILLISECOND, 999); //将毫秒至999
            long startDate=c1.getTimeInMillis();
            long endDate=c2.getTimeInMillis();
            int allCount1=patientCatheterMapper.selectCount(new QueryWrapper<PatientCatheter>().ge("catheter_time",startDate).le("catheter_time",endDate).eq("catheter_hospital","本院"));
            catheterArray1.add(allCount1);
            JSONArray array1=new JSONArray();
            array1.add(i+1+"月");
            for(Nurse nurse:nurses){
                int cathetercount=patientCatheterMapper.selectCount(new QueryWrapper<PatientCatheter>().ge("catheter_time",startDate).le("catheter_time",endDate).eq("nurse_jobnumber",nurse.getJobNumber()).eq("catheter_hospital","本院"));
                array1.add(cathetercount);
            }
            catheterArray2.add(array1);
        }

        catheter.put("allList",catheterArray1);
        catheter.put("seriesData",catheterArray2);
        data.put("catheter",catheter);
        data.put("monthList",monthList);
        data.put("nurseList",nurseList);
        GlobalResult result = GlobalResult.build(200, "ok", data);
        return result;
    }
    @ApiOperation(value = "获取统计信息维护")
    @GetMapping("/get_statisticMaintain")
    @ResponseBody
    public GlobalResult getStatisticMaintain(@RequestParam(value = "year") String year){
        Calendar calendar = Calendar.getInstance();
        String nowYear = calendar.get(Calendar.YEAR)+"";
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        JSONObject data=new JSONObject();
        JSONObject maintain=new JSONObject();
        JSONArray monthList=new JSONArray();
        JSONArray nurseList=new JSONArray();

        JSONArray maintainArray1=new JSONArray();
        JSONArray maintainArray2=new JSONArray();
        if(year.equals(nowYear)){
            for(int i=1;i<=nowMonth;i++){
                monthList.add(i+"月");
            }
        }else{
            for(int i=1;i<=12;i++) {
                monthList.add(i + "月");
            }
        }
        List<Nurse> nurses=nurseMapper.selectList(new QueryWrapper<Nurse>().like("state", "1") );
        for(Nurse nurse:nurses) {
            nurseList.add(nurse.getName());
        }
        for(int i=0;i<monthList.size();i++){
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.YEAR, Integer.parseInt(year));
            c1.set(Calendar.MONTH, i);
            c1.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
            c1.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
            c1.set(Calendar.MINUTE, 0); //将分钟至0
            c1.set(Calendar.SECOND,0); //将秒至0
            c1.set(Calendar.MILLISECOND, 0); //将毫秒至0

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.YEAR, Integer.parseInt(year));
            c2.set(Calendar.MONTH, i);
            c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
            c2.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
            c2.set(Calendar.MINUTE, 59); //将分钟至59
            c2.set(Calendar.SECOND,59); //将秒至59
            c2.set(Calendar.MILLISECOND, 999); //将毫秒至999

            long startDate=c1.getTimeInMillis();
            long endDate=c2.getTimeInMillis();
            int allCount2=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate));
            maintainArray1.add(allCount2);
            JSONArray array2=new JSONArray();
            array2.add(i+1+"月");
            for(Nurse nurse:nurses){
                int maintaincount=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("nurse_jobnumber",nurse.getJobNumber()));
                array2.add(maintaincount);
            }
            maintainArray2.add(array2);
        }
        maintain.put("allList",maintainArray1);
        maintain.put("seriesData",maintainArray2);
        data.put("maintain",maintain);
        data.put("monthList",monthList);
        data.put("nurseList",nurseList);
        GlobalResult result = GlobalResult.build(200, "ok", data);
        return result;
    }
    @ApiOperation(value = "获取统计信息拔管")
    @GetMapping("/get_statisticExtubation")
    @ResponseBody
    public GlobalResult getStatisticExtubation(@RequestParam(value = "year") String year){
        Calendar calendar = Calendar.getInstance();
        String nowYear = calendar.get(Calendar.YEAR)+"";
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        JSONObject data=new JSONObject();
        JSONArray extubation=new JSONArray();
        JSONArray monthList=new JSONArray();
        JSONArray nurseList=new JSONArray();
        JSONArray allList=new JSONArray();
        if(year.equals(nowYear)){
            for(int i=1;i<=nowMonth;i++){
                monthList.add(i+"月");
            }
        }else{
            for(int i=1;i<=12;i++) {
                monthList.add(i + "月");
            }
        }
        List<Nurse> nurses=nurseMapper.selectList(new QueryWrapper<Nurse>().like("state", "1") );
        for(Nurse nurse:nurses) {
            nurseList.add(nurse.getName());
        }
        for(int i=0;i<monthList.size();i++){
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.YEAR, Integer.parseInt(year));
            c1.set(Calendar.MONTH, i);
            c1.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
            c1.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
            c1.set(Calendar.MINUTE, 0); //将分钟至0
            c1.set(Calendar.SECOND,0); //将秒至0
            c1.set(Calendar.MILLISECOND, 0); //将毫秒至0
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.YEAR, Integer.parseInt(year));
            c2.set(Calendar.MONTH, i);
            c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
            c2.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
            c2.set(Calendar.MINUTE, 59); //将分钟至59
            c2.set(Calendar.SECOND,59); //将秒至59
            c2.set(Calendar.MILLISECOND, 999); //将毫秒至999
            long startDate=c1.getTimeInMillis();
            long endDate=c2.getTimeInMillis();
            JSONArray extubationArray=new JSONArray();
            int extubationCount1=patientExtubationMapper.selectCount(new QueryWrapper<PatientExtubation>().ge("extubation_date",startDate).le("extubation_date",endDate).eq("reason","正常拔管"));
            int extubationCount2=patientExtubationMapper.selectCount(new QueryWrapper<PatientExtubation>().ge("extubation_date",startDate).le("extubation_date",endDate).ne("reason","正常拔管"));
            int all=patientExtubationMapper.selectCount(new QueryWrapper<PatientExtubation>().ge("extubation_date",startDate).le("extubation_date",endDate));
            allList.add(all);
            JSONObject extubationObject1=new JSONObject();
            extubationObject1.put("value",extubationCount1);
            extubationObject1.put("name","正常拔管");
            JSONObject extubationObject2=new JSONObject();
            extubationObject2.put("value",extubationCount2);
            extubationObject2.put("name","非计划拔管");
            extubationArray.add(extubationObject1);
            extubationArray.add(extubationObject2);
            extubation.add(extubationArray);

        }
        data.put("extubation",extubation);
        data.put("monthList",monthList);
        data.put("nurseList",nurseList);
        data.put("allList",allList);
        GlobalResult result = GlobalResult.build(200, "ok", data);
        return result;
    }
    @ApiOperation(value = "获取统计信息并发症")
    @GetMapping("/get_statisticCompanion")
    @ResponseBody
    public GlobalResult getStatisticCompanion(@RequestParam(value = "year") String year){
        Calendar calendar = Calendar.getInstance();
        String nowYear = calendar.get(Calendar.YEAR)+"";
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        JSONObject data=new JSONObject();
        JSONArray companion=new JSONArray();
        JSONObject companionBar=new JSONObject();
        JSONArray monthList=new JSONArray();
        JSONArray nurseList=new JSONArray();

        String arrayStr="[\"相关性血流感染\",\"导管堵塞\",\"静脉炎\",\"继发性导管异位\",\"局部皮肤湿疹\",\"皮肤过敏反应\",\"淋巴漏\"]";

        JSONArray companionBarArray1=new JSONArray();
        JSONArray companionBarArray2=JSONArray.fromObject(arrayStr);
        JSONArray companionBarArray3=new JSONArray();

        if(year.equals(nowYear)){
            for(int i=1;i<=nowMonth;i++){
                monthList.add(i+"月");
            }
        }else{
            for(int i=1;i<=12;i++) {
                monthList.add(i + "月");
            }
        }
        List<Nurse> nurses=nurseMapper.selectList(new QueryWrapper<Nurse>().like("state", "1") );
        for(Nurse nurse:nurses) {
            nurseList.add(nurse.getName());
        }

        for(int i=0;i<monthList.size();i++){
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.YEAR, Integer.parseInt(year));
            c1.set(Calendar.MONTH, i);
            c1.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
            c1.set(Calendar.HOUR_OF_DAY, 0); //将小时至0
            c1.set(Calendar.MINUTE, 0); //将分钟至0
            c1.set(Calendar.SECOND,0); //将秒至0
            c1.set(Calendar.MILLISECOND, 0); //将毫秒至0

            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.YEAR, Integer.parseInt(year));
            c2.set(Calendar.MONTH, i);
            c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
            c2.set(Calendar.HOUR_OF_DAY, 23); //将小时至23
            c2.set(Calendar.MINUTE, 59); //将分钟至59
            c2.set(Calendar.SECOND,59); //将秒至59
            c2.set(Calendar.MILLISECOND, 999); //将毫秒至999

            long startDate=c1.getTimeInMillis();
            long endDate=c2.getTimeInMillis();

            int allCount=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate));

            companionBarArray1.add(allCount);
            JSONArray array3=new JSONArray();
            array3.add(i+1+"月");
            JSONArray companionArray=new JSONArray();
            int companionCount1=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","无"));
            int companionCount2=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).ne("companion","无"));
            JSONObject companionObject1=new JSONObject();
            companionObject1.put("value",companionCount1);
            companionObject1.put("name","未发生并发症");
            JSONObject companionObject2=new JSONObject();
            companionObject2.put("value",companionCount2);
            companionObject2.put("name","发生并发症");
            companionArray.add(companionObject1);
            companionArray.add(companionObject2);
            companion.add(companionArray);

            int count1=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","相关性血流感染"));
            int count2=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","导管堵塞"));
            int count3=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","静脉炎"));
            int count4=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","继发性导管异位"));
            int count5=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","局部皮肤湿疹"));
            int count6=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","皮肤过敏反应"));
            int count7=patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().ge("maintain_date",startDate).le("maintain_date",endDate).eq("companion","淋巴漏"));
            array3.add(count1);
            array3.add(count2);
            array3.add(count3);
            array3.add(count4);
            array3.add(count5);
            array3.add(count6);
            array3.add(count7);
            companionBarArray3.add(array3);
        }

        companionBar.put("xAxisData",companionBarArray2);
        companionBar.put("seriesData",companionBarArray3);

        data.put("companion",companion);
        data.put("companionBar",companionBar);
        data.put("monthList",monthList);
        data.put("nurseList",nurseList);
        data.put("allList",companionBarArray1);
        GlobalResult result = GlobalResult.build(200, "ok", data);
        return result;
    }
}
