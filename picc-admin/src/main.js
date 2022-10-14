import Vue from 'vue';

import App from './App.vue';
import store from './store';
import router from './router';

import 'fullcalendar/dist/fullcalendar.css';
// 插件
import './plugins/element.js';
// 样式
import './styles/index.scss';
import Print from 'vue-print-nb';
Vue.use(Print);

import echarts from 'echarts';

Vue.prototype.$echarts = echarts;
new Vue({
  el: '#app',
  store,
  router,
  render: (h) => h(App)
});
