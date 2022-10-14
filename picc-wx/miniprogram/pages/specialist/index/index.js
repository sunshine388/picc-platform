
Page({
  data: {
    PageCur: 'homepage'
  },
  NavChange(e) {
    this.setData({
      PageCur: e.currentTarget.dataset.cur
    })
  },
  onShareAppMessage: function () {

  }
})