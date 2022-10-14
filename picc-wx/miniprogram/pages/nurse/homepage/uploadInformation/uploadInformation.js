// pages/nurse/homepage/uploadInformation/uploadInformation.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    noData: false,
    appointmentList: []
  },
  //根据工号获取状态为2的受理信息
  //改变状态2为3，上传信息
  upload(e) {
    let index = this.data.appointmentList.findIndex((profile) => profile.id === e.target.dataset.id);
    wx.navigateTo({
      url: '/pages/nurse/homepage/upload/upload?appointment=' + JSON.stringify(this.data.appointmentList[index]),
    })
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
  init() {
    let nurseInfo = wx.getStorageSync('nurseInfo');
    util.request(api.getAppointmentByJobnumber, {
      jobnumber: nurseInfo.jobnumber
    }, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data.length != 0) {
          let appointmentList = res.data;
          appointmentList.sort(this.compare("ctime"));
          let list = [];
          for (let i = 0; i < appointmentList.length; i++) {
            if (appointmentList[i].status == 2) {
              appointmentList[i].appointmentDate = util.timestampFormatDate(appointmentList[i].appointmentDate, "yyyy-MM-dd");
              appointmentList[i].ctime = util.timestampFormatDate(appointmentList[i].ctime, "yyyy-MM-dd hh:mm:ss");
              appointmentList[i].step = "已完成";
              appointmentList[i].color = "color65C03D";
              list.push(appointmentList[i]);
            }
          }
          this.setData({
            appointmentList: list
          });
          if (list.length == 0) {
            this.setData({
              noData: true
            })
          }
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
      console.log(err)
    });

  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    wx.showLoading({
      title: '加载中',
    })
    this.init();
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