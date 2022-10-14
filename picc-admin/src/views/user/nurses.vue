<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">护士列表</span></el-breadcrumb-item
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
        >
          <el-table-column
            prop="jobnumber"
            label="工号"
            width="180"
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
            width="180"
          ></el-table-column>
          <el-table-column prop="phone" label="电话" width="180">
          </el-table-column>
          <el-table-column
            prop="department"
            label="科室"
            width="180"
          ></el-table-column>
          <el-table-column prop="state" label="审核状态"></el-table-column>
          <el-table-column label="操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                icon="el-icon-edit"
                @click="showEditDialog(scope.row)"
              ></el-button>
              <el-button
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="deleteNurse(scope.row)"
              ></el-button>
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
        <!-- 编辑用户对话框 -->
        <el-dialog title="编辑用户" :visible.sync="editDialogFormVisible1">
          <el-form
            :model="editForm1"
            :label-width="formLabelWidth"
            ref="editNurseForm1"
            :rules="rules1"
          >
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
              ></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-select v-model="editForm1.sex" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input
                v-model="editForm1.phone"
                auto-complete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="科室" prop="department">
              <el-select v-model="editForm1.department" placeholder="请选择">
                <el-option
                  v-for="item in departmentList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogFormVisible1 = false">取 消</el-button>
            <el-button type="primary" @click="editNurseSubmit">确 定</el-button>
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
        >
          <el-table-column
            prop="specialistNumber"
            label="编号"
            width="180"
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
            width="180"
          ></el-table-column>
          <el-table-column prop="phone" label="电话" width="180">
          </el-table-column>
          <el-table-column
            prop="workUnit"
            label="工作单位"
            width="180"
          ></el-table-column>
          <el-table-column prop="state" label="审核状态"></el-table-column>
          <el-table-column label="操作" fixed="right" width="200">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                icon="el-icon-edit"
                @click="showEditSpecialistDialog(scope.row)"
              ></el-button>
              <el-button
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="deleteSpecialist(scope.row)"
              ></el-button>
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
        <!-- 编辑用户对话框 -->
        <el-dialog title="编辑用户" :visible.sync="editDialogFormVisible2">
          <el-form
            :model="editForm2"
            :label-width="formLabelWidth"
            ref="editNurseForm2"
            :rules="rules2"
          >
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="editForm2.username"
                auto-complete="off"
                :disabled="true"
              ></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-select v-model="editForm2.sex" placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="电话" prop="phone">
              <el-input
                v-model="editForm2.phone"
                auto-complete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="工作单位" prop="workUnit">
              <el-input
                v-model="editForm2.workUnit"
                auto-complete="off"
              ></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="editDialogFormVisible2 = false">取 消</el-button>
            <el-button type="primary" @click="editSpecialistSubmit"
              >确 定</el-button
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
  getNurseById,
  editNurse,
  deleteNurse
} from '@/api/nurses';
import { getDepartment } from '@/api/common';
import {
  getSpecialistList,
  getSpecialistById,
  editSpecialist,
  deleteSpecialist
} from '@/api/specialists';

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
      rules1: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        jobnumber: [{ required: true, message: '请输入工号', trigger: 'blur' }],
        sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
        phone: [{ required: true, message: '电话不能为空' }],
        department: [{ required: true, message: '科室不能为空' }]
      },
      editForm1: {
        username: '',
        jobnumber: '',
        sex: '',
        phone: '',
        department: '',
        id: ''
      },
      options: [
        {
          value: '选项1',
          label: '男'
        },
        {
          value: '选项2',
          label: '女'
        },
        {
          value: '选项3',
          label: '不明'
        }
      ],
      departmentList: [],
      editDialogFormVisible1: false,

      activeName: 'first',
      specialistList: [],
      query2: '',
      rules2: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
        phone: [{ required: true, message: '电话不能为空' }],
        workUnit: [{ required: true, message: '工作单位不能为空' }]
      },
      editForm2: {
        username: '',
        sex: '',
        phone: '',
        workUnit: '',
        id: ''
      },
      editDialogFormVisible2: false
    };
  },
  created() {
    this.initList();
    this.getDepartment();
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
    getDepartment() {
      getDepartment().then((res) => {
        if (res.status === 200) {
          for (let i = 0; i < res.data.length; i++) {
            let object = {};
            object.value = i;
            object.label = res.data[i];
            this.departmentList.push(object);
          }
        }
      });
    },
    // 获取用户列表信息
    initList() {
      getNurseList({
        query: this.query1,
        pagenum: this.pagenum,
        type: 1
      }).then((res) => {
        this.nurseList = res.data.nurses;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 根据id获取用户信息
    showEditDialog(row) {
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
    // 编辑用户
    editNurseSubmit() {
      this.$refs.editNurseForm1.validate((valide) => {
        if (valide) {
          editNurse(this.editForm1).then((res) => {
            if (res.status === 200) {
              this.initList();
              this.$message({
                type: 'success',
                message: '编辑成功'
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
        }
      });
    },
    // 删除用户
    deleteNurse(row) {
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

    // 获取用户列表信息
    initSpecialistList() {
      getSpecialistList({
        query: this.query2,
        pagenum: this.pagenum,
        type: 1
      }).then((res) => {
        this.specialistList = res.data.specialists;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 根据id获取用户信息
    showEditSpecialistDialog(row) {
      this.editDialogFormVisible2 = true;
      getSpecialistById({ id: row.id }).then((res) => {
        if (res.status === 200) {
          this.editForm2.username = res.data.username;
          this.editForm2.specialistNumber = res.data.specialistNumber;
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
    // 编辑用户
    editSpecialistSubmit() {
      this.$refs.editNurseForm2.validate((valide) => {
        if (valide) {
          editSpecialist(this.editForm2).then((res) => {
            if (res.status === 200) {
              this.initSpecialistList();
              this.$message({
                type: 'success',
                message: '编辑成功'
              });
              this.editDialogFormVisible2 = false;
              this.initList();
            } else {
              this.$message({
                type: 'warning',
                message: res.msg
              });
            }
          });
        }
      });
    },
    // 删除用户
    deleteSpecialist(row) {
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
