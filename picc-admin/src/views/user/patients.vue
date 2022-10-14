<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">患者列表</span></el-breadcrumb-item
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
      <el-table-column prop="address" label="详细地址" width="150">
      </el-table-column>
      <el-table-column prop="phone" label="联系电话"> </el-table-column>
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
            @click="deletePatient(scope.row)"
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
    <el-dialog title="编辑用户" :visible.sync="editDialogFormVisible">
      <el-form
        :model="editForm"
        :label-width="formLabelWidth"
        ref="editPatientForm"
        :rules="rules"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="editForm.username"
            auto-complete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="editForm.sex" placeholder="请选择">
            <el-option
              v-for="item in option1"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-input v-model="editForm.birthday" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="editForm.phone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="文化程度" prop="culture">
          <el-select v-model="editForm.culture" placeholder="请选择">
            <el-option
              v-for="item in option2"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="详细地址" prop="city">
          <el-input v-model="editForm.address" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editPatientSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPatientList,
  getPatientById,
  editPatient,
  deletePatient
} from '@/api/patients';
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
      option1: [
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
      option2: [
        {
          value: '选项1',
          label: '文盲'
        },
        {
          value: '选项2',
          label: '小学'
        },
        {
          value: '选项3',
          label: '初中'
        },
        {
          value: '选项4',
          label: '高中'
        },
        {
          value: '选项5',
          label: '大专及以上'
        }
      ],
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        sex: [{ required: true, message: '请选择性别', trigger: 'blur' }],
        birthday: [{ required: true, message: '出生日期不能为空' }],
        phone: [{ required: true, message: '电话不能为空' }],
        culture: [{ required: true, message: '请选择文化程度' }],
        address: [{ required: true, message: '请输入居住地址' }]
      },
      editForm: {
        username: '',
        sex: '',
        birthday: '',
        age: '',
        phone: '',
        culture: '',
        city: '',
        address: '',
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
      getPatientList({
        query: this.query,
        pagenum: this.pagenum,
        pagesize: this.pagesize
      }).then((res) => {
        this.patientList = res.data.patients;
        for (let i = 0; i < this.patientList.length; i++) {
          this.patientList[i].age = getAge(this.patientList[i].birthday);
        }
        this.total = res.data.total;
        this.loading = false;
      });
    },
    // 根据id获取用户信息
    showEditDialog(row) {
      this.editDialogFormVisible = true;
      getPatientById({ id: row.id }).then((res) => {
        if (res.status === 200) {
          this.editForm.username = res.data.username;
          this.editForm.patientnumber = res.data.patientnumber;
          this.editForm.sex = res.data.sex;
          this.editForm.birthday = res.data.birthday;
          this.editForm.age = getAge(res.data.birthday);
          this.editForm.phone = res.data.phone;
          this.editForm.id = res.data.id;
          this.editForm.culture = res.data.culture;
          this.editForm.city = res.data.city;
          this.editForm.address = res.data.address;
        } else {
          this.$message({
            type: 'warning',
            message: '获取用户信息失败，请稍后尝试'
          });
        }
      });
    },
    // 编辑用户
    editPatientSubmit() {
      this.$refs.editPatientForm.validate((valide) => {
        if (valide) {
          editPatient(this.editForm).then((res) => {
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
    deletePatient(row) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deletePatient({ id: row.id }).then((res) => {
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
