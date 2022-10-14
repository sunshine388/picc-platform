// pages/patient/information/information.js
var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
const check = require('../../../utils/check.js');

const app = getApp();
var submitting = false;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    username: '',
    stature:'',
    weight:'',
    sex: '',
    phone: '',
    index:null,
    address:'',
    date: '1999-1-1',
    picker: ['文盲', '小学', '初中', '高中', '大专及以上'],
    region: ['湖北省', '武汉市', '洪山区'],
  },
  updateDataBase(number,userInfo){
    const db = wx.cloud.database(); //引用数据库
    const _ = db.command
    db.collection('userInfo').doc(number).set({
      data:userInfo
    }).then(res => {
      console.log(res)
    }).catch(err => {
      console.log(err)
    })
  },
  DateChange(e) {
    this.setData({
      date: e.detail.value
    })
  },
  PickerChange(e) {
    this.setData({
      index: e.detail.value
    })
  },
  RegionChange: function(e) {
    this.setData({
      region: e.detail.value
    })
  },
  formSubmit: function (e) {
    let that = this;
    var birthday = that.data.date;
    if (submitting) {
      console.log("on submitting ...");
      return;
    }
    submitting = true;
    //console.log(e.detail.value);
    if (e.detail.value.username.length == 0 || e.detail.value.username.length >= 8) {
      submitting = false;
      wx.showToast({
        title: '姓名不能为空或过长!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else if (e.detail.value.address.length == 0 ) {
      submitting = false;
      wx.showToast({
        title: '请输入详细地址!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else if (!check.isValidPhone(e.detail.value.phone)) {
      submitting = false;
      wx.showToast({
        title: '请输入正确手机号!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    } else if(that.data.index==null){
      submitting = false;
      wx.showToast({
        title: '请选择学历!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else {
      util.request(api.PatientInfoSubmit,{
        skey: wx.getStorageSync('token'),
        username: e.detail.value.username,
        culture:  that.data.picker[that.data.index],
        city:  that.data.region[2],
        phone: e.detail.value.phone,
        address: e.detail.value.address,
        sex: e.detail.value.sex,
        birthday: birthday
      }, 'POST').then(res => {
          submitting = false;
          if (res.status == 200) {
            this.patientRoleCheck();
          }
        }).catch((err) => {
          util.showErrorToast('请求失败');
          console.log(err);
        });
    }
  },
  patientRoleCheck(){
    util.request(api.patientRoleCheck, {
      skey: wx.getStorageSync('token'),
    }).then(res => {
      if (res.status == 200) {
        if (res.data != null) {
          let userInfo = wx.getStorageSync('userInfo');
          userInfo.role = 'patient';
          app.globalData.role = 'patient';
          wx.setStorageSync('userInfo', userInfo);
          wx.setStorageSync('patientInfo', res.data);
          let number=res.data.patientnumber;
          let user={username:res.data.username,avatarUrl:userInfo.avatarUrl}
          this.updateDataBase(number,user);
          wx.redirectTo({
            url: '/pages/patient/index/index',
          })
        }
      } else {
        console.log(res.msg)
      }
    }).catch((err) => {
      util.showErrorToast('请求失败');
      console.log(err);
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
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