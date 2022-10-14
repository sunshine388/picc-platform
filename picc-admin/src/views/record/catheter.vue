<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>记录查看</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">置管记录</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <el-input
      placeholder="请输入用户名"
      class="input-with-select search-input"
      v-model="query"
      @keydown.native.enter="initList"
    >
      <el-button
        slot="append"
        icon="el-icon-search"
        @click="initList"
      ></el-button>
    </el-input>
    <el-table
      :data="patientList.slice((pagenum - 1) * pagesize, pagenum * pagesize)"
      border
      v-loading="loading"
      height="calc(100% - 170px)"
    >
      <el-table-column
        prop="patientnumber"
        label="患者编号"
        width="120"
        sortable
      ></el-table-column>
      <el-table-column
        prop="username"
        label="姓名"
        width="150"
      ></el-table-column>
      <el-table-column prop="sex" label="性别" width="120"></el-table-column>
      <el-table-column prop="age" label="年龄" width="120"></el-table-column>
      <el-table-column
        prop="birthday"
        label="出生日期"
        width="150"
      ></el-table-column>
      <el-table-column
        prop="culture"
        label="文化程度"
        width="150"
      ></el-table-column>
      <el-table-column prop="city" label="居住城市" width="150">
      </el-table-column>
      <el-table-column prop="phone" label="联系电话"> </el-table-column>
      <el-table-column label="置管信息查看" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button type="text" @click="patientJump(scope.row)"
            >点击查看置管信息</el-button
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
</template>

<script>
import { getPatientsCatheter } from '@/api/record';
import { getAge } from '@/utils/index';

export default {
  data() {
    return {
      loading: 'true',
      patientList: [],
      total: 0,
      pagesize: 10,
      pagenum: 1,
      query: '',
      num: 10,
      formLabelWidth: '80px',
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
        birthday: [{ required: true, message: '出生日期不能为空' }],
        phone: [{ required: true, message: '电话不能为空' }],
        culture: [{ required: true, message: '请选择文化程度' }],
        city: [{ required: true, message: '请选择居住城市' }]
      },
      editForm: {
        username: '',
        sex: '',
        birthday: '',
        age: '',
        phone: '',
        culture: '',
        city: '',
        id: ''
      },
      editDialogFormVisible: false,
      grantForm: {},
      grantDialogFormVisible: false,
      roleId: '',
      roleList: []
    };
  },
  created() {
    this.initList();
  },
  methods: {
    handleSizeChange(val) {
      this.pagesize = val;
      this.initList();
    },
    handleCurrentChange(val) {
      this.pagenum = val;
      this.initList();
    },
    // 获取用户列表信息
    initList() {
      getPatientsCatheter({
        query: this.query,
        pagenum: this.pagenum,
        pagesize: this.pagesize
      }).then((res) => {
        this.patientList = res.data.patientsCatheter;
        for (let i = 0; i < this.patientList.length; i++) {
          this.patientList[i].age = getAge(this.patientList[i].birthday);
        }
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 跳转
    patientJump(row) {
      this.$router.push({
        path: '/record/personalCatheter',
        query: {
          id: row.id,
          patientnumber: row.patientnumber
        }
      });
    }
  }
};
</script>

<style scoped>
.search-input {
  width: 300px;
  margin-bottom: 15px;
}
.page {
  padding: 5px;
  background-color: #d3dce6;
  margin: 10px 0;
}
</style>
