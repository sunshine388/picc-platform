var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    extubationInfo: {},
    noData: false,
    loading:true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中',
    })
    util.request(api.getPatientExtubationNew + "/" + options.num, {}, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data) {
          let extubationInfo = res.data[0];
          extubationInfo.extubation_date = util.timestampFormatDate(extubationInfo.extubation_date, "yyyy-MM-dd hh:mm:ss");
          extubationInfo.end_intact = extubationInfo.end_intact == "true" ? '是' : '否';
          extubationInfo.process_smooth = extubationInfo.process_smooth == "true" ? '是' : '否';
          this.setData({
            extubationInfo: extubationInfo,
            loading:false
          })
        }else {
          this.setData({
            noData: true
          })
        }
        wx.hideLoading();
      }
    }).catch((err) => {
      wx.showToast({
        title: '请求失败',
        image: '/images/common/icon_error.png',
        duration: 1500,
        success(data) {
          setTimeout(function () {
            wx.navigateBack();
          }, 1000)
        }
      })
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