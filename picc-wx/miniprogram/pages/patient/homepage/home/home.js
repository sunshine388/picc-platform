// pages/patient/homepage/home/home.js
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
    elements: [{
        title: '预约挂号',
        name: 'appointment',
        color: 'cyan',
        icon: 'formfill'
      },
      {
        title: '咨询医生 ',
        name: 'consult',
        color: 'grey',
        icon: 'messagefill'
      },
      //{
      //  title: '健康知识',
      //  name: 'knowledge',
      ///  color: 'grey',
      //  icon: 'formfill'
      //},
      //{
      //  title: '常见并发症 ',
      //  name: 'complication',
      //  color: 'cyan',
       // icon: 'messagefill'
      //}
    ],
    hasCatheter: true,
    isTimeout: false,
    date: '',
    days: 0,
    patientInfo: {},
    isClock: false
  },
  lifetimes: {
    attached: function () {
      wx.showLoading({
        title: '加载中',
      })
      let patientInfo = wx.getStorageSync('patientInfo');
      const date = new Date();
      const today = new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime();
      util.request(api.getNextMaintainTime, {
        patientnum: patientInfo.patientnumber
      }, 'GET').then(res => {
        if (res.status == 200) {
          if (res.data) {
            if (res.data.hasCatheter == 'false') {
              this.setData({
                hasCatheter: res.data.hasCatheter == 'false' ? false : true,
                isClock: true
              })
            } else {
              let date1 = util.timestampFormatDate(res.data.date, "yyyy-MM-dd");
              const date2 = new Date(date1);
              let usedTime = date2 - today;
              // 计算相差的天数 
              let days = Math.floor(usedTime / (24 * 3600 * 1000));
              if (days >= 0) {
                this.setData({
                  date: date1,
                  isTimeout: false,
                  days: days,
                  isClock: true
                })
              } else {
                this.setData({
                  date: date1,
                  isTimeout: true,
                  days: Math.abs(days),
                  isClock: true
                })
              }
            }
          }else {
            this.setData({
              hasCatheter: false,
              isClock: true
            })
          }
        } else {
          this.setData({
            hasCatheter: false,
            isClock: true
          })
        }
        wx.hideLoading();

      }).catch((err) => {
        util.showErrorToast('请求失败');
        console.log(err);
      });
    },
    detached: function () {
      // 在组件实例被从页面节点树移除时执行
    },
  },
})