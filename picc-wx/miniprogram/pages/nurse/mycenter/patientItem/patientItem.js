// pages/nurse/mycenter/patientItem/patientItem.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    noData1: true,
    noData2: true,
    noData: true,
    recordInfo: [],
    activeNames: [],
    loading: true,
    piccInformation: {},
    loading:true,
    patientname:'',
    patientnum:'',

    picker1:['PICC','输液港','其他'],
    picker2:['心脏病','糖尿病','高血压','传染病','其他','无'],
    index1:null,
    index2:null,
  },
  
  PickerChange1(e) {
    this.setData({
      index1: e.detail.value
    });
  },
  PickerChange2(e) {
    this.setData({
      index2: e.detail.value
    });
  },
  formSubmit: function(e) {
    let that = this;
    if (e.detail.value.animalHeat.length==0) {
      wx.showToast({
        title: '请输入体温!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else if (!this.data.index1) {
      wx.showToast({
        title: '请选择导管类型!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function() {
        wx.hideToast()
      }, 2000)
    } else if (this.data.index1==2&&e.detail.value.catheterType.length==0) {
      wx.showToast({
        title: '请输入导管类型!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function() {
        wx.hideToast()
      }, 2000)
    }else if (!this.data.index2) {
      wx.showToast({
        title: '请选择既往史!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function() {
        wx.hideToast()
      }, 2000)
    } else if (this.data.index2==4&&e.detail.value.previousHistory.length==0) {
      wx.showToast({
        title: '请输入既往史!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function() {
        wx.hideToast()
      }, 2000)
    }  else if (e.detail.value.drugAllergyHistory.length==0) {
      wx.showToast({
        title: '请输入药物过敏史!',
        icon: 'none',
        duration: 1500
      })
      setTimeout(function () {
        wx.hideToast()
      }, 2000)
    }else {
      let that=this;
      util.request(api.updatePatientInformation,{
        patientnum: this.data.patientnum,
        patientname: this.data.patientname,
        animalHeat: e.detail.value.animalHeat,
        drugAllergyHistory: e.detail.value.drugAllergyHistory,
        catheterType:this.data.index1==2?e.detail.value.catheterType: this.data.picker1[this.data.index1],
        previousHistory:this.data.index2==4?e.detail.value.previousHistory: this.data.picker2[this.data.index2]
      }, 'POST').then(res=>{
        if (res.status == 200) {
          wx.showToast({
            title: '成功提交!',
            icon: 'none',
            duration: 1500,
            success(data) {
              setTimeout(function () {
                wx.showLoading({
                  title: '加载中',
                })
                that.getPatientRecord();
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
  getPatientRecord(){
    util.request(api.getPatientRecord + "/" + this.data.patientnum, {}, 'GET').then(res => {
      if (res.status == 200) {
        if (res.data.record.length != 0) {
          let recordInfo = res.data.record;
          recordInfo.sort(this.compare("ctime"))
          for (let i = 0; i < recordInfo.length; i++) {
            recordInfo[i].ctime = util.timestampFormatDate(recordInfo[i].ctime, "yyyy-MM-dd");
          }
          if (res.data.patientname) {
            this.setData({
              piccInformation: res.data,
              recordInfo: recordInfo,
              noData1: false,
              noData2: false,
              loading:false
            })
          } else {
            this.setData({
              recordInfo: recordInfo,
              noData1: true,
              noData2: false,
              loading:false
            })
          }
        } else {
          if (res.data.patientname) {
            this.setData({
              piccInformation: res.data,
              noData1: false,
              noData2: true,
              loading:false
            })
          } else {
            this.setData({
              noData1: true,
              noData2: true,
              loading:false
            })
          }
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
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      patientnum:options.num,
      patientname:options.name
    })
    wx.showLoading({
      title: '加载中',
    })
    this.getPatientRecord();
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