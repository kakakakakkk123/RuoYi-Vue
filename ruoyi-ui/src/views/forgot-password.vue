<template>
  <div class="login">
    <el-form ref="form" :model="form" :rules="rules" class="login-form">
      <h3 class="title">找回密码</h3>
      <el-form-item prop="username">
        <el-input v-model.trim="form.username" placeholder="登录账号" />
      </el-form-item>
      <el-form-item prop="studentNo">
        <el-input v-model.trim="form.studentNo" placeholder="学号" />
      </el-form-item>
      <el-form-item prop="newPassword" :rules="registerPwdValidator">
        <el-input v-model="form.newPassword" type="password" show-password placeholder="新密码" />
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" show-password placeholder="确认密码" />
      </el-form-item>
      <el-form-item v-if="captchaEnabled" prop="code">
        <el-input v-model.trim="form.code" placeholder="验证码" style="width: 63%" />
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <el-button :loading="loading" type="primary" style="width:100%" @click="handleSubmit">重置密码</el-button>
      <div style="margin-top: 12px; text-align: right;">
        <router-link class="link-type" :to="'/login'">返回登录</router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getCodeImg, forgotPassword } from "@/api/login"
import passwordRule from "@/utils/passwordRule"

export default {
  mixins: [passwordRule],
  data() {
    return {
      form: {
        username: "",
        studentNo: "",
        newPassword: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      codeUrl: "",
      captchaEnabled: true,
      loading: false,
      rules: {
        username: [{ required: true, message: "请输入登录账号", trigger: "blur" }],
        studentNo: [{ required: true, message: "请输入学号", trigger: "blur" }],
        confirmPassword: [{ required: true, message: "请再次输入密码", trigger: "blur" }],
        code: []
      }
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        this.rules.code = this.captchaEnabled ? [{ required: true, trigger: "change", message: "请输入验证码" }] : []
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.form.uuid = res.uuid
        }
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.form.newPassword !== this.form.confirmPassword) {
          this.$modal.msgWarning("两次输入的密码不一致")
          return
        }
        this.loading = true
        forgotPassword(this.form)
          .then(() => {
            this.$modal.msgSuccess("密码重置成功")
            this.$router.push("/login")
          })
          .catch(() => {
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
          .finally(() => {
            this.loading = false
          })
      })
    }
  }
}
</script>

<style scoped lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0 auto 24px auto;
  text-align: center;
  color: #707070;
}
.login-form {
  border-radius: 6px;
  background: #fff;
  width: 400px;
  padding: 25px 25px 5px 25px;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
}
.login-code-img {
  height: 38px;
  cursor: pointer;
}
</style>
