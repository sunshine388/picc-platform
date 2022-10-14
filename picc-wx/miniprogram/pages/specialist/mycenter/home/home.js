var app = getApp();
Component({
  data: {
    specialistInfo:{},
    userInfo:{}
  },
  lifetimes: {
    attached: function() {
      this.setData({
        specialistInfo: wx.getStorageSync('specialistInfo'),
        userInfo: wx.getStorageSync('userInfo')
      });
    },
    detached: function() {
      // 在组件实例被从页面节点树移除时执行
    },
  },
  methods:{
    editProfileJump(){
      wx.navigateTo({
        url: '/pages/specialist/mycenter/edit_profile/edit_profile',
      })
    },
    FeedbackJump(){
      wx.navigateTo({
        url: '/pages/specialist/mycenter/Feedback/Feedback',
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
            wx.removeStorageSync('specialistInfo');
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