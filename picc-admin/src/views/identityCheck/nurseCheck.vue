<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>身份审核</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">护士身份</span></el-breadcrumb-item
      >
    </el-breadcrumb>
    <el-tabs v-model="activeName" @tab-click="tabChange">
      <el-tab-pane label="本院职工" name="first">
        <el-input
          placeholder="请输入用户名"
          class="input-with-select search-input"
          v-model="query1"
          @keydown.native.enter="initList"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="initList"
          ></el-button>
        </el-input>
        <el-table
          :data="nurseList.slice((pagenum - 1) * pagesize, pagenum * pagesize)"
          border
          v-loading="loading"
          height="calc(100% - 110px)"
          :default-sort="{ prop: 'state', order: 'ascending' }"
        >
          <el-table-column
            prop="jobnumber"
            label="工号"
            width="120"
            sortable
          ></el-table-column>
          <el-table-column
            prop="username"
            label="姓名"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="sex"
            label="性别"
            width="120"
          ></el-table-column>
          <el-table-column prop="phone" label="电话" width="180">
          </el-table-column>
          <el-table-column
            prop="department"
            label="科室"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="create_time"
            label="申请时间"
            width="180"
          ></el-table-column>
          <el-table-column prop="state" label="审核状态"></el-table-column>
          <el-table-column label="审核操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="nurseCheck(scope.row)"
                v-if="scope.row.status == 0"
                >审核</el-button
              >
              <el-button
                type="text"
                style="color: red"
                @click="nurseDelete(scope.row)"
                v-if="scope.row.status == -1"
                >删除</el-button
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
        <!-- 审核护士框 -->
        <el-dialog title="审核用户" :visible.sync="editDialogFormVisible1">
          <el-form :model="editForm1" :label-width="formLabelWidth">
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="editForm1.username"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="工号" prop="jobnumber">
              <el-input
                v-model="editForm1.jobnumber"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-input
                v-model="editForm1.sex"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input
                v-model="editForm1.phone"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="科室" prop="department">
              <el-input
                v-model="editForm1.department"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogFormVisible1 = false">取 消</el-button>
            <el-button type="danger" @click="noPassSubmit">不通过</el-button>
            <el-button type="primary" @click="passSubmit">通过</el-button>
          </div>
        </el-dialog>
      </el-tab-pane>
      <el-tab-pane label="外院专家" name="second">
        <el-input
          placeholder="请输入用户名"
          class="input-with-select search-input"
          v-model="query2"
          @keydown.native.enter="initSpecialistList"
        >
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="initSpecialistList"
          ></el-button>
        </el-input>
        <el-table
          :data="
            specialistList.slice((pagenum - 1) * pagesize, pagenum * pagesize)
          "
          border
          v-loading="loading"
          height="calc(100% - 110px)"
          :default-sort="{ prop: 'state', order: 'ascending' }"
        >
          <el-table-column
            prop="specialistNumber"
            label="编号"
            width="120"
            sortable
          ></el-table-column>
          <el-table-column
            prop="username"
            label="姓名"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="sex"
            label="性别"
            width="120"
          ></el-table-column>
          <el-table-column prop="phone" label="电话" width="180">
          </el-table-column>
          <el-table-column
            prop="workUnit"
            label="工作单位"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="create_time"
            label="申请时间"
            width="180"
          ></el-table-column>
          <el-table-column prop="state" label="审核状态"></el-table-column>
          <el-table-column label="审核操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="specialistCheck(scope.row)"
                v-if="scope.row.status == 0"
                >审核</el-button
              >
              <el-button
                type="text"
                style="color: red"
                @click="specialistDelete(scope.row)"
                v-if="scope.row.status == -1"
                >删除</el-button
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
        <!-- 审核护士框 -->
        <el-dialog title="审核用户" :visible.sync="editDialogFormVisible2">
          <el-form :model="editForm2" :label-width="formLabelWidth">
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="editForm2.username"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-input
                v-model="editForm2.sex"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input
                v-model="editForm2.phone"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="工作单位" prop="workUnit">
              <el-input
                v-model="editForm2.workUnit"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogFormVisible2 = false">取 消</el-button>
            <el-button type="danger" @click="noPassSubmitSpecialist"
              >不通过</el-button
            >
            <el-button type="primary" @click="passSubmitSpecialist"
              >通过</el-button
            >
          </div>
        </el-dialog>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {
  getNurseList,
  changeNurseState,
  getNurseById,
  deleteNurse
} from '@/api/nurses';
import {
  getSpecialistList,
  changeSpecialistState,
  getSpecialistById,
  deleteSpecialist
} from '@/api/specialists';
import { formatDate } from '@/utils/index';

