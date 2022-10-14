<template>
  <div class="app">
    <el-container>
      <el-aside
        class="app-side"
        :class="isCollapse ? 'app-side-collapsed' : 'app-side-expanded'"
      >
        <div class="app-side-logo">
          <img
            src="@/assets/logo.png"
            :width="isCollapse ? '60' : '60'"
            height="60"
          />
        </div>
        <div class="app-side-menu scrollbar-default">
          <el-menu
            :unique-opened="true"
            default-active="/user"
            :collapse="isCollapse"
            :router="true"
            class="el-menu-admin"
            background-color="#3b3c5f"
            text-color="#fff"
            active-text-color="#ffd04b"
          >
            <el-submenu
              :index="item.path"
              v-for="item in menuData"
              :key="item.name"
            >
              <template slot="title">
                <i class="el-icon-location"></i>
                <span>{{ item.meta.label }}</span>
              </template>
              <el-menu-item
                :index="tag.path"
                v-for="tag in item.children"
                :key="tag.name"
              >
                <i class="el-icon-menu"></i>
                <span slot="title">{{ tag.meta.label }}</span>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </div>
      </el-aside>

      <el-container>
        <el-header class="app-header">
          <div
            style="width: 60px; cursor: pointer"
            @click.prevent="toggleSideBar"
          >
            <i v-show="!isCollapse" class="el-icon-d-arrow-left"></i>
            <i v-show="isCollapse" class="el-icon-d-arrow-right"></i>
          </div>
          <span class="app-header-title">picc管理中心</span>
          <div class="app-header-userinfo" :hide-on-click="false">
            <el-dropdown>
              <span class="el-dropdown-link" style="color: #fff">
                {{ username }}
                <i class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="forget"
                  >修改密码</el-dropdown-item
                >
                <el-dropdown-item @click.native="logout"
                  >退出登录</el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%">
      <el-form
        :model="passwordForm"
        :label-width="formLabelWidth"
        ref="passwordForm"
      >
        <el-form-item
          label="旧密码"
          prop="oriPassword"
          :rules="[rules.required, rules.password]"
        >
          <el-input
            v-model="passwordForm.oriPassword"
            auto-complete="off"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="新密码"
          prop="newPassword"
          :rules="[
            rules.required,
            rules.password,
            rules.oldPassword(passwordForm.oriPassword)
          ]"
        >
          <el-input
            v-model="passwordForm.newPassword"
            auto-complete="off"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="确认密码"
          prop="newPasswords"
          :rules="[
            rules.required,
            rules.password,
            rules.samePassword(passwordForm.newPassword)
          ]"
        >
          <el-input
            v-model="passwordForm.newPasswords"
            auto-complete="off"
            type="password"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="sumbit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import rules from '@/utils/validate.js';
import crypto from '@/utils/crypto.js';
import { updateManagerPassword } from '@/api/manager';
import { subRoutes } from '@/router';
import { localDataUser } from '@/utils/local-data.js';

export default {
  name: 'Container',
  data() {
    return {
      isCollapse: false,
      menuData: subRoutes,
      dialogVisible: false,
      passwordForm: {
        oriPassword: '',
        newPassWord: '',
        newPasswords: ''
      },
      formLabelWidth: '80px',
      rules: rules,
      username: localDataUser.get().username
    };
  },
  methods: {
    toggleSideBar() {
      this.isCollapse = !this.isCollapse;
    },
    logout: function () {
      this.$confirm('确认退出?', '提示', {})
        .then(() => {
          localDataUser.clear();
          this.$router.push('/login');
        })
        .catch(() => {});
    },
    forget() {
      this.dialogVisible = true;
    },
    sumbit() {
      let username = localDataUser.get().username;
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          let oriPassword = crypto.encryptByDES(this.passwordForm.oriPassword);
          let newPassword = crypto.encryptByDES(this.passwordForm.newPassword);
          const params = {
            oriPassword: oriPassword,
            newPassword: newPassword,
            username: username
          };
          updateManagerPassword(params).then((res) => {
            if (res.status === 200) {
              this.$message({
                type: 'success',
                message: res.msg
              });
              localDataUser.clear();
              this.$router.push('/login');
              this.dialogVisible = false;
            } else {
              this.$message({
                type: 'warning',
                message: res.msg
              });
            }
          });
        }
      });
    }
  },
  // 获取左侧菜单列表
  created() {
    // getMenu().then((res) => {
    //   if (res.status === 200) {
    //     this.menuData = res.data;
    //   }
    // });
  }
};
</script>

<style>
.app {
  width: 100%;
  height: 100%;
  font-family: '微软雅黑';
  display: flex;
  display: -webkit-flex;
  flex-flow: row nowrap;
  position: absolute;
  min-width: 1000px;
}
.app-side {
  width: 200px;
  height: 100%;
  font-weight: 700;
  border-right: 1px solid #eee;
  /*background-color: #464646;*/
  background-color: #3b3c5f;
}
.app-side-logo {
  height: 60px;
  text-align: center;
}
.app-side-collapsed {
  width: 66px !important;
}
.app-side-expanded {
  width: 200px !important;
}
.app-side-menu {
  height: calc(100% - 60px);
  overflow-y: auto;
}
.app-header {
  width: 100%;
  height: 60px;
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-start;
  align-items: center;
  border-bottom: 1px solid #eee;
  background: linear-gradient(90deg, #639df1, #e44e85);
  color: #fff;
}
.app-header-title {
  margin: 0 auto;
  font-size: 28px;
  /*color: #464646;*/
}
.app-header-userinfo {
  position: absolute;
  right: 0;
  margin-right: 25px;
  display: flex;
  flex-flow: row nowrap;
  height: 60px;
  justify-content: flex-start;
  align-items: center;
}
</style>
