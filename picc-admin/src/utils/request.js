import axios from 'axios';
import { stringify } from 'qs';
import { errorMessage, warnMessage } from './error-handler';
import { localDataUser } from './local-data';
import RESTURL from '@/config/rest-url';

/**
 * 创建 axios 服务实例
 * @description
 * 该方法所需的参数如下：
 * @param {String} method 请求方式 `get/post`
 * @param {Object} data post 请求的参数
 * @param {Object} params get 请求的参数
 */
const service = axios.create({
  traditional: true,
  // withCredentials: true,
  timeout: 300000,
  paramsSerializer: (params) => {
    return stringify(params, { arrayFormat: 'brackets' });
  }
});

export default service;

// 请求拦截
service.interceptors.request.use(
  (config) => {
    const source = axios.CancelToken.source();
    config.cancelToken = source.token;

    const userData = localDataUser.get();
    if (RESTURL.manager.requseLogin !== config.url) {
      if (userData) {
        config.headers['Authorization'] = userData.token;
      } else {
        warnMessage('请求已取消(未获取到 token)：' + config.url);
        // this.$message.warning('请求已取消(未获取到 token)')
        source.cancel('Cancel');
      }
    }
    if (config.method === 'get') {
      const params = (config.params = config.params || {});
      // 加上时间戳，解决ie浏览器get请求自动请求来自缓存的问题
      if (!!window.ActiveXObject || 'ActiveXObject' in window) {
        params.timestamplp = new Date().getTime();
      }
    }
    // post 参数转换为 FormData 形式
    if (config.method === 'post' && !config.headers['Content-Type']) {
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded';
      config.data = stringify(config.data);
    }

    return config;
  },
  (error) => {
    return promiseError(error, '请求错误');
  }
);

// 响应拦截
service.interceptors.response.use(
  (response) => {
    const data = response.data;
    return data;
  },
  (error) => {
    return promiseError(error);
  }
);

function promiseError(data, defaultMessage = '服务异常') {
  if (!(data instanceof axios.Cancel)) {
    errorMessage(data || defaultMessage, '', true);
  }
  return Promise.reject(data);
}
