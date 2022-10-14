// pages/patient/homepage/home/home.js
Component({
  options: {
    addGlobalClass: true,
  },

  /**
   * 页面的初始数据
   */
  data: {
    elements1: [
      { title: '预约受理', name: 'appointment', color: 'cyan', icon: 'formfill' },
      { title: '信息上传', name: 'uploadInformation', color: 'grey', icon: 'formfill' },
      { title: '就诊回访 ', name: 'returnVisit', color: 'grey', icon: 'messagefill' },
      { title: '我的排期 ', name: 'mySchedule', color: 'cyan', icon: 'messagefill' }
    ],
  },
})