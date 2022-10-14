// pages/patient/mycenter/appointmentRecord/appointmentRecord.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    appointmentList: [],
    noData: false,
    loading: true
  },
  cancel(e) {
    let that = this;
    wx.showModal({
      title: '提示',
      content: '确定要取消预约吗？',
      success(res) {
        if (res.confirm) {
          let params = {
            id: e.target.dataset.id
          }
          util.request(api.cancelAppointment, params, 'POST').then(res => {
            if (res.status == 200) {
              wx.showToast({
                title: '取消成功!',
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
            util.showErrorToast();
            console.log(err);
          });
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },
  delete(e) {
    let that = this;
    wx.showModal({
      title: '提示',
      content: '确定要删除该条记录吗？',
      success(res) {
        if (res.confirm) {
          let params = {
            id: e.target.dataset.id
          }
          util.request(api.deleteAppointment, params, 'delete').then(res => {
            if (res.status == 200) {
              wx.showToast({
                title: '删除成功!',
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
            util.showErrorToast();
            console.log(err);
          });
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },
  init() {
    wx.showLoading({
      title: '加载中',
    })
    let patientInfo = wx.getStorageSync('patientInfo');
    util.request(api.getAppointment, {
      patientnum: patientInfo.patientnumber
    }, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data.length!=0) {
          let appointmentList = res.data;
          appointmentList.sort(this.compare("ctime"));
          for (let i = 0; i < appointmentList.length; i++) {
            appointmentList[i].showCancelBtn = false;
            appointmentList[i].showDeleteBtn = false;
            appointmentList[i].appointmentDate = util.timestampFormatDate(appointmentList[i].appointmentDate, "yyyy-MM-dd");
            appointmentList[i].ctime = util.timestampFormatDate(appointmentList[i].ctime, "yyyy-MM-dd hh:mm:ss");
            if (appointmentList[i].status == -1) {
              appointmentList[i].step = "已取消";
              appointmentList[i].color = "colorF03131";
              appointmentList[i].showDeleteBtn = true;
            } else if (appointmentList[i].status == 0) {
              appointmentList[i].step = "已预约";
              appointmentList[i].color = "color5984F5";
              appointmentList[i].showCancelBtn = true;
            } else if (appointmentList[i].status == 1) {
              appointmentList[i].step = "诊疗中";
              appointmentList[i].color = "colorE77C2D";
            } else {
              appointmentList[i].step = "已完成";
              appointmentList[i].color = "color65C03D";
            }
          }
          this.setData({
            appointmentList: appointmentList,
            loading: false
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
      });
      console.log(err);
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
  //预约信息：-1：取消预约，0：已预约，1：诊疗中，2：已完成, 3:已上传记录
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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