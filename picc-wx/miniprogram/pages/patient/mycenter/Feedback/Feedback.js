// pages/patient/mycenter/Feedback/Feedback.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    patientInfo: {},
    problem: ''
  },
  textareaAInput(e) {
    this.setData({
      problem: e.detail.value
    })
  },
  submit() {
    if (!this.data.problem) {
      wx.showToast({
        title: '不能为空!',
        icon: 'none',
        duration: 1500,
      })
    } else {
      let params = {
        problem: this.data.problem,
        role: '患者',
        skey: wx.getStorageSync('token'),
        username: this.data.patientInfo.username
      };
      util.request(api.createOpinion, params, 'POST').then(res => {
        if (res.status == 200) {
          wx.showToast({
            title: '提交成功!',
            icon: 'none',
            duration: 1500,
            success(data) {
              setTimeout(function () {
                wx.redirectTo({
                  url: '/pages/patient/mycenter/FeedbackSuccess/FeedbackSuccess',
                })
              }, 1000) //延迟时间
            }
          })
        }
      }).catch((err) => {
        util.showErrorToast('提交失败');
        console.log(err);
      });
    }
  },
  historyJump() {
    wx.navigateTo({
      url: '/pages/patient/mycenter/FeedbackHistory/FeedbackHistory',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      patientInfo: wx.getStorageSync('patientInfo')
    })
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