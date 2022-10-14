<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">管理员列表</span></el-breadcrumb-item
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
    <el-button type="success" plain @click="addManager" v-if="isSuperManager"
      >添加用户</el-button
    >
    <el-table
      :data="managerList.slice((pagenum - 1) * pagesize, pagenum * pagesize)"
      border
      v-loading="loading"
      height="calc(100% - 170px)"
    >
      <el-table-column prop="username" label="姓名"> </el-table-column>
      <el-table-column prop="email" label="邮箱"> </el-table-column>
      <el-table-column prop="phone" label="电话"> </el-table-column>
      <!--
      <el-table-column label="用户状态">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.state"
            @change="changeManagerState(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      -->
      <el-table-column label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="showEditDialog(scope.row)"
            v-if="isSuperManager"
          ></el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="deleteManager(scope.row)"
            v-if="isSuperManager"
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
    <!-- 添加用户对话框   -->
    <el-dialog title="添加用户" :visible.sync="addDialogFormVisible">
      <el-form
        :model="addForm"
        :label-width="formLabelWidth"
        ref="addManagerForm"
      >
        <el-form-item label="用户名" prop="username" :rules="[rules.required]">
          <el-input v-model="addForm.username" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="密码"
          prop="password"
          :rules="[rules.required, rules.password]"
        >
          <el-input v-model="addForm.password" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="邮箱"
          prop="email"
          :rules="[rules.required, rules.email]"
        >
          <el-input v-model="addForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="电话"
          prop="phone"
          :rules="[rules.required, rules.phone]"
        >
          <el-input v-model="addForm.phone" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addManagerSubmit">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 编辑用户对话框 -->
    <el-dialog title="编辑用户" :visible.sync="editDialogFormVisible">
      <el-form
        :model="editForm"
        :label-width="formLabelWidth"
        ref="editManagerForm"
      >
        <el-form-item label="用户名" prop="username" :rules="[rules.required]">
          <el-input
            v-model="editForm.username"
            auto-complete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="邮箱"
          prop="email"
          :rules="[rules.required, rules.email]"
        >
          <el-input v-model="editForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="电话"
          prop="phone"
          :rules="[rules.required, rules.phone]"
        >
          <el-input v-model="editForm.phone" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editManagerSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getManagerList,
  changeManagerState,
  addManager,
  getManagerById,
  editManager,
  deleteManager
} from '@/api/manager';
import crypto from '@/utils/crypto.js';
import rules from '@/utils/validate.js';
import { localDataUser } from '@/utils/local-data.js';

export default {
  data() {
    return {
      isSuperManager: false,
      loading: 'true',
      managerList: [],
      total: 0,
      pagesize: 10,
      pagenum: 1,
      query: '',
      num: 10,
      addForm: {
        username: '',
        password: '',
        email: '',
        phone: ''
      },
      rules: rules,
      formLabelWidth: '80px',
      addDialogFormVisible: false,
      editForm: {
        username: '',
        email: '',
        phone: '',
        id: ''
      },
      editDialogFormVisible: false
    };
  },
  created() {
    let state = localDataUser.get().state;
    if (state === 1) {
      this.isSuperManager = true;
    }
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
      getManagerList({
        query: this.query,
        pagenum: this.pagenum,
        pagesize: this.pagesize
      }).then((res) => {
        this.managerList = res.data.users;
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 改变用户状态
    changeManagerState(row) {
      changeManagerState({ id: row.id, state: row.state }).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '用户状态修改成功'
          });
        } else {
          this.$message({
            type: 'warning',
            message: res.msg
          });
        }
      });
    },
    addManager() {
      this.addDialogFormVisible = true;
      this.addForm = {
        username: '',
        password: '',
        email: '',
        phone: ''
      };
    },
    // 添加用户
    addManagerSubmit() {
      this.$refs.addManagerForm.validate((valid) => {
        if (valid) {
          this.addForm.password = crypto.encryptByDES(this.addForm.password);
          addManager(this.addForm).then((res) => {
            if (res.status === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              this.initList();
              this.addDialogFormVisible = false;
            } else {
              this.$message({
                type: 'warning',
                message: res.msg
              });
            }
          });
        } else {
          return false;
        }
      });
    },
    // 根据id获取用户信息
    showEditDialog(row) {
      this.editDialogFormVisible = true;
      getManagerById({ id: row.id }).then((res) => {
        if (res.status === 200) {
          this.editForm.username = res.data.username;
          this.editForm.email = res.data.email;
          this.editForm.phone = res.data.phone;
          this.editForm.id = res.data.id;
        } else {
          this.$message({
            type: 'warning',
            message: '获取用户信息失败，请稍后尝试'
          });
        }
      });
    },
    // 编辑用户
    editManagerSubmit() {
      this.$refs.editManagerForm.validate((valide) => {
        if (valide) {
          editManager(this.editForm).then((res) => {
            if (res.status === 200) {
              this.initList();
              this.$message({
                type: 'success',
                message: '编辑成功'
              });
              this.editDialogFormVisible = false;
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
    deleteManager(row) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteManager({ id: row.id }).then((res) => {
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
