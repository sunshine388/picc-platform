var util = require('../utils/util.js');
var api = require('../config/api.js')

/**
 * Promise封装wx.checkSession
 */
function checkSession() {
  return new Promise(function(resolve, reject) {
    wx.checkSession({
      success: function() {
        resolve(true);
      },
      fail: function() {
        reject(false);
      }
    })
  });
}

/**
 * Promise封装wx.login
 */
function login() {
  return new Promise(function(resolve, reject) {
    wx.login({
      success: function(res) {
        if (res.code) {
          resolve(res);
        } else {
          reject(res);
        }
      },
      fail: function(err) {
        reject(err);
      }
    });
  });
}

/**
 * 调用微信登录
 */
function loginByWeixin(userInfo) {
  console.log("start loginByWeixin");
  return new Promise(function(resolve, reject) {
    return login().then((res) => {
      //登录远程服务器
      console.log("start loginByWeixin res.code = " + res.code + " userInfo = " + userInfo.nickName);
      wx.getUserInfo({
        success: function(info_res) {
          util.request(api.AuthLoginByWeixin, {
            code: res.code, //临时登录凭证
            rawData: info_res.rawData, //用户非敏感信息
            signature: info_res.signature, //签名
            encrypteData: info_res.encryptedData, //用户敏感信息
            iv: info_res.iv //解密算法的向量
          }, 'POST').then(res => {
            if (res.status==200) {
              //存储用户信息
              wx.setStorageSync('token', res.data);
              resolve(res);
            } else {
              console.log("fail loginByWeixin res = ", res);
              reject(res);
            }
          }).catch((err) => {
            console.log("fail loginByWeixin res = reject 1 err = ", err);
            reject(err);
          });
        }
      })
    }).catch((err) => {
      console.log("fail loginByWeixin res = reject 2", err);
      reject(err);
    })
  });
}

/**
 * 判断用户是否登录
 */
function checkLogin() {
  return new Promise(function(resolve, reject) {
    if (wx.getStorageSync('userInfo') && wx.getStorageSync('token')) {
      console.log("check login with wx");
      checkSession().then(() => {
        resolve(true);
      }).catch(() => {
        reject(false);
      });
    } else {
      console.log("check login with false");
      reject(false);
    }
  });
}

module.exports = {
  checkLogin,
  loginByWeixin
};