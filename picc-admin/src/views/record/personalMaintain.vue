<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>记录查看</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: `maintain` }"
        ><span class="currentpage">维护记录</span></el-breadcrumb-item
      >
      <el-breadcrumb-item
        ><span class="currentpage">个人维护记录</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <div class="idcard">
      <el-row>
        <el-col :span="2" class="color999">
          <el-avatar
            :size="80"
            src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
          >
          </el-avatar>
        </el-col>
        <el-col :span="2">
          <div>病人</div>
          <span>名片</span>
        </el-col>
        <el-col :span="2" class="color999 flex flex-v"
          ><span>患者编号：</span><span>姓名：</span></el-col
        >
        <el-col :span="2" class="flex flex-v"
          ><span>{{ patientInfo.patientnumber }}</span
          ><span>{{ patientInfo.username }}</span></el-col
        >
        <el-col :span="2" class="color999 flex flex-v border-left"
          ><span>性别：</span><span>所在城市：</span></el-col
        >
        <el-col :span="2" class="flex flex-v"
          ><span>{{ patientInfo.sex }}</span
          ><span>{{ patientInfo.city }}</span></el-col
        >
        <el-col :span="3" class="color999 flex flex-v border-left"
          ><span>年龄：</span><span>出生日期：</span></el-col
        >
        <el-col :span="3" class="flex flex-v"
          ><span>{{ patientInfo.age }}</span
          ><span>{{ patientInfo.birthday }}</span></el-col
        >
        <el-col :span="3" class="color999 flex flex-v border-left"
          ><span>文化程度：</span><span>电话号码：</span></el-col
        >
        <el-col :span="3" class="flex flex-v"
          ><span>{{ patientInfo.culture }}</span
          ><span>{{ patientInfo.phone }}</span></el-col
        >
      </el-row>
    </div>
    <div class="flex flex-v catheterCard">
      <el-row class="head">
        <el-col :span="24"> 维护信息表 </el-col>
      </el-row>
      <el-table :data="patientMaintain" border style="width: 100%">
        <el-table-column
          prop="patientnumber"
          label="患者编号"
          width="120"
          sortable
        ></el-table-column>
        <el-table-column
          prop="patientname"
          label="姓名"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="maintain_date"
          label="维护日期"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="pipeline_unobstructed"
          label="管路是否通畅"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="change_dressing"
          label="是否更换敷料"
          width="150"
        ></el-table-column>
        <el-table-column
          prop="replace_connector"
          label="是否更换接头"
          width="150"
        ></el-table-column>
        <el-table-column prop="resistance" label="有无阻力" width="150">
        </el-table-column>
        <el-table-column prop="companion" label="并发症" width="150">
        </el-table-column>
        <el-table-column prop="treatment_process" label="处理方法">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getPatientMaintain } from '@/api/record';
import { getPatientById } from '@/api/patients';
import { formatDate, getAge } from '@/utils/index';

export default {
  data() {
    return {
      total: 0,
      pagesize: 10,
      pagenum: 1,
      num: 10,
      patientInfo: {},
      patientMaintain: []
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
      getPatientMaintain({ patientnum: this.$route.query.patientnumber }).then(
        (res) => {
          this.patientMaintain = res.data.patientMaintain;
          for (let i = 0; i < this.patientMaintain.length; i++) {
            this.patientMaintain[i].maintain_date = formatDate(
              this.patientMaintain[i].maintain_date,
              'yyyy-MM-dd'
            );
            this.patientMaintain[i].pipeline_unobstructed =
              this.patientMaintain[i].pipeline_unobstructed === 'true'
                ? '是'
                : '否';
            this.patientMaintain[i].change_dressing =
              this.patientMaintain[i].change_dressing === 'true' ? '是' : '否';
            this.patientMaintain[i].replace_connector =
              this.patientMaintain[i].replace_connector === 'true'
                ? '是'
                : '否';
            this.patientMaintain[i].resistance =
              this.patientMaintain[i].resistance === 'true' ? '有' : '无';
            this.patientMaintain[i].companion =
              this.patientMaintain[i].companion === 'false'
                ? '无'
                : this.patientMaintain[i].companion;
          }
        }
      );
    }
  }
};
</script>

<style scoped>
.idcard {
  padding: 20px;
  min-width: 800px;
  background: #f0f0f0;
  text-align: center;
  line-height: 40px;
  font-size: 18px;
  margin-bottom: 20px;
  box-shadow: 10px 5px 5px #c1c1c166;
}
.head {
  border-bottom: 1px solid #c1c1c166;
  padding: 20px 0 20px 0;
  margin-bottom: 20px;
  font-size: 30px;
}
.border-left {
  border-left: 1px solid #c1c1c166;
}
.color999 {
  color: #999;
}
.catheterCard {
  padding: 0 20px 20px 20px;
  min-width: 600px;
  background: #f0f0f0;
  box-shadow: 10px 5px 5px #c1c1c166;
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
