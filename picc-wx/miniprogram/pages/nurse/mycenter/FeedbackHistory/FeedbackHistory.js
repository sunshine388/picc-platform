// pages/nurse/mycenter/FeedbackHistory/FeedbackHistory.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    opinionList: [],
    noData: false,
    activeNames1: [],
    activeNames2: [],
    loading:true
  },
  onChange1(event) {
    this.setData({
      activeNames1: event.detail,
    });
  },
  onChange2(event) {
    this.setData({
      activeNames2: event.detail,
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中',
    })
    let skey = wx.getStorageSync('token');
    util.request(api.getOpinion, {
      skey: skey
    }, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data.length!=0) {
          let opinionList = res.data;
          let list=[];
          opinionList.sort(this.compare("ctime"));
          for (let i = 0; i < opinionList.length; i++) {
            if(opinionList[i].role=="本院职工"){
            opinionList[i].ctime = util.timestampFormatDate(opinionList[i].ctime, "yyyy-MM-dd hh:mm:ss")
            opinionList[i].whetherToSolve = opinionList[i].whetherToSolve == "true" ? true : false;
            list.push(opinionList[i]);
            }
          }
          this.setData({
            opinionList: list,
            loading:false
          })
        } else {
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
  compare(prop) {
    return function (obj1, obj2) {
      var val1 = obj1[prop];
      var val2 = obj2[prop];
      if (!isNaN(Number(val1)) && !isNaN(Number(val2))) {
        val1 = Number(val1);
        val2 = Number(val2);
      }
      if (val1 > val2) {
        return -1;
      } else if (val1 < val2) {
        return 1;
      } else {
        return 0;
      }
    }
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