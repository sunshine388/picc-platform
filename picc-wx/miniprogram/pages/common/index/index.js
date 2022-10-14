// pages/index/index.js
var app = getApp();
// pages/start/start.js
var user = require('../../../utils/user.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    hasLogin: false
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let that = this;
    user.checkLogin().then(res => {
      console.log("check login success");
      app.globalData.hasLogin = true;
      that.updateGloableData();
    }).catch(() => {
      //skip account auth
      console.log("check login false");
      app.globalData.hasLogin = false;
      that.updateGloableData();
    });
  },
  onReady: function() {},
  onShow: function() {},
  onHide: function() {},
  onUnload: function() {},
  onPullDownRefresh: function() {},
  onReachBottom: function() {},
  onShareAppMessage: function() {},

  goLogin: function() {
    if (!this.data.hasLogin) {
      wx.redirectTo({
        url: "/pages/common/login/login"
      });
    }
  },
  goPatientHome: function() {
    wx.redirectTo({
      url: '/pages/patient/index/index',
    })
  },
  goNurseHome: function() {
    if (!this.data.hasLogin) {
      wx.redirectTo({
        url: "/pages/nurse/index/index"
      });
    }
  },
  goSpecialistHome: function() {
    if (!this.data.hasLogin) {
      wx.redirectTo({
        url: "/pages/specialist/index/index"
      });
    }
  },
  goRole: function() {
    if (!this.data.hasLogin) {
      wx.redirectTo({
        url: "/pages/common/role/role"
      });
    }
  },
  //更新用户信息
  updateGloableData: function() {

    let userInfo = wx.getStorageSync('userInfo');

    app.globalData.role = userInfo.role;
    app.globalData.userId = userInfo.userid;

    if (userInfo.avatarUrl) {
      app.globalData.avatarUrl = userInfo.avatarUrl;
    }

    if (app.globalData.hasLogin) {
      if (app.globalData.role == "undefined" ||
        app.globalData.role == "" ||
        app.globalData.role == null ||
        app.globalData.role == 'none') {
        this.goRole();
      } else if (app.globalData.role == "nurse") {
        this.goNurseHome();
      }  else if (app.globalData.role == "specialist") {
        this.goSpecialistHome();
      }else {
        this.goPatientHome();
      }
    } else {
      this.goLogin();
    }
  }
})