package com.snowyyy.picc.controller;

import com.baomidou.mybatisplus.annotation.TableField;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@CrossOrigin
@Controller
@RequestMapping("/record")
@Api(tags = "记录信息接口")
public class RecordController {
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
    private PatientAppointmentMapper patientAppointmentMapper;
    @Autowired
    private PatientInformationMapper patientInformationMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;

    @ApiOperation(value = "获取有PICC档案的患者信息列表")
    @GetMapping("/get_patientsRecord")
    @ResponseBody
    public GlobalResult getPatientsRecord(@RequestParam(value = "query", required = false) String query) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<PatientRecord> patients = patientRecordMapper.selectList(new QueryWrapper<PatientRecord>().like("patient_name", query));
        if (patients == null) {
            String message = "没有记录";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            for (PatientRecord patient : patients) {

                Patient patientitem = patientMapper.selectOne(new QueryWrapper<Patient>().eq("patient_num", patient.getPatientNumber()));

                if (patientitem != null) {
                    JSONObject item = new JSONObject();
                    item.put("id", patientitem.getId());
                    item.put("patientnumber", patientitem.getPatientNumber());
                    item.put("username", patientitem.getName());
                    item.put("sex", patientitem.getSex());
                    item.put("phone", patientitem.getPhone());
                    item.put("birthday", patientitem.getBirthday());
                    item.put("city", patientitem.getCity());
                    item.put("address", patientitem.getAddress());
                    item.put("culture", patientitem.getCulture());
                    item.put("create_time", patientitem.getCreateTime());
                    array.add(item);
                }
            }
            String message = "获取病人列表成功";
            data.put("total", patients.size());
            data.put("patientsRecord", array);
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }
    @ApiOperation(value = "根据患者编号获取患者档案信息")
    @GetMapping("/get_patientRecord/{patientnum}")
    @ResponseBody
    public GlobalResult getPatientRecord(@PathVariable(value = "patientnum") String patientnum) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<PatientRecord> patients = patientRecordMapper.selectList(new QueryWrapper<PatientRecord>().eq("patient_num", patientnum));
        if (patients == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            for(PatientRecord patient:patients) {
                JSONObject object=new JSONObject();
                object.put("id", patient.getId());
                object.put("patientname", patient.getPatientName());
                object.put("patientnumber", patient.getPatientNumber());
                object.put("recordNum", patient.getRecordNum());
                object.put("ctime", patient.getCTime());
                array.add(object);
            }
            data.put("record", array);
            PatientInformation patientInformation=patientInformationMapper.selectOne(new QueryWrapper<PatientInformation>().eq("patient_number", patientnum));
            if(patientInformation!=null) {
                data.put("patientname", patientInformation.getPatientName());
                data.put("patientnumber", patientInformation.getPatientNumber());
                data.put("catheterType", patientInformation.getCatheterType());
                data.put("animalHeat", patientInformation.getAnimalHeat());
                data.put("previousHistory", patientInformation.getPreviousHistory());
                data.put("drugAllergyHistory", patientInformation.getDrugAllergyHistory());
            }
            String message = "获取病人档案信息成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }

    @ApiOperation(value = "根据档案编号获取患者置管信息")
    @GetMapping("/get_patientCatheterNew/{recordNum}")
    @ResponseBody
    public GlobalResult getPatientCatheterNew(@PathVariable(value = "recordNum") String recordNum) {
        JSONArray array = new JSONArray();
        List<PatientCatheter> patients = patientCatheterMapper.selectList(new QueryWrapper<PatientCatheter>().eq("record_num", recordNum));
        if (patients == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            for(PatientCatheter patient:patients) {
                JSONObject object=new JSONObject();
                object.put("id", patient.getId());
                object.put("patientname", patient.getPatientName());
                object.put("patientnumber", patient.getPatientNumber());
                object.put("disease_type", patient.getDiseaseType());
                object.put("first_catheter", patient.getFirstCatheter());
                object.put("catheter_time", patient.getCatheterTime());
                object.put("catheter_department", patient.getCatheterDepartment());
                object.put("catheter_opportunity", patient.getCatheterOpportunity());
                object.put("catheter_model", patient.getCatheterModel());
                object.put("catheter_specification", patient.getCatheterSpecification());
                object.put("catheter_batchnumber", patient.getCatheterBatchnumber());
                object.put("catheter_length", patient.getCatheterLength());
                object.put("puncture_method", patient.getPunctureMethod());
                object.put("catheter_site", patient.getCatheterSite());
                object.put("catheter_arm", patient.getCatheterArm());
                object.put("catheter_vein", patient.getCatheterVein());
                object.put("catheter_tip_position", patient.getCatheterTipPosition());
                object.put("catheter_insertion_length", patient.getCatheterInsertionLength());
                object.put("catheter_exposed_length", patient.getCatheterExposedLength());
                object.put("arm_circumference", patient.getArmCircumference());
                object.put("catheter_brand", patient.getCatheterBrand());
                object.put("chemotherapy_number", patient.getChemotherapyNumber());
                object.put("nurse_jobnumber", patient.getNurseJobnumber());
                object.put("catheter_num", patient.getCatheterNum());
                object.put("record_num", patient.getRecordNum());
                object.put("catheter_hospital", patient.getCatheterHospital());
                object.put("number_of_catheterization", patient.getNumberOfCatheterization());

                array.add(object);
            }
            String message = "获取病人置管信息成功";
            GlobalResult result = GlobalResult.build(200, message, array);
            return result;
        }
    }
    @ApiOperation(value = "根据档案编号获取患者的维护信息")
    @GetMapping("/get_patientMaintainNew/{recordNum}")
    @ResponseBody
    public GlobalResult getPatientMaintainNew(@PathVariable(value = "recordNum") String recordNum) {
        JSONArray array = new JSONArray();
        List<PatientMaintain> patient = patientMaintainMapper.selectList(new QueryWrapper<PatientMaintain>().eq("record_num", recordNum));
        if (patient == null) {
            String message = "没有该用户的维护记录";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            for (PatientMaintain patientitem : patient) {

                JSONObject item = new JSONObject();
                Nurse nurse=nurseMapper.selectOne(new QueryWrapper<Nurse>().eq("job_number",patientitem.getNurseJobnumber()));
                item.put("nursename",nurse.getName());
                item.put("id", patientitem.getId());
                item.put("patientname", patientitem.getPatientName());
                item.put("patientnumber", patientitem.getPatientNumber());
                item.put("maintain_date", patientitem.getMaintainDate());
                item.put("pipeline_unobstructed", patientitem.getPipelineUnobstructed());
                item.put("change_dressing", patientitem.getChangeDressing());
                item.put("replace_connector", patientitem.getReplaceConnector());
                item.put("resistance", patientitem.getResistance());
                item.put("companion", patientitem.getCompanion());
                item.put("treatment_process", patientitem.getTreatmentProcess());

                item.put("blood_return", patientitem.getBloodReturn());
                item.put("catheter_exposed_length", patientitem.getCatheterExposedLength());
                item.put("nurse_jobnumber", patientitem.getNurseJobnumber());
                item.put("maintain_num", patientitem.getMaintainNum());
                item.put("record_num", patientitem.getRecordNum());
                array.add(item);
            }
            String message = "获取病人维护信息列表成功";
            GlobalResult result = GlobalResult.build(200, message, array);
            return result;
        }
    }
    @ApiOperation(value = "根据编号获取患者的拔管信息")
    @GetMapping("/get_patientExtubationNew/{recordNum}")
    @ResponseBody
    public GlobalResult getPatientExtubationNew(@PathVariable(value = "recordNum") String recordNum) {
        JSONArray array = new JSONArray();
        List<PatientExtubation> patients = patientExtubationMapper.selectList(new QueryWrapper<PatientExtubation>().eq("record_num", recordNum));
        if (patients == null) {
            String message = "没有该用户的拔管记录";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            for(PatientExtubation patient:patients) {
                JSONObject object=new JSONObject();
                object.put("id", patient.getId());
                object.put("patientname", patient.getPatientName());
                object.put("patientnumber", patient.getPatientNumber());
                object.put("extubation_date", patient.getExtubationDate());
                object.put("indwelling_time", patient.getIndwellingTime());
                object.put("reason", patient.getReason());
                object.put("type", patient.getType());
                object.put("end_intact", patient.getEndIntact());
                object.put("process_smooth", patient.getProcessSmooth());
                object.put("extubation_num", patient.getExtubationNum());
                object.put("record_num", patient.getRecordNum());
                object.put("nurse_jobnumber", patient.getNurseJobnumber());
                array.add(object);
            }
            String message = "获取病人拔管信息成功";
            GlobalResult result = GlobalResult.build(200, message, array);
            return result;
        }
    }

    /*@ApiOperation(value = "获取已经置管的患者信息列表")
    @GetMapping("/get_patientsCatheter")
    @ResponseBody
    public GlobalResult getPatientsCatheter(@RequestParam(value = "query", required = false) String query,
                                            @RequestParam(value = "pagenum", required = false) String num,
                                            @RequestParam(value = "pagesize", required = false) String size) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<PatientCatheter> patients = this.patientCatheterMapper.selectList(new QueryWrapper<PatientCatheter>().like("patient_name", query));
        if (patients == null) {
            String message = "没有该用户的置管记录";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            for (PatientCatheter patient : patients) {

                Patient patientitem = this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("patient_num", patient.getPatientNumber()));

                if (patientitem != null) {
                    JSONObject item = new JSONObject();
                    item.put("id", patientitem.getId());
                    item.put("patientnumber", patientitem.getPatientNumber());
                    item.put("username", patientitem.getName());
                    item.put("sex", patientitem.getSex());
                    item.put("phone", patientitem.getPhone());
                    item.put("birthday", patientitem.getBirthday());
                    item.put("city", patientitem.getCity());
                    item.put("address", patientitem.getAddress());
                    item.put("culture", patientitem.getCulture());
                    item.put("create_time", patientitem.getCreateTime());
                    array.add(item);
                }
            }
            String message = "获取病人列表成功";
            data.put("total", patients.size());
            data.put("pagenum", Integer.parseInt(num));
            data.put("patientsCatheter", array);
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }

    @ApiOperation(value = "根据编号获取患者置管信息")
    @GetMapping("/get_patientCatheter/{patientnum}")
    @ResponseBody
    public GlobalResult getPatientCatheter(@PathVariable(value = "patientnum") String patientnum) {
        JSONArray array = new JSONArray();
        List<PatientCatheter> patients = this.patientCatheterMapper.selectList(new QueryWrapper<PatientCatheter>().eq("patient_num", patientnum));
        if (patients == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            for(PatientCatheter patient:patients) {
                JSONObject object=new JSONObject();
                object.put("id", patient.getId());
                object.put("patientname", patient.getPatientName());
                object.put("patientnumber", patient.getPatientNumber());
                object.put("disease_type", patient.getDiseaseType());
                object.put("first_catheter", patient.getFirstCatheter());
                object.put("catheter_time", patient.getCatheterTime());
                object.put("catheter_department", patient.getCatheterDepartment());
                object.put("catheter_opportunity", patient.getCatheterOpportunity());
                object.put("catheter_model", patient.getCatheterModel());
                object.put("catheter_specification", patient.getCatheterSpecification());
                object.put("catheter_batchnumber", patient.getCatheterBatchnumber());
                object.put("catheter_length", patient.getCatheterLength());
                object.put("puncture_method", patient.getPunctureMethod());
                object.put("catheter_site", patient.getCatheterSite());
                object.put("catheter_arm", patient.getCatheterArm());
                object.put("catheter_vein", patient.getCatheterVein());
                object.put("catheter_tip_position", patient.getCatheterTipPosition());
                object.put("catheter_insertion_length", patient.getCatheterInsertionLength());
                object.put("catheter_exposed_length", patient.getCatheterExposedLength());
                object.put("arm_circumference", patient.getArmCircumference());
                object.put("chemotherapy_number", patient.getChemotherapyNumber());
                array.add(object);
            }
            String message = "获取病人置管信息成功";
            GlobalResult result = GlobalResult.build(200, message, array);
            return result;
        }
    }
*/
    @ApiOperation(value = "上传患者的置管信息")
    @PostMapping("/create_patientsCatheter")
    @ResponseBody
    public GlobalResult createPatientsCatheter(@RequestParam(value = "appointmentid", required = false) String appointmentid,
                                               @RequestParam(value = "patientName", required = false) String patientName,
                                               @RequestParam(value = "patientNum", required = false) String patientNum,
                                               @RequestParam(value = "diseaseType", required = false) String diseaseType,
                                               @RequestParam(value = "firstCatheter", required = false) String firstCatheter,
                                               @RequestParam(value = "catheterTime", required = false) String catheterTime,
                                               @RequestParam(value = "catheterDepartment", required = false) String catheterDepartment,
                                               @RequestParam(value = "catheterOpportunity", required = false) String catheterOpportunity,
                                               @RequestParam(value = "catheterModel", required = false) String catheterModel,
                                               @RequestParam(value = "catheterSpecification", required = false) String catheterSpecification,
                                               @RequestParam(value = "catheterBatchnumber", required = false) String catheterBatchnumber,
                                               @RequestParam(value = "catheterLength", required = false) String catheterLength,
                                               @RequestParam(value = "punctureMethod", required = false) String punctureMethod,
                                               @RequestParam(value = "catheterSite", required = false) String catheterSite,
                                               @RequestParam(value = "catheterArm", required = false) String catheterArm,
                                               @RequestParam(value = "catheterVein", required = false) String catheterVein,
                                               @RequestParam(value = "catheterTipPosition", required = false) String catheterTipPosition,
                                               @RequestParam(value = "atheterInsertionLength", required = false) String atheterInsertionLength,
                                               @RequestParam(value = "catheterExposedLength", required = false) String catheterExposedLength,
                                               @RequestParam(value = "armCircumference", required = false) String armCircumference,
                                               @RequestParam(value = "chemotherapyNumber", required = false) String chemotherapyNumber,
                                               @RequestParam(value = "catheter_brand", required = false) String catheter_brand,
                                               @RequestParam(value = "nurse_jobnumber", required = false) String nurse_jobnumber,
                                               @RequestParam(value = "catheter_hospital", required = false) String catheter_hospital,
                                               @RequestParam(value = "number_of_catheterization", required = false) String number_of_catheterization) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        String year1 = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date());
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
        int count=this.patientCatheterMapper.selectCount(new QueryWrapper<PatientCatheter>().between("create_time", c1.getTimeInMillis(),c2.getTimeInMillis()));
        String count1=count<10?"0"+count:String.valueOf(count);
        String catheternumber="Z"+year1+""+month1+""+day1+""+count1;
        String recordnumber="BH"+year1+""+month1+""+day1+""+count1;

