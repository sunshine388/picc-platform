// pages/nurse/homepage/upload/upload.js
var api = require('../../../../config/api.js');
var util = require('../../../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    type: 0,
    appointment: {},
    nurseInfo: {},
    index1: null,
    index2: null,
    index3: null,
    index4: null,
    index5: null,
    index6: null,
    index7: null,
    index8:null,
    picker1: ['首次化疗前', '第二次化疗前', '第三次化疗及以上'], //置管时机
    picker2: ['双腔三向瓣模式', '单腔三向瓣模式'], //导管型号
    picker3: ['盲穿', '赛丁格技术', 'B超引导下'], //穿刺方法
    picker4: ['肘上', '肘下'], //置管部位
    picker5: ['左侧', '右侧'], //置管手臂
    picker6: ['贵要静脉', '肘正中静脉', '头静脉'], //置管静脉
    picker7: ['气管隆突上', '气管隆突下', 'B超引导下'], //导管尖端位置
    picker8: ['本院', '外院'], //导管尖端位置
    diseaseType: '', //疾病类型
    chemotherapyNumber: '', //化疗次数
    firstCatheter: 'true', //是否首次置管
    catheterTime: '', //置管时间
    catheterSpecification: '', //导管规格
    catheterBatchnumber: '', //导管批号
    catheterLength: '', //导管长度
    catheterInsertionLength: '', //导管插入长度
    catheterExposedLength: '', //导管外露长度
    armCircumference: '', //臂围
    catheterBrand:'',
    numberOfCatheterization:0,
    catheterHospital:'',

    maintainDate: '', //维护日期
    pipelineUnobstructed: 'true', //导管是否通畅
    changeDressing: 'true', //是否更换敷料
    replaceConnector: 'true', //是否更换接头
    resistance: 'false', //有无阻力
    companion: '无', //并发症
    hasCompanion:'false',
    treatmentProcess: '', //处理方法
    bloodReturn:'',
    index9:null,
    picker9:['导管相关性血栓','导管相关性血流感染','导管堵塞','静脉炎','继发性导管异位','局部皮肤湿疹','皮肤过敏反应','淋巴漏','其他'],

    extubationDate: '', //拔管日期
    indwellingTime: '', //导管留置时间
    reason: '', //拔管原因
    endIntact:'',
    processSmooth:'',
    //时间选择器
    minDate: new Date(new Date().getFullYear() - 1, 0, 1).getTime(),
    maxDate: new Date().getTime(),
    currentDate: new Date().getTime(),
    index10:null,
    picker10:['正常拔管','非计划拔管'],

    time1: '',
    time2: '',
    time3: '',
    //弹出框显示
    show1: false,
    show2: false,
    show3: false,

    nurseInfo:wx.getStorageSync('nurseInfo'),
    recordnum:''
  },
  isEmpty(obj) {
    if (typeof obj == "undefined" || obj == null || obj == "") {
      return true;
    } else {
      return false;
    }
  },
  createPatientCatheter() {
    let verify=this.isEmpty(this.data.index1)||this.isEmpty(this.data.index2)||this.isEmpty(this.data.index3)||this.isEmpty(this.data.index4)||this.isEmpty(this.data.index5)||this.isEmpty(this.data.index6)||this.isEmpty(this.data.index7)||this.isEmpty(this.data.diseaseType)||this.isEmpty(this.data.chemotherapyNumber)||this.isEmpty(this.data.firstCatheter)||this.isEmpty(this.data.catheterTime)||this.isEmpty(this.data.catheterSpecification)||this.isEmpty(this.data.catheterBatchnumber)||this.isEmpty(this.data.catheterLength)||this.isEmpty(this.data.catheterInsertionLength)||this.isEmpty(this.data.catheterExposedLength)||this.isEmpty(this.data.armCircumference)
    if (verify) {
      wx.showToast({
        title: '请完善表单!',
        icon: 'none',
        duration: 1500
      });
    } else {
      let params = {
        appointmentid: this.data.appointment.id,
        patientName: this.data.appointment.patientName,
        patientNum: this.data.appointment.patientNumber,
        diseaseType: this.data.diseaseType, //疾病类型
        chemotherapyNumber: this.data.chemotherapyNumber, //化疗次数
        firstCatheter: this.data.firstCatheter, //是否首次置管
        catheterTime: this.data.catheterTime, //置管时间
        catheterDepartment: this.data.nurseInfo.department, //置管科室
        catheterOpportunity: this.data.picker1[this.data.index1], //置管时机
        catheterModel: this.data.picker2[this.data.index2], //导管型号
        catheterSpecification:this.data.catheterSpecification, //导管规格
        catheterBatchnumber: this.data.catheterBatchnumber, //导管批号
        catheterLength: this.data.catheterLength, //导管长度
        punctureMethod: this.data.picker3[this.data.index3], //穿刺方法
        catheterSite: this.data.picker4[this.data.index4], //置管部位
        catheterArm: this.data.picker5[this.data.index5], //置管手臂
        catheterVein: this.data.picker6[this.data.index6], //置管静脉
        catheterTipPosition: this.data.picker7[this.data.index7], //导管尖端位置
        catheterInsertionLength: this.data.catheterInsertionLength, //导管插入长度
        catheterExposedLength: this.data.catheterExposedLength, //导管外露长度
        armCircumference: this.data.armCircumference, //臂围
        catheter_brand:this.data.catheterBrand,
        nurse_jobnumber:this.data.nurseInfo.jobnumber,
        catheter_hospital:this.data.index8==1?this.data.catheterHospital: this.data.picker8[this.data.index8],
        number_of_catheterization:this.data.numberOfCatheterization
      }
      util.request(api.createPatientCatheter, params, 'POST').then(res => {
        if (res.status == 200) {
          wx.showToast({
            title: '上传成功!',
            icon: 'none',
            duration: 1500,
            success(data) {
              setTimeout(function () {
                wx.navigateBack()
              }, 1000)
            }
          });
        }
      }).catch((err) => {
        wx.showToast({
          title: '请求失败',
          image: '/images/common/icon_error.png',
          duration: 1500
        })
        console.log(err)
      });
    }
  },
  createPatientMaintain() {
    let verify=this.isEmpty(this.data.maintainDate);
    if (verify) {
      wx.showToast({
        title: '请完善表单!',
        icon: 'none',
        duration: 1500
      });
    } else {
      let params = {
        appointmentid: this.data.appointment.id,
        patientName: this.data.appointment.patientName,
        patientNum: this.data.appointment.patientNumber,
        maintainDate: this.data.maintainDate, //维护日期
        pipelineUnobstructed: this.data.pipelineUnobstructed, //导管是否通畅
        changeDressing: this.data.changeDressing, //是否更换敷料
        replaceConnector: this.data.replaceConnector, //是否更换接头
        resistance: this.data.resistance, //有无阻力
        companion: this.data.index9==8?this.data.companion: this.data.picker9[this.data.index9], //并发症
        treatmentProcess: this.data.treatmentProcess, //处理方法
        blood_return:this.data.bloodReturn,
        catheter_exposed_length:this.data.catheterExposedLength,
        nurse_jobnumber:this.data.nurseInfo.jobnumber,
        record_num:this.data.recordnum,
      }
      util.request(api.createPatientMaintain, params, 'POST').then(res => {
        if (res.status == 200) {
          wx.showToast({
            title: '上传成功!',
            icon: 'none',
            duration: 1500,
            success(data) {
              setTimeout(function () {
                wx.navigateBack()
              }, 1000)
            }
          });
        }
      }).catch((err) => {
        wx.showToast({
          title: '请求失败',
          image: '/images/common/icon_error.png',
          duration: 1500
        })
        console.log(err)
      });
    }
  },
  createPatientExtubation() {
    let verify=this.isEmpty(this.data.extubationDate)||this.isEmpty(this.data.indwellingTime)||this.isEmpty(this.data.reason);
    if (verify) {
      wx.showToast({
        title: '请完善表单!',
        icon: 'none',
        duration: 1500
      });
    } else {
      let params = {
        appointmentid: this.data.appointment.id,
        patientName: this.data.appointment.patientName,
        patientNum: this.data.appointment.patientNumber,
        extubationDate: this.data.extubationDate, //拔管日期
        indwellingTime: this.data.indwellingTime, //导管留置时间
        reason:this.data.index10==1?(this.data.picker10[this.data.index10]+":"+this.data.reason): this.data.picker10[this.data.index10], //拔管原因
        end_intact:this.data.endIntact,
        process_smooth:this.data.processSmooth,
        nurse_jobnumber:this.data.nurseInfo.jobnumber,
        record_num:this.data.recordnum,
      }
      util.request(api.createPatientExtubation, params, 'POST').then(res => {
        if (res.status == 200) {
          wx.showToast({
            title: '上传成功!',
            icon: 'none',
            duration: 1500,
            success(data) {
              setTimeout(function () {
                wx.navigateBack()
              }, 1000)
            }
          });
        }
      }).catch((err) => {
        wx.showToast({
          title: '请求失败',
          image: '/images/common/icon_error.png',
          duration: 1500
        })
        console.log(err)
      });
    }
  },
  upload() {
    if (this.data.type == 1) {
      this.createPatientCatheter();
    } else
    if (this.data.type == 2) {
      this.createPatientMaintain();
    } else
    if (this.data.type == 3) {
      this.createPatientExtubation();
    }
  },
  PickerChange1(e) {
    this.setData({
      index1: e.detail.value
    })
  },
  PickerChange2(e) {
    this.setData({
      index2: e.detail.value
    })
  },
  PickerChange3(e) {
    this.setData({
      index3: e.detail.value
    })
  },
  PickerChange4(e) {
    this.setData({
      index4: e.detail.value
    })
  },
  PickerChange5(e) {
    this.setData({
      index5: e.detail.value
    })
  },
  PickerChange6(e) {
    this.setData({
      index6: e.detail.value
    })
  },
  PickerChange7(e) {
    this.setData({
      index7: e.detail.value
    })
  },  
  PickerChange8(e) {
    this.setData({
      index8: e.detail.value
    });
  },
  PickerChange9(e) {
    this.setData({
      index9: e.detail.value
    });
  },
  PickerChange10(e) {
    this.setData({
      index10: e.detail.value
    });
  },
  onChange(value){
    this.setData({ numberOfCatheterization: value });
  },
  showPopup1() {
    this.setData({
      show1: true
    });
  },
  showPopup2() {
    this.setData({
      show2: true
    });
  },
  showPopup3() {
    this.setData({
      show3: true
    });
  },
  cancel1() {
    this.setData({
      show1: false
    })
  },
  cancel2() {
    this.setData({
      show2: false
    })
  },
  cancel3() {
    this.setData({
      show3: false
    })
  },
  // 确认置管时间
  confirm1(e) {
    console.log(e)
    const date = new Date(e.detail);
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let time = `${year}-${month}-${day}`;
    this.setData({
      time1: time,
      catheterTime:date.getTime(),
      show1: false
    })
  },
  // 确认维护时间
  confirm2(e) {
    const date = new Date(e.detail);
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let time = `${year}-${month}-${day}`;
    this.setData({
      time2: time,
      maintainDate:date.getTime(),
      show2: false
    })
  },
  // 确认拔管时间
  confirm3(e) {
    const date = new Date(e.detail);
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let time = `${year}-${month}-${day} ${hour}:${minute}`;
    this.setData({
      time3: time,
      extubationDate:date.getTime(),
      show3: false
    })
  },
  radioChange1(e) {
    this.setData({
      firstCatheter:e.detail.value
    })
  },
  radioChange2(e) {
    this.setData({
      pipelineUnobstructed:e.detail.value
    })
  },
  radioChange3(e) {
    this.setData({
      changeDressing:e.detail.value
    })
  },
  radioChange4(e) {
    this.setData({
      replaceConnector:e.detail.value
    })
  },
  radioChange5(e) {
    this.setData({
      resistance:e.detail.value
    })
  },
  radioChange6(e) {
    this.setData({
      hasCompanion:e.detail.value,
      companion:''
    })
  },
  radioChange7(e) {
    this.setData({
      bloodReturn:e.detail.value
    })
  },
  radioChange8(e) {
    this.setData({
      endIntact:e.detail.value
    })
  },
  radioChange9(e) {
    this.setData({
      processSmooth:e.detail.value
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      nurseInfo: wx.getStorageSync('nurseInfo')
    })
    let appointment = JSON.parse(options.appointment);
    if (appointment.type == "置管") {
      this.setData({
        appointment: appointment,
        type: 1
      })
    }
    if (appointment.type == "维护") {
      this.setData({
        appointment: appointment,
        type: 2
      })
    }
    if (appointment.type == "拔管") {
      this.setData({
        appointment: appointment,
        type: 3
      })
    }
    util.request(api.getNextMaintainTime, {
      patientnum: appointment.patientNumber
    }, 'GET').then(res => {
      if (res.status == 200) {
        if(res.data){
          let data=res.data;
          this.setData({
            recordnum:data.recordnum
          })
        }else{
          wx.showModal({
            title: '提示',
            content: '该用户尚未上传置管信息，是否先上传置管信息',
            success (res) {
              if (res.confirm) {
                this.setData({
                  type:1
                })
              } else if (res.cancel) {
                wx.navigateBack();
              }
            }
          })
        }
      }
    })
    //console.log(appointment)
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