<template>
  <div id="login">
    <img
      style="width: 100%; height: 100%; position: absolute; z-index: -1"
      slt=""
      src="@/assets/login.jpg"
    />
    <el-form
      :model="loginForm"
      ref="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <h2 class="login-title">picc管理中心</h2>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          name="username"
          placeholder="Username"
          auto-complete="on"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          name="password"
          type="password"
          placeholder="Password"
          auto-complete="on"
          @keyup.enter.native="handleLogin('loginForm')"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          class="login-button"
          type="primary"
          @click.native.prevent="handleLogin('loginForm')"
          @keyup.enter.native="handleLogin('loginForm')"
          >登录</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { requseLogin } from '@/api/manager';
import crypto from '@/utils/crypto.js';
import { localDataUser } from '@/utils/local-data.js';

export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
      }
    };
  },
  methods: {
    handleLogin(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          this.loginForm.password = crypto.encryptByDES(
            this.loginForm.password
          );
          // 调用登录接口
          requseLogin(this.loginForm)
            .then((res) => {
              // 根据返回的code判断是否成功
              if (res.status === 200) {
                // elementui中提示组件
                localDataUser.set(res.data);
                // 跳转到后台主页面
                this.$router.push({ name: '/' });
              } else {
                this.$message({
                  type: 'error',
                  message: res.msg
                });
              }
            })
            .catch((err) => {
              this.$message({
                type: 'error',
                message: err
              });
            });
        } else {
          this.$message({
            type: 'error',
            message: '验证失败'
          });
          return false;
        }
      });
    }
  }
};
</script>

<style scoped>
#login {
  position: absolute;
  top: 0;
  left: 0;
  /*background: linear-gradient(90deg,blue,red);*/
  width: 100%;
  height: 100%;
  /*
    background-size: cover;
    background-position:center 0;
    background: url("../assets/login.jpg") no-repeat;*/
}
.login-form {
  width: 350px;
  margin: 160px auto; /* 上下间距160px，左右自动居中*/
  background-color: rgb(255, 255, 255, 0.8); /* 透明背景色 */
  padding: 30px;
  border-radius: 20px; /* 圆角 */
}
.login-title {
  color: #7c7c7c;
  text-align: center;
}
.login-button {
  width: 100%;
}
</style>
