// pages/patient/homepage/appointment/appointment.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    index1: null,
    index2: null,
    index3: null,
    index4: null,
    type: ['置管', '维护', '拔管'],
    department: [],
    hasData: false,
    nurseList: [],
    appointment: [],
    checked: false,
    patientInfo: {}
  },
  PickerChange1(e) {
    this.setData({
      index1: e.detail.value
    })
  },
  PickerChange2(e) {
    this.setData({
      index2: e.detail.value
    });
    let department = this.data.department[this.data.index2];
    util.request(api.getNurseByDepartment, {
      department: department
    }, 'GET').then(res => {
      if (res.status) {
        this.setData({
          nurseList: res.data
        })
      }
    }).catch((err) => {
      util.showErrorToast('请求失败');
      console.log(err);
    });
  },
  PickerChange3(e) {
    this.setData({
      index3: e.detail.value
    });
    let jobnumber = this.data.nurseList[this.data.index3].jobnumber;
    const date = new Date();
    const today = new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime();
    util.request(api.getSchedulebyjobnumber, {
      jobnumber: jobnumber
    }, 'GET').then(res => {
      if (res.status==200) {
        let appointment = res.data;
        appointment.sort(this.compare("appointmentDate"));
        let list = [];
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
          if (appointment[i].forenoon || appointment[i].afternoon) {
            list.push(appointment[i]);
          }
        }
        this.setData({
          appointment: list
        })
      }
    }).catch((err) => {
      util.showErrorToast('请求失败');
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
      if (val1 < val2) {
        return -1;
      } else if (val1 > val2) {
        return 1;
      } else {
        return 0;
      }
    }
  },
  PickerChange4(e) {
    this.setData({
      index4: e.detail.value
    })
  },
  onClick() {
    if (this.data.department.length==0) {
      wx.showToast({
        title: '未获取到数据，请稍后再试!',
        icon: 'none',
        duration: 1500
      })
    }
  },
  onClick1() {
    if (!this.data.index2) {
      wx.showToast({
        title: '请先选择预约科室!',
        icon: 'none',
        duration: 1500
      })
    }
  },
  onClick2() {
    if (!this.data.index3) {
      wx.showToast({
        title: '请先选择预约医生!',
        icon: 'none',
        duration: 1500
      })
    } else {
      const checked = !this.data.checked;
      this.setData({
        checked: checked
      })
    }
  },
  appointment1(e) {
    if (this.data.index1 == null) {
      wx.showToast({
        title: '请选择预约类型!',
        icon: 'none',
        duration: 1500
      })
    } else {
      let params = {
        patientnum: this.data.patientInfo.patientnumber,
        patientname: this.data.patientInfo.username,
        nursename: this.data.nurseList[this.data.index3].nursename,
        jobnumber: this.data.nurseList[this.data.index3].jobnumber,
        date: this.data.appointment[e.target.dataset.index].appointmentDate,
        time: '上午',
        type: this.data.type[this.data.index1],
        scheduleId: e.target.dataset.id,
      };
      this.appointment(params);
    }

  },
  appointment2(e) {
    if (this.data.index1 == null) {
      wx.showToast({
        title: '请选择预约类型!',
        icon: 'none',
        duration: 1500
      })
    } else {
      let params = {
        patientnum: this.data.patientInfo.patientnumber,
        patientname: this.data.patientInfo.username,
        nursename: this.data.nurseList[this.data.index3].nursename,
        jobnumber: this.data.nurseList[this.data.index3].jobnumber,
        date: this.data.appointment[e.target.dataset.index].appointmentDate,
        time: '下午',
        type: this.data.type[this.data.index1],
        scheduleId: e.target.dataset.id,
      };
      this.appointment(params);
    }
  },
  appointment(params) {
    util.request(api.createAppointment, params, 'POST').then(res => {
      if (res.status == 200) {
        if (res.data == "false") {
          wx.showToast({
            title: res.msg,
            icon: 'none',
            duration: 1500
          })
        } else {
          wx.showToast({
            title: '预约成功!',
            icon: 'none',
            duration: 1500,
            success(data) {
              setTimeout(function () {
                wx.navigateTo({
                  url: '/pages/patient/mycenter/appointmentRecord/appointmentRecord',
                })
              }, 1000)
            }
          })
        }
      }
    }).catch((err) => {
      util.showErrorToast('请求失败');
      console.log(err);
    });
  },
  appointmentRecordJump() {
    wx.navigateTo({
      url: '/pages/patient/mycenter/appointmentRecord/appointmentRecord',
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let patientInfo = wx.getStorageSync('patientInfo');
    this.setData({
      patientInfo: patientInfo
    })
    util.request(api.getDepartment, {}, 'GET').then(res => {
      if (res.status==200) {
        this.setData({
          department: res.data,
          hasData:true
        })
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