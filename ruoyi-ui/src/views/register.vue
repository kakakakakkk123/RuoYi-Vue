<template>
  <div class="register-page">
    <div class="register-panel">
      <div class="register-hero">
        <p class="eyebrow">学生自主注册</p>
        <h3 class="title">{{ title }}</h3>
        <p class="subtitle">使用学号创建账号，支持验证码与邮箱验证码两种校验方式。</p>
        <el-alert
          title="填写说明"
          type="info"
          :closable="false"
          description="请先准备好学号、登录账号、昵称和密码；如果填写邮箱，可点击发送验证码完成邮箱校验。"
          show-icon
        />
        <el-alert
          v-if="!registerEnabled"
          title="当前未开放学生自主注册"
          type="warning"
          :closable="false"
          show-icon
          style="margin-top: 12px;"
        />
      </div>

      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
        <el-form-item prop="username">
          <el-input v-model.trim="registerForm.username" placeholder="登录账号">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="studentNo">
          <el-input v-model.trim="registerForm.studentNo" placeholder="学号">
            <svg-icon slot="prefix" icon-class="number" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="nickName">
          <el-input v-model.trim="registerForm.nickName" placeholder="昵称">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model.trim="registerForm.email" placeholder="校内邮箱（可选）">
            <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
            <el-button slot="append" :loading="emailCodeLoading" :disabled="!registerForm.email" @click="handleSendEmailCode">
              发送验证码
            </el-button>
          </el-input>
        </el-form-item>
        <el-form-item v-if="registerForm.email" prop="emailCode">
          <el-input
            v-model.trim="registerForm.emailCode"
            placeholder="邮箱验证码"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password" :rules="registerPwdValidator">
          <el-input
            v-model="registerForm.password"
            type="password"
            show-password
            placeholder="密码"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            show-password
            placeholder="确认密码"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item v-if="captchaEnabled" prop="code">
          <el-input
            v-model.trim="registerForm.code"
            placeholder="验证码"
            style="width: 63%"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="register-code">
            <img :src="codeUrl" class="register-code-img" @click="getCode" />
          </div>
        </el-form-item>
        <div class="form-tip">
          如果填写了邮箱，可用于后续找回密码；当前版本仍以学号作为注册主标识。
        </div>
        <el-form-item style="width: 100%;">
          <el-button
            :loading="loading"
            :disabled="!registerEnabled"
            size="medium"
            type="primary"
            style="width: 100%;"
            @click.native.prevent="handleRegister"
          >
            <span v-if="!loading">注册</span>
            <span v-else>注册中...</span>
          </el-button>
          <div class="link-row">
            <router-link class="link-type" :to="'/login'">返回登录</router-link>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <div class="el-register-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register, sendRegisterEmailCode } from "@/api/login"
import passwordRule from "@/utils/passwordRule"
import defaultSettings from "@/settings"

export default {
  mixins: [passwordRule],
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      registerForm: {
        username: "",
        studentNo: "",
        nickName: "",
        email: "",
        emailCode: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      loading: false,
      emailCodeLoading: false,
      captchaEnabled: true,
      registerEnabled: true
    }
  },
  computed: {
    registerRules() {
      const rules = {
        username: [
          { required: true, trigger: "blur", message: "请输入登录账号" },
          { min: 2, max: 20, trigger: "blur", message: "账号长度必须在 2 到 20 个字符之间" }
        ],
        studentNo: [
          { required: true, trigger: "blur", message: "请输入学号" },
          { min: 2, max: 20, trigger: "blur", message: "学号长度不能超过 20 个字符" }
        ],
        nickName: [
          { required: true, trigger: "blur", message: "请输入昵称" },
          { min: 2, max: 30, trigger: "blur", message: "昵称长度不能超过 30 个字符" }
        ],
        email: [
          { type: "email", trigger: ["blur", "change"], message: "请输入正确的邮箱地址" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入密码" },
          {
            validator: (rule, value, callback) => {
              if (this.registerForm.password !== value) {
                callback(new Error("两次输入的密码不一致"))
              } else {
                callback()
              }
            },
            trigger: "blur"
          }
        ]
      }
      if (this.registerForm.email) {
        rules.emailCode = [{ required: true, trigger: "blur", message: "请输入邮箱验证码" }]
      }
      if (this.captchaEnabled) {
        rules.code = [{ required: true, trigger: "change", message: "请输入验证码" }]
      }
      return rules
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        this.registerEnabled = res.registerEnabled === undefined ? true : res.registerEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.registerForm.uuid = res.uuid
        }
      }).catch(() => {})
    },
    handleSendEmailCode() {
      this.$refs.registerForm.validateField("email", error => {
        if (error) {
          return
        }
        if (!this.registerForm.email) {
          this.$modal.msgWarning("请先填写邮箱")
          return
        }
        this.emailCodeLoading = true
        sendRegisterEmailCode({ email: this.registerForm.email })
          .then(() => {
            this.$modal.msgSuccess("邮箱验证码已发送，请注意查收")
          })
          .catch(error => {
            const message = (error && error.message) ? error.message : "邮箱验证码发送失败"
            this.$modal.msgError(message)
          })
          .finally(() => {
            this.emailCodeLoading = false
          })
      })
    },
    handleRegister() {
      if (!this.registerEnabled) {
        this.$modal.msgWarning("当前未开放学生自主注册")
        return
      }
      this.$refs.registerForm.validate(valid => {
        if (!valid) {
          return
        }
        this.loading = true
        register(this.registerForm)
          .then(res => {
            const message = (res && res.msg) || ("账号 " + this.registerForm.username + " 注册成功")
            this.$alert(message, "注册成功", {
              confirmButtonText: "去登录",
              type: "success"
            }).then(() => {
              this.$router.push("/login")
            }).catch(() => {})
          })
          .catch(error => {
            const message = (error && error.message) ? error.message : "注册失败，请检查输入后重试"
            this.$modal.msgError(message)
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

<style rel="stylesheet/scss" lang="scss" scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100%;
  padding: 24px;
  background:
    linear-gradient(135deg, rgba(17, 24, 39, 0.76), rgba(15, 23, 42, 0.62)),
    url("../assets/images/login-background.jpg");
  background-size: cover;
  background-position: center;
}

.register-panel {
  width: 440px;
  max-width: 100%;
  padding: 28px 28px 18px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.3);
}

.register-hero {
  margin-bottom: 20px;
}

.eyebrow {
  margin: 0 0 8px;
  color: #2f6bff;
  font-size: 12px;
  letter-spacing: 0.12em;
}

.title {
  margin: 0;
  font-size: 26px;
  color: #1f2937;
}

.subtitle {
  margin: 8px 0 14px;
  color: #6b7280;
  line-height: 1.6;
}

.register-form {
  .el-input {
    height: 40px;

    input {
      height: 40px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.form-tip {
  margin-bottom: 16px;
  font-size: 12px;
  line-height: 1.6;
  color: #6b7280;
}

.register-code {
  width: 33%;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.register-code-img {
  height: 40px;
  width: 100%;
  border-radius: 4px;
}

.link-row {
  margin-top: 12px;
  text-align: right;
}

.el-register-footer {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  height: 40px;
  line-height: 40px;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
