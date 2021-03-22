<template>
  <div class="login-container">
    <div class="b-wrapper">
      <div class="left">
        <h4><strong>智慧教学系统</strong></h4>
        <div>
          将信息技术与基于工作过程的教学、项目化教学、情景式
          教学、案例教学等相结合，实施差异化教学、课程体系、课程内容以及教学管理现代化，推进教学模式创新和人才培养模式创新，提升职业院校教育教学质量、效率以及社会服务能力构建人人互通的数字化学习空间，推动教学模式变革，提高人才培养质量
        </div>
      </div>
      <div class="right">
        <div class="con">
          <h5>用户登录</h5>
          <form action="" ref="ruleForm" >
            <input type="text"  placeholder="请输入昵称" v-model="ruleForm.username">
            <input type="password" placeholder="请输入密码" v-model="ruleForm.password">
            <input type="text" placeholder="请输入验证码" v-model="ruleForm.captcha">
            <div class="img-box" @click="getCaptcha">
              <img v-if="$store.state.user.captcha" style="margin-top: 2px;" :src="$store.state.user.captcha"/>
              <img v-else style="margin-top: 2px;" src="../../assets/images/checkcode.png"/>
            </div>
          </form>
          <span class="but" @click="submitForm('ruleForm')">提交</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { login,axios } from '@/api/config/request'

export default {
  data() {
    return {
      t: "",
      ruleForm: {
        username: "",
        password: "",
        captcha: "",
      },
      rules: {
        username: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
        ],
        captcha: [
          {
            required: true,
            message: "请输入验证码",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.getCaptcha();
  },
  computed: {
  },
  methods: {
    getCaptcha() {
      const t = Date.now();
      this.t = t;
      this.$store.dispatch("aLoginCaptcha", {paramets: t});
    },
    submitForm() {
      const paramets = this.ruleForm;
      paramets.checkKey = this.t;
      login(paramets, this.$route.query.redirect)
    },
  },
};
</script>

<style scoped lang="scss">
.login-container {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100%;
  background: url(~@/assets/images/login-bg.jpg) no-repeat center center;
  background-size: cover;
}
.b-wrapper {
  position: relative;
  top: 50%;
  transform: translateY(-50%);
}
.left {
  float: left;
  width: 50%;
  padding: 0 40px 0 80px;
  box-sizing: border-box;
  h4 {
    strong {
      display: inline-block;
      font-size: 0.46rem;
      color: #fff;
      font-weight: normal;
      border-bottom: 1px solid #fff;
      padding-bottom: 0.2rem;
      margin-bottom: 0.2rem;
      text-shadow: 3px 3px 0 rgba(0, 0, 0, 0.2);
    }
  }
  div {
    font-size: 0.2rem;
    color: #fff;
    background: rgba(255, 255, 255, 0.2);
    padding: 10px;
    text-indent: 2em;
    line-height: 1.6;
    position: relative;
    &::before {
      display: block;
      width: 100%;
      height: 100%;
      content: "";
      position: absolute;
      top: 8px;
      left: -8px;
      right: 0;
      bottom: -9px;
      background: rgba(255, 255, 255, 0.2);
    }
  }
}
.right {
  float: left;
  width: 50%;
  padding: 0 40px;
  box-sizing: border-box;
  .con {
    width: 24rem;
    box-sizing: border-box;
    background: #fff;
    border-radius: 10px;
    padding: 40px;
  }
  input[type="text"] {
    font-size: 16px;
  }
  .img-box {
    float: right;
    width: 35%;
    height: 40px;
    background: #f2f2f2;
    overflow: hidden;
    vertical-align: middle;
    img {
      width: 100%;
      height: 100%;
    }
  }
  button {
    width: 100%;
    font-size: 16px;
    margin-top: 10px;
  }
}
</style>