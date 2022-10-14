// pages/specialist/homepage/home/home.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Component({
  options: {
    addGlobalClass: true,
  },
  /**
   * 页面的初始数据
   */
  data: {
    bindingNurse: {},
    nurseList: [],
    specialistInfo: {},
    noBinding: true,
  },
  methods: {

    consultJump(e) {
      let index = e.target.dataset.index;
      wx.navigateTo({
        url: '/pages/specialist/message/messageitem/messageitem?nurseInfo=' + JSON.stringify(this.data.nurseList[index]),
      })
    },

    
    getAllNurse() {
      util.request(api.getAllNurse, {
      }, 'GET').then(res => {
        if (res.status == 200) {
          this.setData({
            nurseList: res.data
          })
        }
      }).catch((err) => {
        util.showErrorToast();
        console.log(err);
      });

    },
    init() {
      this.getAllNurse();
    },
  },
  lifetimes: {
    attached: function () {
      this.setData({
        specialistInfo: wx.getStorageSync('specialistInfo')
      });
      this.init();
    },
    detached: function () {
      // 在组件实例被从页面节点树移除时执行
    },
  },
})