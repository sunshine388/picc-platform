import Vue from 'vue';

// 这里隔离 element-ui 的包，因为它可能存在项目中了
// import { Message } from 'element-ui';

const logMethods = {
  // eslint-disable-next-line no-console
  info: console.info,
  // eslint-disable-next-line no-console
  error: console.error,
  // eslint-disable-next-line no-console
  warning: console.warn,
  // eslint-disable-next-line no-console
  success: console.log
};

/**
 * 信息提示
 * @param {String} [type] info/error/warning/success
 * @param {*} message
 * @param {String} title
 * @param {Boolean} [showBox]
 * @return {String}
 */
export function messageBox(
  type = 'info',
  message = '',
  title = '',
  showBox = false
) {
  // 直接使用 $message
  const Message = Vue.prototype.$message;

  // 打印出完整错误信息
  logMethods[type] && logMethods[type](`[门户 ${type}]:`, message);

  if (showBox) {
    // 格式化进行展示
    message = formatterMessage(message);
    if (title) message = `${title}（${message}）`;

    // 显示错误信息
    Message({
      message,
      type,
      duration: 6000,
      showClose: true
    });
  }

  return message;
}

// 提示信息
export const infoMessage = messageBox.bind(null, 'info');

// 成功信息
export const successMessage = messageBox.bind(null, 'success');

// 错误信息
export const errorMessage = messageBox.bind(null, 'error');

// 警告信息
export const warnMessage = messageBox.bind(null, 'warning');

/**
 * 格式化错误信息（限制 80 字）
 * @param {*} msg
 * @return {string}
 */
function formatterMessage(msg) {
  if (msg) {
    if (msg.error) {
      msg = msg.error;
    }
    if (msg.message) {
      msg = msg.message;
    }
  }
  if (msg instanceof Object && !(msg instanceof Error)) {
    msg = formatterMessage(msg);
  } else {
    msg = String(msg);
  }
  msg = msg.length > 80 ? msg.slice(0, 80) + '...' : msg;

  return msg;
}