        PatientRecord record=new PatientRecord();
        record.setRecordNum(recordnumber);
        record.setPatientName(patientName);
        record.setPatientNumber(patientNum);
        record.setCTime(String.valueOf(date.getTime()));
        int a=patientRecordMapper.insert(record);
        PatientCatheter patientCatheter=new PatientCatheter();
        patientCatheter.setPatientName(patientName);
        patientCatheter.setPatientNumber(patientNum);
        patientCatheter.setDiseaseType(diseaseType);
        patientCatheter.setFirstCatheter(firstCatheter);
        patientCatheter.setCatheterTime(catheterTime);
        patientCatheter.setCatheterDepartment(catheterDepartment);
        patientCatheter.setCatheterOpportunity(catheterOpportunity);
        patientCatheter.setCatheterModel(catheterModel);
        patientCatheter.setCatheterSpecification(catheterSpecification);
        patientCatheter.setCatheterBatchnumber(catheterBatchnumber);
        patientCatheter.setCatheterLength(catheterLength);
        patientCatheter.setPunctureMethod(punctureMethod);
        patientCatheter.setCatheterSite(catheterSite);
        patientCatheter.setCatheterArm(catheterArm);
        patientCatheter.setCatheterVein(catheterVein);
        patientCatheter.setCatheterTipPosition(catheterTipPosition);
        patientCatheter.setCatheterInsertionLength(atheterInsertionLength);
        patientCatheter.setCatheterExposedLength(catheterExposedLength);
        patientCatheter.setArmCircumference(armCircumference);
        patientCatheter.setChemotherapyNumber(chemotherapyNumber);
        patientCatheter.setCatheterBrand(catheter_brand);
        patientCatheter.setNurseJobnumber(nurse_jobnumber);
        patientCatheter.setCatheterHospital(catheter_hospital);
        patientCatheter.setNumberOfCatheterization(number_of_catheterization);

