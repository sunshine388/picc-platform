// pages/patient/mycenter/home/home.js
var app = getApp();
Component({
  data: {
    patientInfo:{},
    userInfo:{}
  },
  lifetimes: {
    attached: function() {
      this.setData({
        patientInfo: wx.getStorageSync('patientInfo'),
        userInfo: wx.getStorageSync('userInfo')
      });
      //console.log(this.data.patientInfo);
      //console.log(this.data.userInfo);
    },
    detached: function() {
      // 在组件实例被从页面节点树移除时执行
    },
  },
  methods:{
    editProfileJump(){
      wx.navigateTo({
        url: '/pages/patient/mycenter/edit_profile/edit_profile',
      })
    },
    appointmentRecordJump(){
      wx.navigateTo({
        url: '/pages/patient/mycenter/appointmentRecord/appointmentRecord',
      })
    },
    catheterRecordJump(){
      wx.navigateTo({
        url: '/pages/patient/mycenter/catheterRecord/catheterRecord',
      })
    },
    maintainRecordJump(){
      wx.navigateTo({
        url: '/pages/patient/mycenter/maintainRecord/maintainRecord',
      })
    },
    extubationRecordJump(){
      wx.navigateTo({
        url: '/pages/patient/mycenter/extubationRecord/extubationRecord',
      })
    },
    recordJump(){
      wx.navigateTo({
        url: '/pages/patient/mycenter/piccRecord/piccRecord',
      })
    },
    FeedbackJump(){
      wx.navigateTo({
        url: '/pages/patient/mycenter/Feedback/Feedback',
      })
    },
    logout(){
      wx.showModal({
        title: '退出提示',
        content: '确定要退出登录吗？',
        success (res) {
          if (res.confirm) {
            let userInfo = wx.getStorageSync('userInfo');
            userInfo.role = '';
            app.globalData.role = '';
            wx.setStorageSync('userInfo', userInfo);
            wx.removeStorageSync('patientInfo');
            wx.redirectTo({
              url: '/pages/common/index/index',
            })
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })
    }
  }
})