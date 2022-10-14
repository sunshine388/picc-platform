// pages/index/index.js
var api = require('../../../config/api.js');
var util = require('../../../utils/util.js');
var app = getApp();
Page({

  data: {},
  onLoad: function (options) {},
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
  becomeNurse: function () {
    util.request(api.nurseRoleCheck, {
      skey: wx.getStorageSync('token'),
    }).then(res => {
      if (res.status == 200) {
        if (res.data != null) {
          if (res.data.state == "1") {
            let userInfo = wx.getStorageSync('userInfo');
            userInfo.role = 'nurse';
            app.globalData.role = 'nurse';
            wx.setStorageSync('userInfo', userInfo);
            wx.setStorageSync('nurseInfo', res.data);
            let number=res.data.jobnumber;
            let user={username:res.data.username,avatarUrl:userInfo.avatarUrl}
            this.updateDataBase(number,user);
            wx.redirectTo({
              url: '/pages/nurse/index/index',
            })
          } else {
            wx.navigateTo({
              url: '/pages/nurse/audit_result/audit_result?state='+res.data.state,
            })
          }
        } else {
          wx.navigateTo({
            url: '/pages/nurse/nurse_info/nurse_info'
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
  becomeSpeciaList: function () {
    util.request(api.specialistRoleCheck, {
      skey: wx.getStorageSync('token'),
    }).then(res => {
      if (res.status == 200) {
        if (res.data != null) {
          if (res.data.state == "1") {
            let userInfo = wx.getStorageSync('userInfo');
            userInfo.role = 'specialist';
            app.globalData.role = 'specialist';
            wx.setStorageSync('userInfo', userInfo);
            wx.setStorageSync('specialistInfo', res.data);
            let number=res.data.specialistNumber;
            let user={username:res.data.username,avatarUrl:userInfo.avatarUrl}
            this.updateDataBase(number,user);
            wx.redirectTo({
              url: '/pages/specialist/index/index',
            })
          } else {
            wx.navigateTo({
              url: '/pages/specialist/audit_result/audit_result?state='+res.data.state,
            })
          }
        } else {
          // wx.navigateTo({
          //   url: '/pages/specialist/specialist_info/specialist_info'
          // })
          wx.navigateTo({
            url: '/pages/specialist/UploadCertification/UploadCertification'
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
  becomePatient: function () {
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
        } else {
          wx.navigateTo({
            url: '/pages/patient/patient_info/patient_info'
          })
        }
      } else {
        console.log(res.msg)
      }
    }).catch((err) => {
      util.showErrorToast('请求失败');
      console.log(err);
    });
  }
})