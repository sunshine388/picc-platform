// pages/nurse/mycenter/myPatient/myPatient.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({
  data: {
    nurseInfo: {},
    patientList: [],
    activeNames: ['1'],
    number: '',
    patientname: '',
    noData: true,
    radio: '1',
  },

  radioChange(event) {
    this.setData({
      radio: event.detail,
    });
  },
  binding(){
    let params={
      patientnum: this.data.number,
      jobnumber:this.data.nurseInfo.jobnumber
    }
    util.request(api.createBinding,params, 'POST').then(res => {
      let that=this;
      if (res.status == 200) {
        wx.showToast({
          title: res.msg,
          icon: 'none',
          duration: 1500,
          success(data) {
            setTimeout(function () {
              that.init();
              that.setData({
                activeNames: ['1'],
                number: '',
                patientname: '',
                radio: '1',
              })
            }, 1000)
          }
        })
      } else {
        wx.showToast({
          title: res.msg,
          icon: 'none',
          duration: 1500
        })
      }
    }).catch((err) => {
      util.showErrorToast();
      console.log(err);
      reject(err);
    });
  },
  search() {
    if (this.data.number == '') {
      wx.showToast({
        title: '请输入患者编号!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    } else {
      util.request(api.getPatientByPatientNumber, {
        patientnumber: this.data.number
      }, 'GET').then(res => {
        if (res.status == 200) {
          this.setData({
            patientname: res.data.username
          })
        } else {
          wx.showToast({
            title: res.msg,
            icon: 'none',
            duration: 1500
          })
        }
      }).catch((err) => {
        util.showErrorToast();
        console.log(err);
      });
    }
  },
  onChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  init(){
    util.request(api.getBindingByJobNumber, {
      jobnumber: this.data.nurseInfo.jobnumber
    }, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data.length != 0) {
          this.setData({
            patientList: res.data,
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