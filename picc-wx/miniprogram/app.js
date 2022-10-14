//app.js
App({
  onLaunch: function () {
    if (!wx.cloud) {
      console.error('请使用 2.2.3 或以上的基础库以使用云能力')
    } else {
      wx.cloud.init({
        // env 参数说明：
        //   env 参数决定接下来小程序发起的云开发调用（wx.cloud.xxx）会默认请求到哪个云环境的资源
        //   此处请填入环境 ID, 环境 ID 可打开云控制台查看
        //   如不填则使用默认环境（第一个创建的环境）
        // env: 'my-env-id',
        traceUser: true,
      })
    }

    const updateManager = wx.getUpdateManager();
    wx.getUpdateManager().onUpdateReady(function () {
      wx.showModal({
        title: "更新提示",
        content: "新版本已经准备好，是否重启应用？",
        success: function (res) {
          res.confirm && updateManager.applyUpdate();
        }
      });
    });
    wx.getSystemInfo({
      success: e => {
        this.globalData.StatusBar = e.statusBarHeight;
        let capsule = wx.getMenuButtonBoundingClientRect();
        if (capsule) {
          this.globalData.Custom = capsule;
          this.globalData.CustomBar = capsule.bottom + capsule.top - e.statusBarHeight;
        } else {
          this.globalData.CustomBar = e.statusBarHeight + 50;
        }
      }
    });
    var logs = wx.getStorageSync("logs") || [];
    logs.unshift(Date.now()),
      wx.setStorageSync("logs", logs),
      this.globalData.userInfo = null,
      this.globalData.hasLogin = !1,
      this.globalData.role = "",
      this.globalData.userId = -1,
      this.globalData.status = !1

      wx.login({
        success: res => {
          // 发送 res.code 到后台换取 openId, sessionKey, unionId
        }
      })
  },
  globalData: {
    userInfo: null,
    hasLogin: false,   //
    role: '',        //角色nurse//patient
    userId: -1,
    status: false
  }
})