<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>绑定信息</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">患者绑定</span></el-breadcrumb-item
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
      :data="bindingList.slice((pagenum - 1) * pagesize, pagenum * pagesize)"
      border
      v-loading="loading"
      height="calc(100% - 170px)"
    >
      <el-table-column
        prop="patientnumber"
        label="患者编号"
        width="200"
        sortable
      ></el-table-column>
      <el-table-column
        prop="patientname"
        label="患者姓名"
        width="200"
      ></el-table-column>
      <el-table-column
        prop="nursename"
        label="护士姓名"
        width="200"
      ></el-table-column>
      <el-table-column
        prop="jobnumber"
        label="护士工号"
        width="200"
        sortable
      ></el-table-column>
      <el-table-column prop="create_time" label="绑定时间"></el-table-column>
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
            @click="deleteBinding(scope.row)"
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
    <el-dialog title="修改绑定信息" :visible.sync="editDialogFormVisible">
      <el-form
        :model="editForm"
        :label-width="formLabelWidth"
        ref="editBindingForm"
        :rules="rules"
      >
        <el-form-item label="患者编号" prop="patientnumber">
          <el-input
            v-model="editForm.patientnumber"
            auto-complete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="患者姓名" prop="patientname">
          <el-input
            v-model="editForm.patientname"
            auto-complete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="护士姓名" prop="nursename">
          <el-select
            v-model="editForm.nursename"
            placeholder="请选择"
            @change="selectNurse"
          >
            <el-option
              v-for="item in nurseList"
              :key="item.jobnumber"
              :label="item.username"
              :value="{ value: item.jobnumber, label: item.username }"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editBindingSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBindingList,
  getBindingById,
  editBinding,
  deleteBinding
} from '@/api/binding';
import { getNurseList } from '@/api/nurses';
import { formatDate } from '@/utils/index';

export default {
  data() {
    return {
      loading: 'true',
      bindingList: [],
      total: 0,
      nurseList: [],
      nurseTotal: 0,
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
        phone: [{ required: true, message: '电话不能为空' }]
      },
      editForm: {
        patientnumber: '',
        patientname: '',
        nursename: '',
        jobnumber: ''
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
    // 获取用户绑定列表信息
    initList() {
      getBindingList({
        query: this.query,
        pagenum: this.pagenum,
        pagesize: this.pagesize
      }).then((res) => {
        this.bindingList = res.data.bindings;
        this.total = res.data.total;
        this.loading = false;
        for (let i = 0; i < this.bindingList.length; i++) {
          this.bindingList[i].create_time = formatDate(
            this.bindingList[i].create_time,
            'yyyy-MM-dd hh:mm:ss'
          );
        }
      });
    },
    // 根据id获取绑定信息
    showEditDialog(row) {
      this.editDialogFormVisible = true;
      getBindingById({ id: row.id }).then((res) => {
        if (res.status === 200) {
          this.editForm.id = res.data.id;
          this.editForm.patientnumber = res.data.patientnumber;
          this.editForm.patientname = res.data.patientname;
          this.editForm.nursename = res.data.nursename;
          this.editForm.jobnumber = res.data.jobnumber;
        } else {
          this.$message({
            type: 'warning',
            message: '获取绑定信息失败，请稍后尝试'
          });
        }
      });
      getNurseList({
        query: '',
        pagenum: this.pagenum,
        type: 1
      }).then((res) => {
        this.nurseList = res.data.nurses;
        this.nurseTotal = res.data.total;
      });
    },
    selectNurse(data) {
      const { value, label } = data;
      this.editForm.jobnumber = value;
      this.editForm.nursename = label;
    },
    // 编辑用户
    editBindingSubmit() {
      this.$refs.editBindingForm.validate((valide) => {
        if (valide) {
          editBinding(this.editForm).then((res) => {
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
    deleteBinding(row) {
      this.$confirm('此操作将永久删除该绑定信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          deleteBinding({ id: row.id }).then((res) => {
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
