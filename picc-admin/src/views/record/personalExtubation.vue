<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>记录查看</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: `extubation` }"
        ><span class="currentpage">拔管记录</span></el-breadcrumb-item
      >
      <el-breadcrumb-item
        ><span class="currentpage">个人拔管记录</span></el-breadcrumb-item
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
          <el-col :span="24" class="color999"> 拔管记录表 </el-col>
        </el-row>
        <div style="padding: 60px; line-height: 80px">
          <div>
            <span class="color999">拔管日期：</span
            >{{ patientExtubation.extubation_date }}
          </div>
          <div>
            <span class="color999">导管留置时间：</span
            >{{ patientExtubation.indwelling_time }}天
          </div>
          <div>
            <span class="color999">拔除原因：</span
            >{{ patientExtubation.reason }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPatientExtubation } from '@/api/record';
import { getPatientById } from '@/api/patients';
import { formatDate, getAge } from '@/utils/index';

export default {
  data() {
    return {
      patientInfo: {},
      patientExtubation: {}
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
      getPatientExtubation({
        patientnum: this.$route.query.patientnumber
      }).then((res) => {
        this.patientExtubation = res.data;
        this.patientExtubation.extubation_date = formatDate(
          this.patientExtubation.extubation_date,
          'yyyy-MM-dd'
        );
      });
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
