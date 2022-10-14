const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}
const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}
/*封装云函数的请求
function request(url, data, method) {
  return new Promise(function (resolve, reject) {
    wx.cloud.callFunction({
      name: 'request',
      data: {
        url: url,
        data: data || {},
        method: method || "GET",
        token: wx.getStorageSync('token')
      },
      success: function (res) {
        console.log(res)
        resolve(JSON.parse(res.result));
      },
      complete: res => {},
      fail: function (err) {
        reject(err)
      }
    })
  })
}*/
/**
 * 封封微信的的request  */
function request(url, data = {}, method = "GET") {
  return new Promise(function (resolve, reject) {
    wx.request({
      url: url,
      data: data||{},
      method: method,
      header: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'X-Litemall-Token': wx.getStorageSync('token')
      },
      success: function (res) {

        if (res.statusCode == 200) {

          if (res.data.errno == 501) {
            console.log('err 501: ' + JSON.stringify(res));
            // 清除登录相关内容
            try {
              wx.removeStorageSync('userInfo');
              wx.removeStorageSync('token');
            } catch (e) {
              // Do something when catch error
            }
            // 切换到登录页面
            wx.navigateTo({
              url: '/pages/common/login/login'
            });
          } else {
            resolve(res.data);
          }
        } else {
          reject(res.errMsg);
        }

      },
      fail: function (err) {
        reject(err)
      }
    })
  });
}
function redirect(url) {
  //判断页面是否需要登录
  if (false) {
    wx.redirectTo({
      url: '/pages/common/login/login'
    });
    return false;
  } else {
    wx.redirectTo({
      url: url
    });
  }
}

function showErrorToast(msg) {
  wx.showToast({
    title: msg,
    image: '/images/common/icon_error.png'
  })
}

/**
 * 时间戳转时间
 * @param {String} timestamp 时间戳
 * @return {Object} 时间
 *
 * 例如：
 * timestampToDate('1484222693'); // Thu Jan 12 2017 20:04:53 GMT+0800 (中国标准时间)
 */
function timestampToDate(timeStamp) {
  var now = new Date(parseInt(timeStamp));
  return now
}

/**
 * 获取特定格式时间
 * @param {Object} date 时间
 * @param {String} format 格式
 * @return {String} 特定格式的时间
 *
 * 例如：
 * var now = new Date(); // Mon Jan 16 2017 14:32:22 GMT+0800 (中国标准时间)
 * formatDate(now, 'yyyy-MM-dd h:m:s'); // 2017-01-16 14:32:22
 */
function formatDate(date, format) {
  let time = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds(),
    'q+': Math.floor((date.getMonth() + 3) / 3)
  }

  if (/(y+)/i.test(format)) {
    format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }

  for (let k in time) {
    if (new RegExp('(' + k + ')').test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? time[k] : ('00' + time[k]).substr(('' + time[k]).length));
    }
  }
  return format;
}

/**
 * 时间戳转特点格式时间
 * @param {String} timestamp 时间戳
 * @return {String} 特点格式时间
 *
 * 例如：
 * timestampFormatDate('1484222693', 'yyyy-MM-dd'); // 2017-01-12
 */
function timestampFormatDate(unix, format) {
  var now = timestampToDate(unix)
  return formatDate(now, format)
}

function getAge(birthday) {
  //出生时间 毫秒
  var birthDayTime = new Date(birthday).getTime();
  //当前时间 毫秒
  var nowTime = new Date().getTime();
  //一年毫秒数(365 * 86400000 = 31536000000)
  return Math.ceil((nowTime - birthDayTime) / 31536000000);
}

function getTimeStamp(birthday) {
  var date = new Date(birthday);
  var time = date.getTime();
  return time;
}

function getVerifyStatusfromServer() {
  return new Promise(function (resolve, reject) {
    let userInfo = wx.getStorageSync('userInfo');
    console.log("getVerifyStatusfromServer  userInfo.userid " + userInfo.userid);
    wx.request({
      url: 'https://www.lightcloud.club/wx/auth/status',
      data: {
        userId: userInfo.userid
      },
      method: 'GET',
      header: {
        'Content-Type': 'application/json',
        'X-Litemall-Token': wx.getStorageSync('token')
      },
      success: function (res) {
        console.log('util getVerifyStatusfromServer res' + JSON.stringify(res));
        if (res.statusCode == 200) {
          resolve(res.data.data.status);
        } else {
          reject(false);
        }

      },
      fail: function (err) {
        reject(err)
      }
    })
  });
}

module.exports = {
  formatTime: formatTime,
  request,
  redirect,
  showErrorToast,
  timestampFormatDate,
  getAge,
  getTimeStamp,
  getVerifyStatusfromServer
}