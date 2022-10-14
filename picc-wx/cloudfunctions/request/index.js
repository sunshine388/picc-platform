// 云函数入口文件
const cloud = require('wx-server-sdk')

var rp = require('request-promise');
cloud.init()

// 云函数入口函数
exports.main = async (event, context) => {
  //const url='https://snowyyy.com/picc-server/common/get_department'
  let method = event.method;
  if (method == "POST") {
    let options = {
      url: event.url,
      method: "POST",
      json: true,
      form: event.data || {},
      headers: {
        //"content-Type": "application/json",
        'X-Litemall-Token': event.token,
        "content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
      }
    };
    return await rp(options)
    .then(function (res) {
      return res
    })
    .catch(function (err) {
      return '请求失败'
    });

  } else if (method == "GET") {
    let options = {
      url: event.url,
      method: "GET",
      form: event.data || {},
      dataType: 'json',
      responseType: 'text',
      headers: {
        "content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
        'X-Litemall-Token': event.token
      }
    };
    let url=event.url
    if(event.data){
      let str=url=url+'?';
      for(let key in event.data){
        str=str+key+"="+String(event.data[key])+"&";
      }
      url=str.substring(0,str.length-1);
    }
    return await rp(url)
    .then(function (res) {
      return res
    })
    .catch(function (err) {
      return err
    });
  }
}