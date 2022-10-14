var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    maintainInfo: [],
    noData: false,
    activeNames:[],
    loading:true
  },
  onChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中',
    })
    util.request(api.getPatientMaintainNew + "/" + options.num, {}, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data.length!=0) {
          let maintainInfo = res.data;
          maintainInfo.sort(this.compare("maintain_date"))
          for (let i = 0; i < maintainInfo.length; i++) {
            maintainInfo[i].maintain_date = util.timestampFormatDate(maintainInfo[i].maintain_date, "yyyy-MM-dd");
            maintainInfo[i].pipeline_unobstructed = maintainInfo[i].pipeline_unobstructed == "true" ? '是' : '否';
            maintainInfo[i].change_dressing = maintainInfo[i].change_dressing == "true" ? '是' : '否';
            maintainInfo[i].replace_connector = maintainInfo[i].replace_connector == "true" ? '是' : '否';
            maintainInfo[i].resistance = maintainInfo[i].resistance == "true" ? '有' : '无';
            maintainInfo[i].companion = maintainInfo[i].companion == "无" ? false : maintainInfo[i].companion;
          }
          this.setData({
            maintainInfo: maintainInfo,
            loading:false
          })
        } else {
          this.setData({
            noData: true
          })
        }
        wx.hideLoading();
      }
    }).catch((err) => {
      wx.showToast({
        title: '请求失败',
        image: '/images/common/icon_error.png',
        duration: 1500,
        success(data) {
          setTimeout(function () {
            wx.navigateBack();
          }, 1000)
        }
      })
      console.log(err);
    });

  },
  compare(prop) {
    return function (obj1, obj2) {
      var val1 = obj1[prop];
      var val2 = obj2[prop];
      if (!isNaN(Number(val1)) && !isNaN(Number(val2))) {
        val1 = Number(val1);
        val2 = Number(val2);
      }
      if (val1 > val2) {
        return -1;
      } else if (val1 < val2) {
        return 1;
      } else {
        return 0;
      }
    }
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