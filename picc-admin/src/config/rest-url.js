const CONFIG = window.CONFIG;

// rest请求配置
export default {
  manager: {
    requseLogin: CONFIG.SERVER + '/manager/manager_login',
    get_menulist: CONFIG.SERVER + '/manager/get_menulist',
    getManagerList: CONFIG.SERVER + '/manager/get_managers',
    changeManagerState: CONFIG.SERVER + '/manager/change_managerState',
    addManager: CONFIG.SERVER + '/manager/add_manager',
    getManagerById: CONFIG.SERVER + '/manager/get_managerById',
    editManager: CONFIG.SERVER + '/manager/edit_manager',
    updateManagerPassword: CONFIG.SERVER + '/manager/manager_updatePassword',
    deleteManager: CONFIG.SERVER + '/manager/delete_manager'
  },
  nurses: {
    getNurseList: CONFIG.SERVER + '/nurses/get_nurses',
    changeNurseState: CONFIG.SERVER + '/nurses/change_nurseState',
    getNurseById: CONFIG.SERVER + '/nurses/get_nurseById',
    editNurse: CONFIG.SERVER + '/nurses/edit_nurse',
    deleteNurse: CONFIG.SERVER + '/nurses/delete_nurse',
    getScheduleByJobnumber: CONFIG.SERVER + '/nurses/get_Schedule_by_jobnumber',
    postSchedule: CONFIG.SERVER + '/nurses/post_Schedule',
    getSchedule: CONFIG.SERVER + '/nurses/get_Schedule',
    deleteScheduleOne: CONFIG.SERVER + '/nurses/delete_ScheduleOne',
    getScheduleByTime: CONFIG.SERVER + '/nurses/get_ScheduleByTime'
  },
  specialists: {
    getSpecialistList: CONFIG.SERVER + '/specialists/get_specialists',
    changeSpecialistState:
      CONFIG.SERVER + '/specialists/change_specialistState',
    getSpecialistById: CONFIG.SERVER + '/specialists/get_specialistById',
    editSpecialist: CONFIG.SERVER + '/specialists/edit_specialist',
    deleteSpecialist: CONFIG.SERVER + '/specialists/delete_specialist'
  },
  patients: {
    getPatientList: CONFIG.SERVER + '/patients/get_patients',
    getPatientById: CONFIG.SERVER + '/patients/get_patientById',
    editPatient: CONFIG.SERVER + '/patients/edit_patient',
    deletePatient: CONFIG.SERVER + '/patients/delete_patient'
  },
  binding: {
    getBindingList: CONFIG.SERVER + '/binding/get_binding',
    getBindingById: CONFIG.SERVER + '/binding/get_bindingById',
    editBinding: CONFIG.SERVER + '/binding/edit_binding',
    deleteBinding: CONFIG.SERVER + '/binding/delete_binding'
  },
  record: {
    getPatientsRecord: CONFIG.SERVER + '/record/get_patientsRecord',
    getPatientRecord: CONFIG.SERVER + '/record/get_patientRecord',
    getPatientCatheterNew: CONFIG.SERVER + '/record/get_patientCatheterNew',
    getPatientMaintainNew: CONFIG.SERVER + '/record/get_patientMaintainNew',
    getPatientExtubationNew: CONFIG.SERVER + '/record/get_patientExtubationNew',
    getPatientsCatheter: CONFIG.SERVER + '/record/get_patientsCatheter',
    getPatientCatheter: CONFIG.SERVER + '/record/get_patientCatheter',
    getPatientsMaintain: CONFIG.SERVER + '/record/get_patientsMaintain',
    getPatientMaintain: CONFIG.SERVER + '/record/get_patientMaintain',
    getPatientsExtubation: CONFIG.SERVER + '/record/get_patientsExtubation',
    getPatientExtubation: CONFIG.SERVER + '/record/get_patientExtubation'
  },
  opinion: {
    getOpinion: CONFIG.SERVER + '/opinion/get_opinion',
    updateOpinion: CONFIG.SERVER + '/opinion/update_opinion',
    changeOpinionState: CONFIG.SERVER + '/opinion/change_opinionState'
  },
  statistic: {
    getStatisticCatheter: CONFIG.SERVER + '/statistic/get_statisticCatheter',
    getStatisticMaintain: CONFIG.SERVER + '/statistic/get_statisticMaintain',
    getStatisticExtubation:
      CONFIG.SERVER + '/statistic/get_statisticExtubation',
    getStatisticCompanion: CONFIG.SERVER + '/statistic/get_statisticCompanion'
  },
  common: {
    getDepartment: CONFIG.SERVER + '/common/get_department'
  }
};
