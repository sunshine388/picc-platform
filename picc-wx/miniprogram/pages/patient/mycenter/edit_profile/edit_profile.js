// pages/patient/mycenter/edit_profile/edit_profile.js
var provinces = require('../../../../utils/provinces');
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
const check = require('../../../../utils/check.js');

const app = getApp();
var submitting = false;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    username: '',
    sex: '',
    phone: '',
    index:null,
    date: '1999-1-1',
    picker: ['文盲', '小学', '初中', '高中', '大专及以上'],
    region: ['浙江省', '杭州市', '下城区'],
    checked: true,
    address:''
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
      util.request(api.PatientInfoUpdate,{
        skey: wx.getStorageSync('token'),
        username: e.detail.value.username,
        culture:  that.data.picker[that.data.index],
        city:  that.data.region[2],
        phone: e.detail.value.phone,
        address:e.detail.value.address,
        sex: e.detail.value.sex,
        birthday: birthday
      }, 'POST').then(res => {
          submitting = false;
          if (res.status == 200) {
            let patientInfo= wx.getStorageSync('patientInfo');
            patientInfo.username=e.detail.value.username;
            patientInfo.culture= that.data.picker[that.data.index];
            patientInfo.city=that.data.region[2];
            patientInfo.phone= e.detail.value.phone;
            patientInfo.address=e.detail.value.address,
            patientInfo.sex= e.detail.value.sex;
            patientInfo.birthday= birthday;
            wx.setStorageSync('patientInfo',patientInfo);
            wx.showToast({
              title: '成功修改个人资料!',
              icon: 'none',
              duration: 1500,
              success(data) {
                setTimeout(function () {
                  //要延时执行的代码
                  wx.navigateBack()
                }, 1000) //延迟时间
              }
            })
          }
        }).catch((err) => {
          util.showErrorToast('请求失败');
          console.log(err);
        });
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const patientInfo= wx.getStorageSync('patientInfo');
    let region=[];
    provinces.forEach((item,index) => {
      item.city.forEach((itt, idx) => {
        itt.districtAndCounty.forEach((itd, idd) => {
          if(patientInfo.city==itd){
            region[0]=item.name;
            region[1]=itt.name;
            region[2]=itd;
          }
        })
      })
    })
    let index;
    for(let i=0;i<this.data.picker.length;i++){
      if(this.data.picker[i]==patientInfo.culture){index=i}
    }
    if(patientInfo.sex=='男'){
      this.setData({checked:true})
    }else{
      this.setData({checked:false})
    }
    this.setData({
      username: patientInfo.username,
      index:  index,
      phone: patientInfo.phone,
      address:patientInfo.address,
      sex: patientInfo.sex,
      date: patientInfo.birthday,
      region: region
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