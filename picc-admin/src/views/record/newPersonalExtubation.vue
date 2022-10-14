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
        ><span class="currentpage">个人拔管记录</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <div style="text-align: right; margin-right: 100px">
      <button v-print="'#printMe'" v-if="total != 0">打印</button>
    </div>
    <div class="box" v-if="total == 0">暂无记录</div>
    <div class="box" v-else id="printMe">
      <div>
        <span class="color999">患者姓名：</span
        >{{ patientExtubation.patientname }}
      </div>
      <div>
        <span class="color999">档案编号：</span
        >{{ patientExtubation.record_num }}
      </div>
      <div>
        <span class="color999">拔管编号：</span
        >{{ patientExtubation.extubation_num }}
      </div>
      <div>
        <span class="color999">拔管日期：</span
        >{{ patientExtubation.extubation_date }}
      </div>
      <div>
        <span class="color999">导管留置时间：</span
        >{{ patientExtubation.indwelling_time }}天
      </div>
      <div>
        <span class="color999">导管末端是否完整：</span
        >{{ patientExtubation.indwelling_time }}
      </div>
      <div>
        <span class="color999">拔管过程是否顺利：</span
        >{{ patientExtubation.process_smooth }}
      </div>
      <div>
        <span class="color999">拔除原因：</span>{{ patientExtubation.reason }}
      </div>
    </div>
  </div>
</template>

<script>
import { getPatientExtubationNew } from '@/api/record';
import { formatDate } from '@/utils/index';

export default {
  data() {
    return {
      loading: true,
      total: 0,
      pagesize: 10,
      pagenum: 1,
      patientExtubation: {}
    };
  },
  created() {
    this.initList();
  },
  methods: {
    initList() {
      getPatientExtubationNew({ recordNum: this.$route.query.recordNum }).then(
        (res) => {
          let list = res.data;
          if (res.data.length !== 0) {
            for (let i = 0; i < res.data.length; i++) {
              list[i].extubation_date = formatDate(
                res.data[i].extubation_date,
                'yyyy-MM-dd'
              );
              list[i].end_intact = list[i].end_intact === 'false' ? '否' : '是';
              list[i].process_smooth =
                list[i].process_smooth === 'false' ? '否' : '是';
            }
            this.total = res.data.length;
            this.patientExtubation = list[0];
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
.box {
  min-width: 600px;
  background: #f0f0f0;
  box-shadow: 10px 5px 5px #c1c1c166;
  margin: 20px 100px;
  text-align: center;
  font-size: 18px;
  padding: 30px 60px;
  line-height: 60px;
}
</style>
