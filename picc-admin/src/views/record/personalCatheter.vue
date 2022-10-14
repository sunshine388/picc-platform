<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>记录查看</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: `Catheter` }"
        ><span class="currentpage">置管记录</span></el-breadcrumb-item
      >
      <el-breadcrumb-item
        ><span class="currentpage">个人置管记录</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <div class="flex">
      <div class="idcard">
        <el-row class="head">
          <el-col :span="8" class="color999">
            <el-avatar
              :size="80"
              src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
            >
            </el-avatar>
          </el-col>
          <el-col :span="16">
            <div>病人名</div>
            <span>片</span>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="color999">患者编号：</el-col>
          <el-col :span="16">{{ patientInfo.patientnumber }}</el-col>
          <el-col :span="8" class="color999">姓名：</el-col>
          <el-col :span="16">{{ patientInfo.username }}</el-col>
          <el-col :span="8" class="color999">性别：</el-col>
          <el-col :span="16">{{ patientInfo.sex }}</el-col>
          <el-col :span="8" class="color999">年龄：</el-col>
          <el-col :span="16">{{ patientInfo.age }}</el-col>
          <el-col :span="8" class="color999">出生日期：</el-col>
          <el-col :span="16">{{ patientInfo.birthday }}</el-col>
          <el-col :span="8" class="color999">所在城市：</el-col>
          <el-col :span="16">{{ patientInfo.city }}</el-col>
          <el-col :span="8" class="color999">文化程度：</el-col>
          <el-col :span="16">{{ patientInfo.culture }}</el-col>
          <el-col :span="8" class="color999">电话号码：</el-col>
          <el-col :span="16">{{ patientInfo.phone }}</el-col>
        </el-row>
      </div>
      <div class="flex flex-v catheterCard">
        <el-row class="head">
          <el-col :span="24"> 置管信息表 </el-col>
        </el-row>
        <el-row>
          <el-col :span="6" class="color999">疾病类型：</el-col>
          <el-col :span="6">{{ patientCatheter.disease_type }}</el-col>
          <el-col :span="6" class="color999">是否首次置管：</el-col>
          <el-col :span="6">{{
            patientCatheter.first_catheter == 'true' ? '是' : '否'
          }}</el-col>
          <el-col :span="6" class="color999">置管科室：</el-col>
          <el-col :span="4">{{ patientCatheter.catheter_department }}</el-col>
          <el-col :span="6" class="color999">置管时间：</el-col>
          <el-col :span="8">{{ patientCatheter.catheter_time }}</el-col>
          <el-col :span="6" class="color999">化疗次数：</el-col>
          <el-col :span="6">{{ patientCatheter.chemotherapy_number }}</el-col>
          <el-col :span="6" class="color999">置管时机：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_opportunity }}</el-col>
          <el-col :span="6" class="color999">导管型号：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_model }}</el-col>
          <el-col :span="6" class="color999">导管规格：</el-col>
          <el-col :span="6">{{
            patientCatheter.catheter_specification
          }}</el-col>
          <el-col :span="6" class="color999">导管批号：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_batchnumber }}</el-col>
          <el-col :span="6" class="color999">导管长度：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_length }}</el-col>
          <el-col :span="6" class="color999">穿刺方法：</el-col>
          <el-col :span="6">{{ patientCatheter.puncture_method }}</el-col>
          <el-col :span="6" class="color999">置管部位：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_site }}</el-col>
          <el-col :span="6" class="color999">置管手臂：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_arm }}</el-col>
          <el-col :span="6" class="color999">置管静脉：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_vein }}</el-col>
          <el-col :span="6" class="color999">导管尖端位置：</el-col>
          <el-col :span="6">{{ patientCatheter.catheter_tip_position }}</el-col>
          <el-col :span="6" class="color999">导管插入长度：</el-col>
          <el-col :span="6">{{
            patientCatheter.catheter_insertion_length
          }}</el-col>
          <el-col :span="6" class="color999">导管外露长度：</el-col>
          <el-col :span="6">{{
            patientCatheter.catheter_exposed_length
          }}</el-col>
          <el-col :span="6" class="color999">臂围：</el-col>
          <el-col :span="6">{{ patientCatheter.arm_circumference }}</el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { getPatientCatheter } from '@/api/record';
import { getPatientById } from '@/api/patients';
import { formatDate, getAge } from '@/utils/index';

export default {
  data() {
    return {
      patientInfo: {},
      patientCatheter: {}
    };
  },
  created() {
    this.initList();
  },
  methods: {
    initList() {
      getPatientById({ id: this.$route.query.id }).then((res) => {
        this.patientInfo = res.data;
        this.patientInfo.age = getAge(this.patientInfo.birthday);
      });
      getPatientCatheter({ patientnum: this.$route.query.patientnumber }).then(
        (res) => {
          this.patientCatheter = res.data;
          this.patientCatheter.catheter_time = formatDate(
            this.patientCatheter.catheter_time,
            'yyyy-MM-dd hh:mm:ss'
          );
        }
      );
    }
  }
};
</script>

<style scoped>
.idcard {
  padding: 20px;
  width: 300px;
  min-width: 280px;
  background: #f0f0f0;
  text-align: center;
  line-height: 40px;
  font-size: 18px;
  box-shadow: 10px 5px 5px #c1c1c166;
}
.head {
  border-bottom: 1px solid #c1c1c166;
  padding: 20px 0 20px 0;
  margin-bottom: 20px;
  font-size: 30px;
}
.color999 {
  color: #999;
}
.catheterCard {
  padding: 0 20px 20px 20px;
  width: 800px;
  min-width: 600px;
  background: #f0f0f0;
  box-shadow: 10px 5px 5px #c1c1c166;
  margin-left: 40px;
  text-align: center;
  line-height: 40px;
  font-size: 18px;
}
.page {
  padding: 5px;
  background-color: #d3dce6;
  margin: 10px 0;
}
.flex {
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
}
.flex-v {
  -webkit-box-orient: vertical;
  -webkit-flex-direction: column;
  -ms-flex-direction: column;
  flex-direction: column;
}
</style>