        patientCatheter.setCatheterNum(catheternumber);
        patientCatheter.setRecordNum(recordnumber);

        patientCatheter.setCreateTime(String.valueOf(date.getTime()));
        int a2=patientCatheterMapper.insert(patientCatheter);
        if (a == 1&&a2==1) {
            String id=patientCatheter.getId();
            PatientAppointment appointment=patientAppointmentMapper.selectById(appointmentid);
            appointment.setRecordId(id);
            appointment.setStatus("3");
            int b=patientAppointmentMapper.updateById(appointment);
            //更新患者下次维护时间
            long time = 1000 * 60 * 60 * 24 * 7+Long.parseLong(catheterTime);
            String nextTime=String.valueOf(new Date(time).getTime());
            PatientInformation maintain=patientInformationMapper.selectOne(new QueryWrapper<PatientInformation>().eq("patient_number",patientNum));
            if(maintain!=null){
                maintain.setNextTime(nextTime);
                maintain.setHasCatheter("true");
                maintain.setRecordNum(recordnumber);
                patientInformationMapper.updateById(maintain);
            }else{
                PatientInformation patientInformation =new PatientInformation();
                patientInformation.setPatientName(patientName);
                patientInformation.setPatientNumber(patientNum);
                patientInformation.setNextTime(nextTime);
                patientInformation.setHasCatheter("true");
                maintain.setRecordNum(recordnumber);
                patientInformationMapper.insert(patientInformation);
            }
            if(b==1) {
                GlobalResult result = GlobalResult.build(200, "上传成功", null);
                return result;
            }else{
                GlobalResult result = GlobalResult.build(400, "上传失败", null);
                return result;
            }
        } else {
            GlobalResult result = GlobalResult.build(400, "上传失败", null);
            return result;
        }
    }
    /*
    @ApiOperation(value = "获取有维护记录的患者信息列表")
    @GetMapping("/get_patientsMaintain")
    @ResponseBody
    public GlobalResult getPatientMaintain(@RequestParam(value = "query", required = false) String query,
                                           @RequestParam(value = "pagenum", required = false) String num,
                                           @RequestParam(value = "pagesize", required = false) String size) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<PatientMaintain> patients = this.patientMaintainMapper.selectList(new QueryWrapper<PatientMaintain>().like("patient_name", query));
        if (patients == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            for (PatientMaintain patient : patients) {
                Patient patientitem = this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("patient_num", patient.getPatientNumber()));
                if (patientitem != null) {
                    Boolean exist=false;
                    for(int i=0;i<array.size();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        String patientnumber = jsonObject.getString("patientnumber");
                        String p=patientitem.getPatientNumber();
                        if(p.equals(patientnumber)){
                            exist=true;
                        }
                    }
                    if(exist==false) {
                        JSONObject item = new JSONObject();
                        item.put("id", patientitem.getId());
                        item.put("patientnumber", patientitem.getPatientNumber());
                        item.put("username", patientitem.getName());
                        item.put("sex", patientitem.getSex());
                        item.put("phone", patientitem.getPhone());
                        item.put("birthday", patientitem.getBirthday());
                        item.put("city", patientitem.getCity());
                        item.put("address", patientitem.getAddress());
                        item.put("culture", patientitem.getCulture());
                        item.put("create_time", patientitem.getCreateTime());
                        array.add(item);
                    }
                }
            }
            String message = "获取病人列表成功";
            data.put("total", patients.size());
            data.put("pagenum", Integer.parseInt(num));
            data.put("patientsMaintain", array);
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }
    @ApiOperation(value = "根据编号获取患者的维护信息")
    @GetMapping("/get_patientMaintain/{patientnum}")
    @ResponseBody
    public GlobalResult getPatientMaintain(@PathVariable(value = "patientnum") String patientnum) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<PatientMaintain> patient = this.patientMaintainMapper.selectList(new QueryWrapper<PatientMaintain>().eq("patient_num", patientnum));
        if (patient == null) {
            String message = "没有该用户的维护记录";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            for (PatientMaintain patientitem : patient) {

                JSONObject item = new JSONObject();
                item.put("id", patientitem.getId());
                item.put("patientname", patientitem.getPatientName());
                item.put("patientnumber", patientitem.getPatientNumber());
                item.put("maintain_date", patientitem.getMaintainDate());
                item.put("pipeline_unobstructed", patientitem.getPipelineUnobstructed());
                item.put("change_dressing", patientitem.getChangeDressing());
                item.put("replace_connector", patientitem.getReplaceConnector());
                item.put("resistance", patientitem.getResistance());
                item.put("companion", patientitem.getCompanion());
                item.put("treatment_process", patientitem.getTreatmentProcess());
                array.add(item);
            }
            data.put("patientMaintain", array);
            String message = "获取病人维护信息列表成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }*/
    @ApiOperation(value = "上传患者的维护信息")
    @PostMapping("/create_patientsMaintain")
    @ResponseBody
    public GlobalResult createPatientsMaintain(@RequestParam(value = "appointmentid", required = false) String appointmentid,
                                               @RequestParam(value = "patientName", required = false) String patientName,
                                               @RequestParam(value = "patientNum", required = false) String patientNum,
                                               @RequestParam(value = "maintainDate", required = false) String maintainDate,
                                               @RequestParam(value = "pipelineUnobstructed", required = false) String pipelineUnobstructed,
                                               @RequestParam(value = "changeDressing", required = false) String changeDressing,
                                               @RequestParam(value = "replaceConnector", required = false) String replaceConnector,
                                               @RequestParam(value = "resistance", required = false) String resistance,
                                               @RequestParam(value = "companion", required = false) String companion,
                                               @RequestParam(value = "treatmentProcess", required = false) String treatmentProcess,
                                               @RequestParam(value = "blood_return", required = false) String blood_return,
                                               @RequestParam(value = "catheter_exposed_length", required = false) String catheter_exposed_length,
                                               @RequestParam(value = "nurse_jobnumber", required = false) String nurse_jobnumber,
                                               @RequestParam(value = "record_num", required = false) String record_num) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        String year1 = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date());
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
        int count=this.patientMaintainMapper.selectCount(new QueryWrapper<PatientMaintain>().between("maintain_date", c1.getTimeInMillis(),c2.getTimeInMillis()));
        String count1=count<10?"0"+count:String.valueOf(count);
        String maintainnumber="Z"+year1+""+month1+""+day1+""+count1;
        PatientMaintain patientMaintain=new PatientMaintain();
        patientMaintain.setPatientName(patientName);
        patientMaintain.setPatientNumber(patientNum);
        patientMaintain.setMaintainDate(maintainDate);
        patientMaintain.setPipelineUnobstructed(pipelineUnobstructed);
        patientMaintain.setChangeDressing(changeDressing);
        patientMaintain.setReplaceConnector(replaceConnector);
        patientMaintain.setResistance(resistance);
        patientMaintain.setCompanion(companion);
        patientMaintain.setTreatmentProcess(treatmentProcess);
        patientMaintain.setMaintainNum(maintainnumber);
        patientMaintain.setBloodReturn(blood_return);
        patientMaintain.setCatheterExposedLength(catheter_exposed_length);
        patientMaintain.setNurseJobnumber(nurse_jobnumber);
        patientMaintain.setRecordNum(record_num);
        int a=patientMaintainMapper.insert(patientMaintain);
        if (a == 1) {
            String id=patientMaintain.getId();
            PatientAppointment appointment=this.patientAppointmentMapper.selectById(appointmentid);
            appointment.setRecordId(id);
            appointment.setStatus("3");
            int b=patientAppointmentMapper.updateById(appointment);
            //更新患者下次维护时间
            Date date=new Date(Long.parseLong(maintainDate));
            long time = 1000 * 60 * 60 * 24 * 7+Long.parseLong(maintainDate);
            String nextTime=String.valueOf(new Date(time).getTime());
            PatientInformation maintain=this.patientInformationMapper.selectOne(new QueryWrapper<PatientInformation>().eq("patient_number",patientNum));
            if(maintain!=null){
                maintain.setNextTime(nextTime);
                maintain.setHasCatheter("true");
                patientInformationMapper.updateById(maintain);
            }else{
                PatientInformation patientInformation =new PatientInformation();
                patientInformation.setPatientName(patientName);
                patientInformation.setPatientNumber(patientNum);
                patientInformation.setNextTime(nextTime);
                patientInformation.setHasCatheter("true");
                patientInformationMapper.insert(patientInformation);
            }
            if(b==1) {
                GlobalResult result = GlobalResult.build(200, "上传成功", null);
                return result;
            }else{
                GlobalResult result = GlobalResult.build(400, "上传失败", null);
                return result;
            }
        } else {
            GlobalResult result = GlobalResult.build(400, "上传失败", null);
            return result;
        }
    }
