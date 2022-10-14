// pages/nurse/homepage/mySchedule/mySchedule.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    appointment: [],
    noData: false
  },

  compare(prop) {
    return function (obj1, obj2) {
      var val1 = obj1[prop];
      var val2 = obj2[prop];
      if (!isNaN(Number(val1)) && !isNaN(Number(val2))) {
        val1 = Number(val1);
        val2 = Number(val2);
      }
      if (val1 < val2) {
        return -1;
      } else if (val1 > val2) {
        return 1;
      } else {
        return 0;
      }
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let jobnumber = wx.getStorageSync('nurseInfo').jobnumber;
    const date = new Date();
    const today = new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime();
    util.request(api.getSchedulebyjobnumber, {
      jobnumber: jobnumber
    }, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data.length != 0) {
          let appointment = res.data;
          appointment.sort(this.compare("appointmentDate"));
          for (let i = 0; i < appointment.length; i++) {
            const date1 = new Date(Number(appointment[i].appointmentDate));
            appointment[i].date = date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate();
            // 两个时间戳相差的毫秒数
            let usedTime = appointment[i].appointmentDate - today;
            // 计算相差的天数 
            let days = Math.floor(usedTime / (24 * 3600 * 1000));
            if (days == 1) {
              appointment[i].label = "明天";
            } else if (days == 2) {
              appointment[i].label = "后天";
            } else {
              appointment[i].label = days + "天后";
            }
          }
          this.setData({
            appointment: appointment
          })
        }else{
          this.setData({
            noData:true
          })
        }
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