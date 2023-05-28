<template>
  <div class="login">
    <el-form :model="user" class="login-form" status-icon :rules="rules" ref="userForm">
      <h2 class="title">注册</h2>
      <el-form-item prop="username">
        <el-input
            v-model="user.username"
            type="text"
            auto-complete="off"
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="user.password"
            type="password"
            auto-complete="off"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
            @keyup.enter.native="handleLogin"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
            v-model="user.confirmPassword"
            type="password"
            auto-complete="off"
            placeholder="确认密码"
            prefix-icon="el-icon-lock"
            show-password
            @keyup.enter.native="handleLogin"
        >
        </el-input>
      </el-form-item>
      <el-form-item style="width:100% ">
        <div style="width: 220px;align-items: center;margin: auto; ">
          <el-button
              size="medium"
              type="primary"
              style="width:100px"
              @click.native.prevent="handleLogin"
          >
            <span>注 册</span>
          </el-button>
          <el-button
              size="medium"
              type="warning"
              style="width:100px"
              @click="$router.push('/login')">
            <span>返 回</span>
          </el-button>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright ©...</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        username: [
          {required: true, message: "请输入用户名", trigger: 'blur'}
        ],
        password: [
          {required: true, message: "请输入密码", trigger: 'blur'},
          {min: 2, max: 16, message: "长度在 2 到 16 个字符", trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: "请输入密码", trigger: 'blur'},
          {min: 2, max: 16, message: "长度在 2 到 16 个字符", trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    handleLogin() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入密码不一致")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if (res.code === '200') {
              setTimeout(() => {
                this.$router.push("/login")
              }, 500)  // 登陆后延迟跳转\
              this.$message.success("注册成功")
              // this.$router.push("/")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });

    }
  }
}
</script>

<style lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  background-size: cover;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 38px;
}
</style>