export default {
  data() {
    return {
      loading: true,
      nurseList: [],
      total: 0,
      pagesize: 10,
      pagenum: 1,
      query1: '',
      formLabelWidth: '80px',
      editDialogFormVisible1: false,
      editForm1: {
        username: '',
        jobnumber: '',
        sex: '',
        phone: '',
        department: '',
        id: ''
      },
      activeName: 'first',
      specialistList: [],
      query2: '',
      editDialogFormVisible2: false,
      editForm2: {
        username: '',
        sex: '',
        phone: '',
        workUnit: '',
        id: ''
      }
    };
  },
  created() {
    this.initList();
  },
  methods: {
    tabChange() {
      this.loading = true;
      this.editDialogFormVisible1 = false;
      this.editDialogFormVisible2 = false;
      if (this.activeName === 'first') {
        this.query1 = '';
        this.initList();
      } else {
        this.query2 = '';
        this.initSpecialistList();
      }
    },
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
      getNurseList({
        query: this.query1,
        pagenum: this.pagenum,
        type: 2
      }).then((res) => {
        this.nurseList = res.data.nurses;
        this.total = res.data.total;
        this.loading = false;
        for (let i = 0; i < this.nurseList.length; i++) {
          this.nurseList[i].create_time = formatDate(
            this.nurseList[i].create_time,
            'yyyy-MM-dd hh:mm:ss'
          );
        }
      });
    },
    // 根据id获取用户信息
    nurseCheck(row) {
      this.editDialogFormVisible1 = true;
      getNurseById({ id: row.id }).then((res) => {
        if (res.status === 200) {
          this.editForm1.username = res.data.username;
          this.editForm1.jobnumber = res.data.jobnumber;
          this.editForm1.sex = res.data.sex;
          this.editForm1.phone = res.data.phone;
          this.editForm1.department = res.data.department;
          this.editForm1.id = res.data.id;
        } else {
          this.$message({
            type: 'warning',
            message: '获取用户信息失败，请稍后尝试'
          });
        }
      });
    },
    nurseDelete(row) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteNurse({ id: row.id }).then((res) => {
            if (res.status === 200) {
              this.initList();
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            } else {
              this.$message({
                type: 'warning',
                message: res.msg
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },
    //审核不通过
    noPassSubmit() {
      changeNurseState({ id: this.editForm1.id, state: -1 }).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '成功修改审核信息'
          });
          this.editDialogFormVisible1 = false;
          this.initList();
        } else {
          this.$message({
            type: 'warning',
            message: res.msg
          });
        }
      });
    },
    //审核通过
    passSubmit() {
      changeNurseState({ id: this.editForm1.id, state: 1 }).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '成功修改审核信息'
          });
          this.editDialogFormVisible1 = false;
          this.initList();
        } else {
          this.$message({
            type: 'warning',
            message: res.msg
          });
        }
      });
    },

    // 获取用户列表信息
    initSpecialistList() {
      getSpecialistList({
        query: this.query2,
        pagenum: this.pagenum,
        type: 2
      }).then((res) => {
        this.specialistList = res.data.specialists;
        this.total = res.data.total;
        this.loading = false;
        for (let i = 0; i < this.specialistList.length; i++) {
          this.specialistList[i].create_time = formatDate(
            this.specialistList[i].create_time,
            'yyyy-MM-dd hh:mm:ss'
          );
        }
      });
    },
    // 根据id获取用户信息
    specialistCheck(row) {
      this.editDialogFormVisible2 = true;
      getSpecialistById({ id: row.id }).then((res) => {
        if (res.status === 200) {
          this.editForm2.username = res.data.username;
          this.editForm2.sex = res.data.sex;
          this.editForm2.phone = res.data.phone;
          this.editForm2.workUnit = res.data.workUnit;
          this.editForm2.id = res.data.id;
        } else {
          this.$message({
            type: 'warning',
            message: '获取用户信息失败，请稍后尝试'
          });
        }
      });
    },
    specialistDelete(row) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteSpecialist({ id: row.id }).then((res) => {
            if (res.status === 200) {
              this.initSpecialistList();
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            } else {
              this.$message({
                type: 'warning',
                message: res.msg
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },
    //审核不通过
    noPassSubmitSpecialist() {
      changeSpecialistState({ id: this.editForm2.id, state: -1 }).then(
        (res) => {
          if (res.status === 200) {
            this.$message({
              type: 'success',
              message: '成功修改审核信息'
            });
            this.editDialogFormVisible2 = false;
            this.initSpecialistList();
          } else {
            this.$message({
              type: 'warning',
              message: res.msg
            });
          }
        }
      );
    },
    //审核通过
    passSubmitSpecialist() {
      changeSpecialistState({ id: this.editForm2.id, state: 1 }).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '成功修改审核信息'
          });
          this.editDialogFormVisible2 = false;
          this.initSpecialistList();
        } else {
          this.$message({
            type: 'warning',
            message: res.msg
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.search-input {
  width: 300px;
  margin-bottom: 15px;
}
.page {
  padding: 5px;
  background-color: #d3dce6;
  margin: 10px 0;
}
.el-tabs {
  height: calc(100% - 60px);
  /deep/.el-tabs__content {
    height: calc(100% - 50px);
    .el-tab-pane {
      height: 100%;
    }
  }
}
</style>
