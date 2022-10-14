// 以下是业务服务器API地址
// 本机开发时使用
const WxApiRoot = "http://localhost:8080/picc-server/";
// const WxApiRoot = 'https://snowyyy.com/picc-server/';

module.exports = {
  AuthLoginByWeixin: WxApiRoot + "auth/login_by_weixin", //微信登录
  patientRoleCheck: WxApiRoot + "auth/patientRoleCheck", //查询病患资料是否存在
  nurseRoleCheck: WxApiRoot + "auth/nurseRoleCheck", //查询护士审核状态
  specialistRoleCheck: WxApiRoot + "auth/specialistRoleCheck", //查询护士审核状态

  PatientInfoSubmit: WxApiRoot + "patient/create", //提交用户资料
  PatientInfoUpdate: WxApiRoot + "patient/update", //修改用户资料
  createAppointment: WxApiRoot + "patient/create_appointment", //创建预约信息
  getAppointment: WxApiRoot + "patient/get_appointmentByPatientnumber", //获取预约信息
  cancelAppointment: WxApiRoot + "patient/cancel_appointmentById", //取消预约
  deleteAppointment: WxApiRoot + "patient/delete_appointmentById", //删除预约记录
  getNextMaintainTime: WxApiRoot + "patient/get_nextMaintainTime", //根据编号获取下次维护时间

  NurseInfoSubmit: WxApiRoot + "nurse/create", //提交护士资料
  NurseInfoUpdate: WxApiRoot + "nurse/update", //修改护士资料
  getAppointmentByJobnumber: WxApiRoot + "nurse/get_appointmentByJobnumber", //获取预约信息
  updateAppointment: WxApiRoot + "nurse/update_appointment", //更新预约信息
  getBindingByJobNumber: WxApiRoot + "binding/get_bindingByJobNumber", //根据护士工号获取绑定信息
  getPatientByPatientNumber: WxApiRoot + "binding/get_patientByPatientNumber", //根据患者编号获取患者信息
  createBinding: WxApiRoot + "binding/create_binding", //绑定患者
  updatePatientInformation: WxApiRoot + "nurse/updatePatientInformation", //

  SpecialistInfoSubmit: WxApiRoot + "specialist/create", //提交护士资料
  SpecialistInfoUpdate: WxApiRoot + "specialist/update", //修改护士资料

  getDepartment: WxApiRoot + "common/get_department", //获取部门信息
  getNurseByDepartment: WxApiRoot + "common/get_nurse_By_department", //根据部门获取医生
  createOpinion: WxApiRoot + "opinion/create_opinion", //提交意见
  getOpinion: WxApiRoot + "opinion/get_opinionBySkey", //获取意见

  getSchedulebyjobnumber: WxApiRoot + "nurses/get_Schedule_by_jobnumber", //根据护士工号获取排期表
  getPatientCatheter: WxApiRoot + "record/get_patientCatheter", //获取病人置管记录
  getPatientMaintain: WxApiRoot + "record/get_patientMaintain", //获取病人维护记录
  getPatientExtubation: WxApiRoot + "record/get_patientExtubation", //获取病人拔管记录
  getPatientRecord: WxApiRoot + "record/get_patientRecord", //获取病人picc档案记录
  getPatientCatheterNew: WxApiRoot + "record/get_patientCatheterNew", //获取病人置管记录
  getPatientMaintainNew: WxApiRoot + "record/get_patientMaintainNew", //获取病人维护记录
  getPatientExtubationNew: WxApiRoot + "record/get_patientExtubationNew", //获取病人拔管记录

  createPatientCatheter: WxApiRoot + "record/create_patientsCatheter", //上传病人置管管记录
  createPatientMaintain: WxApiRoot + "record/create_patientsMaintain", //上传病人维护记录
  createPatientExtubation: WxApiRoot + "record/create_patientsExtubation", //上传病人拔管记录
  getRecord: WxApiRoot + "record/get_record", //根据预约id获取记录

  getPatientByJobNumber: WxApiRoot + "consult/get_patientByJobNumber", //根据护士工号获取患者信息带头像
  getNurseByPatientNumber: WxApiRoot + "consult/get_nurseByPatientNumber", //根据编号获取护士信息带头像
  getNurseExceptBinding: WxApiRoot + "consult/get_nurseExceptBinding", //根据编号获取所有护士信息(除了绑定)带头像
  getAllNurse: WxApiRoot + "consult/get_AllNurse", //获取所有护士信息带头像
  getAllSpecialist: WxApiRoot + "consult/get_Specialist", //获取所有外院专家信息带头像

  timeout: 87600,
  accesskey: "d3b9892bcffdf704ac611babf8bb2b7f",
  accessid: "wx6f580062bbe78653",
};
