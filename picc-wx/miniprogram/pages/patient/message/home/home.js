// pages/patient/consult/home/home.js
Component({

  /**
   * 页面的初始数据
   */
  data: {
    noData: false,
    chatList: [],
    openId: '',
    collection: 'lastMessage',
  },
  methods: {
    async init() {
      this.try(async () => {
        await this.initOpenID();
        const db = wx.cloud.database();
        const _ = db.command;
        
        const myNumber=wx.getStorageSync('patientInfo').patientnumber;
        const {
          data: initList
        } = await db.collection(this.data.collection).where({
          myNumber: _.eq(myNumber)
        }).orderBy('sendTimeTS', 'desc').get()
        console.log('init query chats', initList);
        const date = new Date();
        const today = new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime();
        for (let i = 0; i < initList.length; i++) {
          const date1 = new Date(Number(initList[i].sendTimeTS));
          // 两个时间戳相差的毫秒数
          let usedTime = today-initList[i].sendTimeTS;
          // 计算相差的天数 
          let days = Math.floor(usedTime / (24 * 3600 * 1000));
          initList[i].days = days;
          if (days == -1) {
            const hours = date1.getHours();//获取当前时（(0-23）
            const minutes = date1.getMinutes();//获取当前分钟数(0-59)
            initList[i].label =hours+":"+minutes;
          } else if (days == 0) {
            initList[i].label = "昨天";
          } else if (days == 1) {
            initList[i].label = "前天";
          } else {
            const month = date1.getMonth()+1;//获取当前时（(0-23）
            const date = date1.getDate();//获取当前分钟数(0-59)
            initList[i].label = month+'-'+date;
          }
        }
        this.setData({
          chatList:initList
        })
        if(initList.length==0){
          this.setData({
            noData:true
          })
        }
        wx.hideLoading()
      }, '获取失败')
    },
    async initOpenID() {
      return this.try(async () => {
        const openId = await this.getOpenID()
        this.setData({
          openId,
        })
      }, '初始化 openId 失败')
    },
    getOpenID: async function () {
      if (this.openid) {
        return this.openid
      }
      const {
        result
      } = await wx.cloud.callFunction({
        name: 'login',
      })
      return result.openid
    },
    messageItemJump(e) {
      console.log(e); //index:e.currentTarget.dataset.target
      
      const nurse = {
        nursename: this.data.chatList[e.currentTarget.dataset.index].name,
        jobnumber: this.data.chatList[e.currentTarget.dataset.index].otherNumber,
      }
      wx.navigateTo({
        url: '/pages/patient/message/messageitem/messageitem?nurseInfo='+JSON.stringify(nurse),
      })
    },
    // ListTouch触摸开始
    ListTouchStart(e) {
      this.setData({
        ListTouchStart: e.touches[0].pageX
      })
    },

    // ListTouch计算方向
    ListTouchMove(e) {
      this.setData({
        ListTouchDirection: e.touches[0].pageX - this.data.ListTouchStart > 0 ? 'right' : 'left'
      })
    },

    // ListTouch计算滚动
    ListTouchEnd(e) {
      if (this.data.ListTouchDirection == 'left') {
        this.setData({
          modalName: e.currentTarget.dataset.target
        })
      } else {
        this.setData({
          modalName: null
        })
      }
      this.setData({
        ListTouchDirection: null
      })
    },
    async try (fn, title) {
      try {
        await fn()
      } catch (e) {
        this.showError(title, e)
      }
    },

    showError(title, content, confirmText, confirmCallback) {
      console.error(title, content)
      wx.showModal({
        title,
        content: content.toString(),
        showCancel: confirmText ? true : false,
        confirmText,
        success: res => {
          res.confirm && confirmCallback()
        },
      })
    },
  },
  ready() {
    wx.showLoading({
    title: '加载中',
  })
    this.init()
  },
})