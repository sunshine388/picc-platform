// pages/nurse/homepage/returnVisit/returnVisit.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    nurseInfo: {},
    patientList: [],
    noData:false

  },
  returnVisitJump(e){
    wx.navigateTo({
      url: '/pages/nurse/message/messageitem/messageitem?patientInfo='+JSON.stringify(this.data.patientList[e.target.dataset.index]),
    })
  },
  appointmentHistoryJump(){
    wx.navigateTo({
      url: '/pages/nurse/homepage/appointmentHistory/appointmentHistory',
    })
  },
  init(){
    util.request(api.getPatientByJobNumber, {
      jobnumber: this.data.nurseInfo.jobnumber
    }, 'GET').then(res => {
      if (res.status == 200) {
        let patientList=res.data;
        if (patientList.length != 0) {
          for(let i=0;i<patientList.length;i++){
            patientList[i].create_time = util.timestampFormatDate(patientList[i].create_time, "yyyy-MM-dd hh:mm:ss");
          }
          this.setData({
            patientList: patientList,
            noData: false
          })
        } else {
          this.setData({
            noData: true
          })
        }
      }
    }).catch((err) => {
      util.showErrorToast();
      console.log(err);
    });
  },
  onLoad: function (options) {
    this.setData({
      nurseInfo: wx.getStorageSync('nurseInfo')
    });
    this.init();
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