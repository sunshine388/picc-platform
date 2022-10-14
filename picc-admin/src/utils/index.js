export function formatDate(date, fmt) {
  try {
    if (!date) {
      return '';
    }
    let dateTime;
    if (typeof date === 'string') {
      dateTime = new Date(Number(date));
      if (JSON.stringify(dateTime) === 'null') {
        return date;
      }
    } else if (typeof date === 'number') {
      dateTime = new Date(date);
    } else {
      dateTime = date;
    }

    var o = {
      'M+': dateTime.getMonth() + 1, //月份
      'd+': dateTime.getDate(), //日
      'h+': dateTime.getHours(), //小时
      'm+': dateTime.getMinutes(), //分
      's+': dateTime.getSeconds(), //秒
      'q+': Math.floor((dateTime.getMonth() + 3) / 3), //季度
      S: dateTime.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt))
      fmt = fmt.replace(
        RegExp.$1,
        (dateTime.getFullYear() + '').substr(4 - RegExp.$1.length)
      );
    for (var k in o)
      if (new RegExp('(' + k + ')').test(fmt))
        fmt = fmt.replace(
          RegExp.$1,
          RegExp.$1.length === 1
            ? o[k]
            : ('00' + o[k]).substr(('' + o[k]).length)
        );
    return fmt;
  } catch (e) {
    console.log(e);
    return new Date();
  }
}
//输入日期,获取n天后的yyyy-mm-dd
export function GetDateStr(day, n) {
  var dd = new Date(day);
  dd.setDate(dd.getDate() + n);
  var y = dd.getFullYear();
  const m = dd.getMonth() + 1;
  const d = dd.getDate();
  //var m =dd.getMonth() + 1 < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;
  //var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
  return y + '-' + m + '-' + d;
}

//根据出生日期获取年龄
export function getAge(birthday) {
  //出生时间 毫秒
  var birthDayTime = new Date(birthday).getTime();
  //当前时间 毫秒
  var nowTime = new Date().getTime();
  //一年毫秒数(365 * 86400000 = 31536000000)
  return Math.ceil((nowTime - birthDayTime) / 31536000000);
}
