<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>记录查看</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ path: '/record/record1' }"
        ><span class="currentpage">档案记录</span></el-breadcrumb-item
      >
      <el-breadcrumb-item
        ><span class="currentpage">个人档案记录</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <div class="flex">
      <div class="idcard">
        <el-row class="head">
          <el-col :span="8" class="color999">
            <el-avatar
              :size="72"
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
          <el-col :span="10" class="color999">患者编号：</el-col>
          <el-col :span="14">{{ patientInfo.patientnumber }}</el-col>
          <el-col :span="10" class="color999">姓名：</el-col>
          <el-col :span="14">{{ patientInfo.username }}</el-col>
          <el-col :span="10" class="color999">性别：</el-col>
          <el-col :span="14">{{ patientInfo.sex }}</el-col>
          <el-col :span="10" class="color999">年龄：</el-col>
          <el-col :span="14">{{ patientInfo.age }}</el-col>
          <el-col :span="10" class="color999">出生日期：</el-col>
          <el-col :span="14">{{ patientInfo.birthday }}</el-col>
          <el-col :span="10" class="color999">所在城市：</el-col>
          <el-col :span="14">{{ patientInfo.city }}</el-col>
          <el-col :span="10" class="color999">详细地址</el-col>
          <el-col :span="14">{{ patientInfo.address }}</el-col>
          <el-col :span="10" class="color999">文化程度：</el-col>
          <el-col :span="14">{{ patientInfo.culture }}</el-col>
          <el-col :span="10" class="color999">电话号码：</el-col>
          <el-col :span="14">{{ patientInfo.phone }}</el-col>

          <el-col :span="10" class="color999">导管类型:</el-col>
          <el-col :span="14">{{ patientInfo.catheterType }}</el-col>
          <el-col :span="10" class="color999">体温：</el-col>
          <el-col :span="14">{{ patientInfo.animalHeat }}</el-col>
          <el-col :span="10" class="color999">既往史：</el-col>
          <el-col :span="14">{{ patientInfo.drugAllergyHistory }}</el-col>
          <el-col :span="10" class="color999">药物过敏史：</el-col>
          <el-col :span="14">{{ patientInfo.previousHistory }}</el-col>
        </el-row>
      </div>
      <div>
        <el-table
          :data="recordList.slice((pagenum - 1) * pagesize, pagenum * pagesize)"
          border
          v-loading="loading"
          style="width: 100%"
        >
          <el-table-column
            prop="recordNum"
            label="档案编号"
            width="150"
            sortable
          ></el-table-column>
          <el-table-column
            prop="ctime"
            label="档案生成日期"
            width="150"
            sortable
          ></el-table-column>
          <el-table-column label="置管信息查看" width="150">
            <template slot-scope="scope">
              <el-button type="text" @click="catheterJump(scope.row)"
                >点击查看置管信息</el-button
              >
            </template>
          </el-table-column>
          <el-table-column label="维护信息查看" width="150">
            <template slot-scope="scope">
              <el-button type="text" @click="maintainJump(scope.row)"
                >点击查看维护信息</el-button
              >
            </template>
          </el-table-column>
          <el-table-column label="拔管信息查看" width="150">
            <template slot-scope="scope">
              <el-button type="text" @click="extubationJump(scope.row)"
                >点击查看拔管信息</el-button
              >
            </template>
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
  </div>
</template>

<script>
import { getPatientRecord } from '@/api/record';
import { getPatientById } from '@/api/patients';
import { formatDate, getAge } from '@/utils/index';

export default {
  data() {
    return {
      loading: true,
      patientInfo: {},
      recordList: [],
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
      getPatientById({ id: this.$route.query.id }).then((res) => {
        this.patientInfo = res.data;
        this.patientInfo.age = getAge(this.patientInfo.birthday);
      });
      getPatientRecord({ patientnum: this.$route.query.patientnumber }).then(
        (res) => {
          this.recordList = res.data.record;
          for (let i = 0; i < res.data.record.length; i++) {
            this.recordList[i].ctime = formatDate(
              this.recordList[i].ctime,
              'yyyy-MM-dd'
            );
          }
          this.total = res.data.record.length;
          this.patientInfo.animalHeat = res.data.animalHeat;
          this.patientInfo.catheterType = res.data.catheterType;
          this.patientInfo.drugAllergyHistory = res.data.drugAllergyHistory;
          this.patientInfo.previousHistory = res.data.previousHistory;
          this.loading = false;
        }
      );
    },
    catheterJump(row) {
      this.$router.push({
        path: '/record/newPersonalCatheter',
        query: {
          id: row.id,
          recordNum: row.recordNum
        }
      });
    },
    maintainJump(row) {
      this.$router.push({
        path: '/record/newPersonalMaintain',
        query: {
          id: row.id,
          recordNum: row.recordNum,
          patientname: row.patientname
        }
      });
    },
    extubationJump(row) {
      this.$router.push({
        path: '/record/newPersonalExtubation',
        query: {
          id: row.id,
          recordNum: row.recordNum
        }
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
  line-height: 34px;
  font-size: 18px;
  box-shadow: 10px 5px 5px #c1c1c166;
  margin-right: 60px;
}
.head {
  border-bottom: 1px solid #c1c1c166;
  padding: 20px 0 20px 0;
  margin-bottom: 20px;
  font-size: 28px;
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
