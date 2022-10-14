// pages/nurse/homepage/appointment/appointment.js
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

  treat(e) {
    let that = this;
    let params = {
      id: e.target.dataset.id,
      status: 1
    }
    util.request(api.updateAppointment, params, 'POST').then(res => {
      if (res.status == 200) {
        wx.showToast({
          title: '点击成功!',
          icon: 'none',
          duration: 1500,
          success(data) {
            setTimeout(function () {
              that.init();
            }, 1000)
          }
        });
      }

    }).catch((err) => {
      wx.showToast({
        title: '请求失败',
        image: '/images/common/icon_error.png',
        duration: 1500
      })
      console.log(err)
    });
  },
  complete(e) {
    let that = this;
    let params = {
      id: e.target.dataset.id,
      status: 2
    }
    util.request(api.updateAppointment, params, 'POST').then(res => {
      if (res.status == 200) {
        wx.showToast({
          title: '点击成功!',
          icon: 'none',
          duration: 1500,
          success(data) {
            setTimeout(function () {
              that.init();
            }, 1000)
          }
        });
      }

    }).catch((err) => {
      wx.showToast({
        title: '请求失败',
        image: '/images/common/icon_error.png',
        duration: 1500
      })
      console.log(err)
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
            if (appointmentList[i].status == 0 || appointmentList[i].status == 1) {
              appointmentList[i].showYyBtn = false;
              appointmentList[i].showZlBtn = false;
              appointmentList[i].appointmentDate = util.timestampFormatDate(appointmentList[i].appointmentDate, "yyyy-MM-dd");
              appointmentList[i].ctime = util.timestampFormatDate(appointmentList[i].ctime, "yyyy-MM-dd hh:mm:ss");
              if (appointmentList[i].status == 0) {
                appointmentList[i].step = "已预约";
                appointmentList[i].color = "color5984F5";
                appointmentList[i].showYyBtn = true;
              } else if (appointmentList[i].status == 1) {
                appointmentList[i].step = "诊疗中";
                appointmentList[i].color = "colorE77C2D";
                appointmentList[i].showZlBtn = true;
              }
              list.push(appointmentList[i]);
            }
          }
          this.setData({
            appointmentList: list
          })
          if(list.length==0){
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
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中',
    })
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