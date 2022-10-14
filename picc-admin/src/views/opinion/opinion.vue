<template>
  <div class="full">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>留言板管理</el-breadcrumb-item>
      <el-breadcrumb-item
        ><span class="currentpage">留言板管理</span></el-breadcrumb-item
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
      :data="list.slice((pagenum - 1) * pagesize, pagenum * pagesize)"
      v-loading="loading"
      height="calc(100% - 170px)"
    >
      <el-table-column label="姓名" width="240" align="center">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>姓名: {{ scope.row.username }}</p>
            <p>性别: {{ scope.row.sex }}</p>
            <p>电话: {{ scope.row.phone }}</p>
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{ scope.row.username }}</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="反馈身份" width="180">
      </el-table-column>
      <el-table-column prop="problem" label="反映问题"> </el-table-column>
      <el-table-column prop="ctime" label="反馈时间" width="180">
      </el-table-column>
      <el-table-column prop="whether_to_solve" label="是否回复">
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button type="text" @click="showEditDialog(scope.row)"
            >回复</el-button
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
    <el-dialog title="回复" :visible.sync="editDialogFormVisible">
      <el-form
        :model="editForm"
        :label-width="formLabelWidth"
        ref="editManagerForm"
      >
        <el-input
          v-model="editForm.solution"
          auto-complete="off"
          type="textarea"
          :rows="10"
        ></el-input>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOpinion, updateOpinion, changeOpinionState } from '@/api/opinion';
import { formatDate } from '@/utils/index';

export default {
  data() {
    return {
      loading: 'true',
      list: [],
      total: 0,
      pagesize: 10,
      pagenum: 1,
      query: '',
      num: 10,
      editForm: {},
      formLabelWidth: '80px',
      editDialogFormVisible: false
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
      getOpinion().then((res) => {
        this.list = res.data.list;
        this.total = res.data.total;
        this.loading = false;
        for (let i = 0; i < this.list.length; i++) {
          this.list[i].ctime = formatDate(
            this.list[i].ctime,
            'yyyy-MM-dd hh:mm:ss'
          );
        }
      });
    },
    changeState(row) {
      changeOpinionState({
        id: row.id,
        whether_to_solve: row.whether_to_solve
      }).then((res) => {
        if (res.status === 200) {
          this.$message({
            type: 'success',
            message: '修改成功'
          });
        } else {
          this.$message({
            type: 'warning',
            message: res.msg
          });
        }
      });
    },
    showEditDialog(row) {
      this.editForm = row;
      this.editDialogFormVisible = true;
    },
    editSubmit() {
      updateOpinion({
        id: this.editForm.id,
        solution: this.editForm.solution
      }).then((res) => {
        if (res.status === 200) {
          this.initList();
          this.$message({
            type: 'success',
            message: '回复成功'
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
