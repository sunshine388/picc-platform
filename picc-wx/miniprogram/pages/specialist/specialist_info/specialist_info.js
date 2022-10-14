// pages/specialist/specialist_info/specialist_info.js
var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
Page({
  data: {
    id: 0,
    username: '',
    sex: '',
    phone: '',
    workUnit: '',
  },
  
  formSubmit: function(e) {
    let that = this;
    if (e.detail.value.workUnit.length ==0) {
      wx.showToast({
        title: '请填写单位地址!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else if (e.detail.value.username.length == 0 || e.detail.value.username.length >= 8) {
      wx.showToast({
        title: '姓名不能为空或过长!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function() {
        wx.hideToast()
      }, 2000)
    } else if (e.detail.value.phone.length< 11 || e.detail.value.phone.length > 11) {
      wx.showToast({
        title: '请输入正确手机号!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else {
      util.request(api.SpecialistInfoSubmit,{
        skey: wx.getStorageSync('token'),
        workUnit: e.detail.value.workUnit,
        username: e.detail.value.username,
        sex: e.detail.value.sex,
        phone: e.detail.value.phone
      }, 'POST').then(res=>{
        if (res.status == 200) {
          wx.redirectTo({
            url: '/pages/specialist/audit_result/audit_result?state=0',
          })
        }
      }).catch((err) => {
        util.showErrorToast('请求失败');
        console.log(err);
      });
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    util.request(api.getDepartment, {}, 'GET').then(res => {
      if (res.status) {
        this.setData({
          department: res.data
        })
      }
    }).catch((err) => {
      util.showErrorToast('请求失败');
      console.log(err);
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})