// pages/nurse/homepage/appointmentHistory/appointmentHistory.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({
  /**
   * 页面的初始数据
   */
  data: {
    noData: false,
    appointmentList: [],
    activeNames: [],
  },
  onChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  returnVisitJump(e){
    let appointment=this.data.appointmentList[e.target.dataset.index];
    let patientInfo={
      patientname:appointment.patientName,
      patientnumber:appointment.patientNumber
    }
    wx.navigateTo({
      url: '/pages/nurse/message/messageitem/messageitem?patientInfo='+JSON.stringify(patientInfo),
    })
  },
  onOpen(e){
    const index=e.target.dataset.index;
    const type = this.data.appointmentList[index].type;
    const id = this.data.appointmentList[index].id;
    const appointment = this.data.appointmentList[index];
    if (type == '置管') {
      util.request(api.getRecord, {
        id: id
      }, 'GET').then(res => {
        if (res.status == 200) {
          appointment.first_catheter=res.data.first_catheter=="true"?"是":"否"
          appointment.catheter_time = util.timestampFormatDate(res.data.catheter_time, "yyyy-MM-dd hh:mm:ss");
          appointment.arm_circumference = res.data.arm_circumference;
          appointment.catheter_arm = res.data.catheter_arm;
          appointment.catheter_batchnumber = res.data.catheter_batchnumber;
          appointment.catheter_department = res.data.catheter_department;
          appointment.catheter_exposed_length = res.data.catheter_exposed_length;
          appointment.catheter_insertion_length = res.data.catheter_insertion_length;
          appointment.catheter_length = res.data.catheter_length;
          appointment.catheter_model = res.data.catheter_model;
          appointment.catheter_opportunity = res.data.catheter_opportunity;
          appointment.catheter_site = res.data.catheter_site;
          appointment.catheter_specification = res.data.catheter_specification;
          appointment.catheter_tip_position = res.data.catheter_tip_position;
          appointment.catheter_vein = res.data.catheter_vein;
          appointment.chemotherapy_number = res.data.chemotherapy_number;
          appointment.disease_type = res.data.disease_type;
          appointment.puncture_method = res.data.puncture_method;
          appointment.loading=false;
          let updateTodo = `appointmentList[${index}]`
          this.setData({
            [updateTodo]:appointment
          })
        }
      }).catch((err) => {
        wx.showToast({
          title: '请求失败',
          image: '/images/common/icon_error.png',
          duration: 1500
        })
        console.log(err)
      });
    } else if (type == '维护') {
      util.request(api.getRecord, {
        id: id
      }, 'GET').then(res => {
        if (res.status == 200) {
          appointment.loading=false;
          appointment.maintain_date = util.timestampFormatDate(res.data.maintain_date, "yyyy-MM-dd");
          appointment.pipeline_unobstructed = res.data.pipeline_unobstructed == "true" ? '是' : '否';
          appointment.change_dressing = res.data.change_dressing == "true" ? '是' : '否';
          appointment.replace_connector = res.data.replace_connector == "true" ? '是' : '否';
          appointment.resistance = res.data.resistance == "true" ? '有' : '无';
          appointment.bfz = res.data.companion == "无" ? false : true;
          appointment.companion= res.data.companion;
          let updateTodo = `appointmentList[${index}]`
          this.setData({
            [updateTodo]:appointment
          })
        }
      }).catch((err) => {
        wx.showToast({
          title: '请求失败',
          image: '/images/common/icon_error.png',
          duration: 1500
        })
        console.log(err)
      });
    } else if (type == '拔管') {
      util.request(api.getRecord, {
        id: id
      }, 'GET').then(res => {
        if (res.status == 200) {
          appointment.indwelling_time = res.data.indwelling_time;
          appointment.reason = res.data.reason;
          appointment.extubation_date = util.timestampFormatDate(res.data.extubation_date, "yyyy-MM-dd hh:mm:ss");
          appointment.loading=false;
          let updateTodo = `appointmentList[${index}]`
          this.setData({
            [updateTodo]:appointment
          })
        }
      }).catch((err) => {
        wx.showToast({
          title: '请求失败',
          image: '/images/common/icon_error.png',
          duration: 1500
        })
        console.log(err)
      });
    }
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
            if (appointmentList[i].status == 3) {
              appointmentList[i].appointmentDate = util.timestampFormatDate(appointmentList[i].appointmentDate, "yyyy-MM-dd");
              appointmentList[i].ctime = util.timestampFormatDate(appointmentList[i].ctime, "yyyy-MM-dd hh:mm:ss");
              appointmentList[i].loading = true;
              list.push(appointmentList[i]);
            }
          }
          this.setData({
            appointmentList: list
          })
          //this.appointmentInit();
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
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中',
    })
    this.init();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {},

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