const app = getApp()

Page({
  data: {
    chatRoomGroupName:'',
    logged: false,
    takeSession: false,
    requestResult: '',
    
    chatRoomGroupId: 'demo',
    patientnumber:'',
    jobnumber:'',

    // functions for used in chatroom components
    onGetUserInfo: null,
    getOpenID: null,

  },

  onLoad: function(e) {
    let nurseInfo=JSON.parse(e.nurseInfo);
    let patientInfo=wx.getStorageSync('patientInfo');
    let chatRoomGroupId=nurseInfo.jobnumber+'-'+patientInfo.patientnumber;
    this.setData({
      chatRoomGroupId:chatRoomGroupId,
      patientnumber:patientInfo.patientnumber,
      jobnumber:nurseInfo.jobnumber,
      chatRoomGroupName:nurseInfo.nursename
    })

    this.setData({
      onGetUserInfo: this.onGetUserInfo,
      getOpenID: this.getOpenID,
    })

    wx.getSystemInfo({
      success: res => {
        console.log('system info', res)
        if (res.safeArea) {
          const { top, bottom } = res.safeArea
          this.setData({
            containerStyle: `padding-top: ${(/ios/i.test(res.system) ? 10 : 20) + top}px; padding-bottom: ${20 + res.windowHeight - bottom}px`,
          })
        }
      },
    })
  },

  getOpenID: async function() {
    if (this.openid) {
      return this.openid
    }
    const { result } = await wx.cloud.callFunction({
      name: 'login',
    })
    return result.openid
  },

  onGetUserInfo: function(e) {
    console.log(e);
    if (!this.logged && e.detail.userInfo) {
      this.setData({
        logged: true,
        //avatarUrl: e.detail.userInfo.avatarUrl,
        //userInfo: e.detail.userInfo
      })
    }
  },
})



/*Page({
  data: {
    InputBottom: 0
  },
  InputFocus(e) {
    this.setData({
      InputBottom: e.detail.height
    })
  },
  InputBlur(e) {
    this.setData({
      InputBottom: 0
    })
  }
})*/