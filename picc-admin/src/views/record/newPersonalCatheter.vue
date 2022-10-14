<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>记录查看</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/record/record1' }"
        ><span class="currentpage">档案记录</span></el-breadcrumb-item
      >
      <el-breadcrumb-item
        ><span class="currentpage">个人档案记录</span></el-breadcrumb-item
      >
      <el-breadcrumb-item
        ><span class="currentpage">个人置管记录</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <div style="text-align: right; margin-right: 100px">
      <button v-print="'#printMe'">打印</button>
    </div>
    <div class="flex flex-v box" id="printMe">
      <el-row class="head">
        <el-col :span="24"> 置管信息表 </el-col>
      </el-row>
      <el-row>
        <el-col :span="6" class="color999">患者姓名：</el-col>
        <el-col :span="6">{{ patientCatheter.patientname }}</el-col>
        <el-col :span="6" class="color999">置管编号：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_num }}</el-col>
        <el-col :span="6" class="color999">档案编号：</el-col>
        <el-col :span="6">{{ patientCatheter.record_num }}</el-col>
        <el-col :span="6" class="color999">疾病类型：</el-col>
        <el-col :span="6">{{ patientCatheter.disease_type }}</el-col>
        <el-col :span="6" class="color999">是否首次置管：</el-col>
        <el-col :span="6">{{
          patientCatheter.first_catheter == 'true' ? '是' : '否'
        }}</el-col>
        <el-col :span="6" class="color999">置管科室：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_department }}</el-col>
        <el-col :span="6" class="color999">置管时间：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_time }}</el-col>
        <el-col :span="6" class="color999">化疗次数：</el-col>
        <el-col :span="6">{{ patientCatheter.chemotherapy_number }}</el-col>
        <el-col :span="6" class="color999">置管时机：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_opportunity }}</el-col>
        <el-col :span="6" class="color999">导管型号：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_model }}</el-col>
        <el-col :span="6" class="color999">导管规格：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_specification }}</el-col>
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
        <el-col :span="6">{{ patientCatheter.catheter_exposed_length }}</el-col>
        <el-col :span="6" class="color999">臂围：</el-col>
        <el-col :span="6">{{ patientCatheter.arm_circumference }}</el-col>
        <el-col :span="6" class="color999">导管品牌：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_brand }}</el-col>
        <el-col :span="6" class="color999">第几次置管：</el-col>
        <el-col :span="6">{{
          patientCatheter.number_of_catheterization
        }}</el-col>
        <el-col :span="6" class="color999">置管医院：</el-col>
        <el-col :span="6">{{ patientCatheter.catheter_hospital }}</el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getPatientCatheterNew } from '@/api/record';
import { formatDate } from '@/utils/index';

export default {
  data() {
    return {
      patientCatheter: {},
      loading: true,
      total: 0,
      pagesize: 10,
      pagenum: 1
    };
  },
  created() {
    this.initList();
  },
  methods: {
    handleSizeChange(val) {
      this.pagesize = val;
    },
    handleCurrentChange(val) {
      this.pagenum = val;
    },
    initList() {
      getPatientCatheterNew({ recordNum: this.$route.query.recordNum }).then(
        (res) => {
          let list = res.data;
          if (res.data.length !== 0) {
            for (let i = 0; i < res.data.length; i++) {
              list[i].catheter_time = formatDate(
                res.data[i].catheter_time,
                'yyyy-MM-dd'
              );
              list[i].first_catheter =
                list[i].first_catheter === 'false' ? '否' : '是';
            }
            this.total = res.data.length;
            this.patientCatheter = list[0];
          }
          this.loading = false;
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
  padding: 16px 0 16px 0;
  margin-bottom: 16px;
  font-size: 30px;
}
.color999 {
  color: #999;
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

.box {
  min-width: 600px;
  background: #f0f0f0;
  box-shadow: 10px 5px 5px #c1c1c166;
  margin: 20px 100px;
  text-align: center;
  font-size: 18px;
  padding: 60px;
  line-height: 40px;
}
</style>
