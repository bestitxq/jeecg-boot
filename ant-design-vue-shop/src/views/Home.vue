<template>
  <div class="login-container">
    <div class="b-wrapper">
      <div class="left">
        <h4><strong>欢迎登录智慧教学系统</strong></h4>
        <div>
          <br />
          当前登录用户昵称：{{ $store.getters.userInfo.realname }} <a-button @click="userLogout()" >退出登录</a-button><br />
          当前用户会员到期：{{ vipDay == undefined ? '非会员' : 'vip剩余' + vipDay + '天（不包含当日）' }}<br />
          当前用户累计邀请人数：{{ inviteUserNum }}人<br />
          当前用户推广链接：<a :href="adUrl" target="_blank">{{ adUrl }}</a><br />
          当前规则展示历程：
          <div v-for="item in showVipRuleData" :key="item.id">
            <font color="greet"> 邀请{{ item.userNum }}人 - 奖励{{ item.vipDay }}天vip </font>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { generateAdUrl, memberTime, showVipRule, getInviteUserNum } from '@/api/userApi'
import { logout } from '@/api/config/request'


export default {
  data() {
    return {
      adUrl: undefined,
      vipDay: undefined,
      t: '',
      showVipRuleData: [],
      inviteUserNum: 0,
      ruleForm: {
        username: '',
        password: '',
        captcha: '',
      },
      rules: {
        username: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          },
        ],
        captcha: [
          {
            required: true,
            message: '请输入验证码',
            trigger: 'blur',
          },
        ],
      },
    }
  },
  mounted() {
    this.getUrl()
  },
  computed: {},
  methods: {
    userLogout(){
      logout()
    },
    getUrl() {
      // 生成推广url
      generateAdUrl().then((x) => {
        if (x.success) {
          this.adUrl = x.result
        }
      })
      memberTime().then((x) => {
        if (x.success) {
          this.vipDay = x.result
        }
      })
      showVipRule().then((x) => {
        if (x.success) {
          this.showVipRuleData = x.result
        }
      })
      getInviteUserNum().then((x) => {
        if (x.success) {
          this.inviteUserNum = x.result
        }
      })
    },
  },
}
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
  top: 20%;
  transform: translateY(-20%);
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
  input[type='text'] {
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