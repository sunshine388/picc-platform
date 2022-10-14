// pages/common/login/login.js
var app = getApp();
var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
var user = require('../../../utils/user.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    //判断小程序的API，回调，参数，组件等是否在当前版本可用。
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    userInfo: null
  },

  onLoad: function() {
    // 页面加载时使用用户授权逻辑，弹出确认的框  
    this.userAuthorized()
  },

  userAuthorized() {
    wx.getSetting({
      success: data => {
        if (data.authSetting['scope.userInfo']) {
          wx.getUserInfo({
            success: data => {
              this.setData({
                userInfo: data.userInfo
              })
            }
          })
        }
      }
    })
  },

  wxLogin: function (e) {
    const userInfo = e.detail.userInfo
    if (userInfo == undefined) {
      app.globalData.hasLogin = false;
      util.showErrorToast('微信登录失败');
      return;
    }

    user.checkLogin().catch(() => {

      user.loginByWeixin(e.detail.userInfo).then(res => {
        wx.setStorageSync('userInfo', userInfo);
        app.globalData.hasLogin = true;
        this.goStart();

      }).catch((err) => {
        app.globalData.hasLogin = false;
        util.showErrorToast('微信登录失败');
      });

    });
  },

  goPatientHome: function() {
    wx.switchTab({
      url: "/pages/patient/index/index"
    });
  },
  goNurseHome: function() {
    this.data.hasLogin || wx.redirectTo({
      url: "/pages/nurse/index/index"
    });
  },
  goSpecialistHome: function() {
    if (!this.data.hasLogin) {
      wx.redirectTo({
        url: "/pages/specialist/index/index"
      });
    }
  },
  goRole: function() {
    this.data.hasLogin || wx.redirectTo({
      url: "/pages/common/role/role"
    });
  },
  goStart: function() {
    if (!this.data.hasLogin) {
      wx.redirectTo({
        url: "/pages/common/index/index"
      });
    }
  },

})