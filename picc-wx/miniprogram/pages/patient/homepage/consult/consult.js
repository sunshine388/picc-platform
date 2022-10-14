// pages/patient/homepage/consult/consult.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bindingNurse:{},
    nurseList:[],
    patientInfo:{},
    noBinding:true,
  },
  
   consultJump1(e){
     wx.navigateTo({
       url: '/pages/patient/message/messageitem/messageitem?nurseInfo='+JSON.stringify(this.data.bindingNurse),
     })/*
    wx.showToast({
      title: '该模块尚未开发',
      icon: 'none',
      duration: 1500
    })*/
  },
  
  consultJump2(e){
    let index=e.target.dataset.index;
    wx.navigateTo({
      url: '/pages/patient/message/messageitem/messageitem?nurseInfo='+JSON.stringify(this.data.nurseList[index]),
    })
  },

  getNurseByPatientNumber(){
    util.request(api.getNurseByPatientNumber, {
      patientnum: this.data.patientInfo.patientnumber
    }, 'GET').then(res => {
      if(res.status==200){
        if(res.data!=null){
          let data=res.data;
          data.create_time = util.timestampFormatDate(data.create_time, "yyyy-MM-dd hh:mm:ss");
          this.setData({
            noBinding:false,
            bindingNurse:data
          })
        }
      }
    }).catch((err) => {
      util.showErrorToast();
      console.log(err);
    });
  },
  getNurseExceptBinding(){
    util.request(api.getNurseExceptBinding, {
      patientnum: this.data.patientInfo.patientnumber
    }, 'GET').then(res => {
      if(res.status==200){
        this.setData({
          nurseList:res.data
        })
      }
    }).catch((err) => {
      util.showErrorToast();
      console.log(err);
    });

  },
  init(){
    this.getNurseByPatientNumber();
    this.getNurseExceptBinding();
  },
  onLoad: function (options) {
    this.setData({
      patientInfo: wx.getStorageSync('patientInfo')
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