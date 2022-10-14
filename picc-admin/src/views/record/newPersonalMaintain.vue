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
        ><span class="currentpage">个人维护记录</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <el-button @click="print">打印</el-button>
    <div class="flex flex-v catheterCard">
      <el-row class="head">
        <el-col :span="24"> 维护记录表 </el-col>
      </el-row>
      <el-table
        :data="
          patientMaintain.slice((pagenum - 1) * pagesize, pagenum * pagesize)
        "
        border
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column
          prop="patientnumber"
          label="患者编号"
          width="120"
          sortable
        ></el-table-column>
        <el-table-column
          prop="patientname"
          label="患者姓名"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="maintain_num"
          label="维护编号"
          width="120"
        ></el-table-column>
        <el-table-column
          prop="nursename"
          label="维护医生"
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
        <el-table-column
          prop="blood_return"
          label="是否有回血"
          width="150"
        ></el-table-column>
        <el-table-column
          prop="catheter_exposed_length"
          label="导管外露长度"
          width="150"
        ></el-table-column>
        <el-table-column prop="resistance" label="有无阻力" width="150">
        </el-table-column>
        <el-table-column prop="companion" label="并发症" width="150">
        </el-table-column>
        <el-table-column prop="treatment_process" label="处理方法">
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="1"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { getPatientMaintainNew } from '@/api/record';
import { formatDate } from '@/utils/index';
// import print from "print-js";

export default {
  data() {
    return {
      total: 0,
      pagesize: 10,
      pagenum: 1,
      loading: true,
      patientInfo: {},
      patientMaintain: [],
      patientname: '',
      recordNum: ''
    };
  },
  created() {
    this.initList();
    this.patientname = this.$route.query.patientname;
    this.recordNum = this.$route.query.recordNum;
  },
  methods: {
    handleSizeChange(val) {
      this.pagesize = val;
    },
    handleCurrentChange(val) {
      this.pagenum = val;
    },
    initList() {
      getPatientMaintainNew({ recordNum: this.$route.query.recordNum }).then(
        (res) => {
          this.patientMaintain = res.data;
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
            this.patientMaintain[i].blood_return =
              this.patientMaintain[i].blood_return === 'true' ? '有' : '无';
            this.patientMaintain[i].companion =
              this.patientMaintain[i].companion === 'false'
                ? '无'
                : this.patientMaintain[i].companion;
          }
          this.loading = false;
          this.total = this.patientMaintain.length;
        }
      );
    },
    print() {
      // eslint-disable-next-line no-undef
      printJS({
        printable: this.patientMaintain,
        properties: [
          {
            field: 'maintain_num',
            displayName: '维护编号',
            columnSize: 1
          },
          {
            field: 'nursename',
            displayName: '维护医生',
            columnSize: 1
          },
          {
            field: 'maintain_date',
            displayName: '维护日期',
            columnSize: 1
          },
          {
            field: 'pipeline_unobstructed',
            displayName: '管路是否通畅',
            columnSize: 1
          },
          {
            field: 'change_dressing',
            displayName: '是否更换敷料',
            columnSize: 1
          },
          {
            field: 'replace_connector',
            displayName: '是否更换接头',
            columnSize: 1
          },
          {
            field: 'blood_return',
            displayName: '是否有回血',
            columnSize: 1
          },
          {
            field: 'catheter_exposed_length',
            displayName: '导管外露长度',
            columnSize: 1
          },
          {
            field: 'resistance',
            displayName: '有无阻力',
            columnSize: 1
          },
          {
            field: 'companion',
            displayName: '并发症',
            columnSize: 1
          },
          {
            field: 'treatment_process',
            displayName: '处理方法',
            columnSize: 1
          }
        ],
        type: 'json',
        header: `<h2> 维护记录表 </h2><h4>患者姓名：${this.patientname}</h4><h4>档案编号：${this.recordNum}</h4>`,
        // 样式设置
        gridStyle: 'border: 2px solid #3971A5;',
        gridHeaderStyle: 'color: red;  border: 2px solid #3971A5;'
      });
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
