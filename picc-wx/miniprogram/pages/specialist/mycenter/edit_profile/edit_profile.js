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
    workUnit:'',
    username: '',
    sex: '',
    phone: '',
    checked:true,
    index:null
  },
  
  PickerChange(e) {
    this.setData({
      index: e.detail.value
    });
  },
  formSubmit: function(e) {
    let that = this;
    if (e.detail.value.username.length == 0 || e.detail.value.username.length >= 8) {
      wx.showToast({
        title: '姓名不能为空或过长!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function() {
        wx.hideToast()
      }, 2000)
    } else if (e.detail.value.workUnit.length==0) {
      wx.showToast({
        title: '请输入工作单位!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else if (e.detail.value.phone.length< 11 || e.detail.value.phone.length > 11) {
      wx.showToast({
        title: '请输入正确手机号!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else {
      util.request(api.SpecialistInfoUpdate,{
        skey: wx.getStorageSync('token'),
        workUnit: e.detail.value.workUnit,
        username: e.detail.value.username,
        sex: e.detail.value.sex,
        phone: e.detail.value.phone
      }, 'POST').then(res=>{
        if (res.status == 200) {
          let specialistInfo= wx.getStorageSync('specialistInfo');
          specialistInfo.specialistNumber=e.detail.value.specialistNumber;
          specialistInfo.username=e.detail.value.username;
          specialistInfo.sex=e.detail.value.sex;
          specialistInfo.phone=e.detail.value.phone;
          specialistInfo.workUnit= e.detail.value.workUnit;
          wx.setStorageSync('specialistInfo',specialistInfo);
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
    util.request(api.getDepartment, {}, 'GET').then(res => {
      if (res.status==200) {
        const specialistInfo= wx.getStorageSync('specialistInfo');
        if(specialistInfo.sex=='男'){
          this.setData({checked:true})
        }else{
          this.setData({checked:false})
        }
        this.setData({
          workUnit: specialistInfo.workUnit,
          username: specialistInfo.username,
          phone: specialistInfo.phone,
          sex: specialistInfo.sex
        });
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