/*
    @ApiOperation(value = "获取已经拔管的患者信息列表")
    @GetMapping("/get_patientsExtubation")
    @ResponseBody
    public GlobalResult getPatientExtubation(@RequestParam(value = "query", required = false) String query,
                                             @RequestParam(value = "pagenum", required = false) String num,
                                             @RequestParam(value = "pagesize", required = false) String size) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<PatientExtubation> patients = this.patientExtubationMapper.selectList(new QueryWrapper<PatientExtubation>().like("patient_name", query));
        if (patients == null) {
            String message = "用户不存在";
            GlobalResult result = GlobalResult.build(400, message, null);
            return result;
        } else {
            for (PatientExtubation patient : patients) {

                Patient patientitem = this.patientMapper.selectOne(new QueryWrapper<Patient>().eq("patient_num", patient.getPatientNumber()));
                if (patientitem != null) {
                    JSONObject item = new JSONObject();
                    item.put("id", patientitem.getId());
                    item.put("patientnumber", patientitem.getPatientNumber());
                    item.put("username", patientitem.getName());
                    item.put("sex", patientitem.getSex());
                    item.put("phone", patientitem.getPhone());
                    item.put("birthday", patientitem.getBirthday());
                    item.put("city", patientitem.getCity());
                    item.put("address", patientitem.getAddress());
                    item.put("culture", patientitem.getCulture());
                    item.put("create_time", patientitem.getCreateTime());
                    array.add(item);
                }
            }
            String message = "获取病人列表成功";
            data.put("total", patients.size());
            data.put("pagenum", Integer.parseInt(num));
            data.put("patientsExtubation", array);
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }

    @ApiOperation(value = "根据编号获取患者的拔管信息")
    @GetMapping("/get_patientExtubation/{patientnum}")
    @ResponseBody
    public GlobalResult getPatientExtubation(@PathVariable(value = "patientnum") String patientnum) {
        JSONObject data = new JSONObject();
        PatientExtubation patient = this.patientExtubationMapper.selectOne(new QueryWrapper<PatientExtubation>().eq("patient_num", patientnum));
        if (patient == null) {
            String message = "没有该用户的拔管记录";
            GlobalResult result = GlobalResult.build(200, message, null);
            return result;
        } else {
            data.put("id", patient.getId());
            data.put("patientname", patient.getPatientName());
            data.put("patientnumber", patient.getPatientNumber());
            data.put("extubation_date", patient.getExtubationDate());
            data.put("indwelling_time", patient.getIndwellingTime());
            data.put("reason", patient.getReason());
            String message = "获取病人拔管信息成功";
            GlobalResult result = GlobalResult.build(200, message, data);
            return result;
        }
    }
 */
    @ApiOperation(value = "上传患者的拔管信息")
    @PostMapping("/create_patientsExtubation")
    @ResponseBody
    public GlobalResult createPatientsExtubation(@RequestParam(value = "appointmentid", required = false) String appointmentid,
                                                 @RequestParam(value = "patientName", required = false) String patientName,
                                                 @RequestParam(value = "patientNum", required = false) String patientNum,
                                                 @RequestParam(value = "extubationDate", required = false) String extubationDate,
                                                 @RequestParam(value = "indwellingTime", required = false) String indwellingTime,
                                                 @RequestParam(value = "reason", required = false) String reason,
                                                 @RequestParam(value = "end_intact", required = false) String end_intact,
                                                 @RequestParam(value = "process_smooth", required = false) String process_smooth,
                                                 @RequestParam(value = "record_num", required = false) String record_num,
                                                 @RequestParam(value = "nurse_jobnumber", required = false) String nurse_jobnumber) {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
        int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
        String year1 = new SimpleDateFormat("yyyy", Locale.CHINESE).format(new Date());
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
        int count=this.patientExtubationMapper.selectCount(new QueryWrapper<PatientExtubation>().between("extubation_date", c1.getTimeInMillis(),c2.getTimeInMillis()));
        String count1=count<10?"0"+count:String.valueOf(count);
        String extubationnumber="Z"+year1+""+month1+""+day1+""+count1;
        PatientExtubation patientExtubation=new PatientExtubation();
        patientExtubation.setPatientName(patientName);
        patientExtubation.setPatientNumber(patientNum);
        patientExtubation.setExtubationDate(extubationDate);
        patientExtubation.setIndwellingTime(indwellingTime);
        patientExtubation.setReason(reason);
        patientExtubation.setExtubationNum(extubationnumber);
        patientExtubation.setEndIntact(end_intact);
        patientExtubation.setProcessSmooth(process_smooth);
        patientExtubation.setRecordNum(record_num);
        patientExtubation.setNurseJobnumber(nurse_jobnumber);
        int a=patientExtubationMapper.insert(patientExtubation);
        if (a == 1) {
            //更新患者下次维护时间
            PatientInformation maintain=this.patientInformationMapper.selectOne(new QueryWrapper<PatientInformation>().eq("patient_number",patientNum));
            if(maintain!=null){
                maintain.setNextTime("");
                maintain.setHasCatheter("false");
                maintain.setRecordNum("");
                patientInformationMapper.updateById(maintain);
            }
            String id=patientExtubation.getId();
            PatientAppointment appointment=this.patientAppointmentMapper.selectById(appointmentid);
            appointment.setRecordId(id);
            appointment.setStatus("3");
            int b=patientAppointmentMapper.updateById(appointment);
            if(b==1) {
                GlobalResult result = GlobalResult.build(200, "上传成功", null);
                return result;
            }else{
                GlobalResult result = GlobalResult.build(400, "上传失败", null);
                return result;
            }
        } else {
            GlobalResult result = GlobalResult.build(400, "上传失败", null);
            return result;
        }
    }
    @ApiOperation(value = "根据预约表的id获取记录")
    @GetMapping("/get_record")
    @ResponseBody
    public GlobalResult getRecord(@RequestParam(value = "id", required = false) String id){
        PatientAppointment appointment= patientAppointmentMapper.selectById(id);
        JSONObject data =new JSONObject();
        if(appointment!=null&&appointment.getStatus().equals("3")){
            if(appointment.getType().equals("置管")){
                PatientCatheter catheter=this.patientCatheterMapper.selectById(appointment.getRecordId());
                data.put("id", catheter.getId());
                data.put("patientname", catheter.getPatientName());
                data.put("patientnumber", catheter.getPatientNumber());
                data.put("disease_type", catheter.getDiseaseType());
                data.put("first_catheter", catheter.getFirstCatheter());
                data.put("catheter_time", catheter.getCatheterTime());
                data.put("catheter_department", catheter.getCatheterDepartment());
                data.put("catheter_opportunity", catheter.getCatheterOpportunity());
                data.put("catheter_model", catheter.getCatheterModel());
                data.put("catheter_specification", catheter.getCatheterSpecification());
                data.put("catheter_batchnumber", catheter.getCatheterBatchnumber());
                data.put("catheter_length", catheter.getCatheterLength());
                data.put("puncture_method", catheter.getPunctureMethod());
                data.put("catheter_site", catheter.getCatheterSite());
                data.put("catheter_arm", catheter.getCatheterArm());
                data.put("catheter_vein", catheter.getCatheterVein());
                data.put("catheter_tip_position", catheter.getCatheterTipPosition());
                data.put("catheter_insertion_length", catheter.getCatheterInsertionLength());
                data.put("catheter_exposed_length", catheter.getCatheterExposedLength());
                data.put("arm_circumference", catheter.getArmCircumference());
                data.put("chemotherapy_number", catheter.getChemotherapyNumber());
            }else if(appointment.getType().equals("维护")){
                PatientMaintain maintain=this.patientMaintainMapper.selectById(appointment.getRecordId());
                data.put("id", maintain.getId());
                data.put("patientname", maintain.getPatientName());
                data.put("patientnumber", maintain.getPatientNumber());
                data.put("maintain_date", maintain.getMaintainDate());
                data.put("pipeline_unobstructed", maintain.getPipelineUnobstructed());
                data.put("change_dressing", maintain.getChangeDressing());
                data.put("replace_connector", maintain.getReplaceConnector());
                data.put("resistance", maintain.getResistance());
                data.put("companion", maintain.getCompanion());
                data.put("treatment_process", maintain.getTreatmentProcess());
            }else if(appointment.getType().equals("拔管")){
                PatientExtubation extubation=this.patientExtubationMapper.selectById(appointment.getRecordId());
                data.put("id", extubation.getId());
                data.put("patientname", extubation.getPatientName());
                data.put("patientnumber", extubation.getPatientNumber());
                data.put("extubation_date", extubation.getExtubationDate());
                data.put("indwelling_time", extubation.getIndwellingTime());
                data.put("reason", extubation.getReason());

            }
            GlobalResult result = GlobalResult.build(200, "查询成功", data);
            return result;
        }else{
            GlobalResult result = GlobalResult.build(400, "未查询到相关记录，请稍后再试", null);
            return result;
        }
    }
}
