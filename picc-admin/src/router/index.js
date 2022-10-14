/**
 * 路由配置
 */
import Vue from 'vue';
import Router from 'vue-router';

// 主容器组件
import Layout from '../layout';

// 登陆/注册
import Login from '@/views/login';

import { localDataUser } from '@/utils/local-data.js';

// 子模块的路由注册
export const subRoutes = [
  {
    path: '/user',
    name: 'user',
    component: { render: (h) => h('router-view') },
    redirect: '/user/nurses',
    meta: {
      label: '用户管理'
    },
    children: [
      {
        path: '/user/nurses',
        name: 'nurses',
        component: () => import('@/views/user/nurses'),
        meta: {
          label: '护士列表'
        }
      },
      {
        path: '/user/patients',
        name: 'patients',
        component: () => import('@/views/user/patients'),
        meta: {
          label: '患者列表'
        }
      },
      {
        path: '/user/managers',
        name: 'managers',
        component: () => import('@/views/user/managers'),
        meta: {
          label: '管理员列表'
        }
      }
    ]
  },
  {
    path: '/identityCheck',
    name: 'identityCheck',
    component: { render: (h) => h('router-view') },
    redirect: '/identityCheck/nurseCheck',
    meta: {
      label: '身份审核'
    },
    children: [
      {
        path: '/identityCheck/nurseCheck',
        name: 'nurseCheck',
        component: () => import('@/views/identityCheck/nurseCheck'),
        meta: {
          label: '护士身份'
        }
      }
    ]
  },
  {
    path: '/binding',
    name: 'binding',
    component: { render: (h) => h('router-view') },
    redirect: '/binding/patientBinding',
    meta: {
      label: '绑定信息'
    },
    children: [
      {
        path: '/binding/patientBinding',
        name: 'patientBinding',
        component: () => import('@/views/binding/patientBinding'),
        meta: {
          label: '患者绑定'
        }
      }
    ]
  },
  {
    path: '/schedule',
    name: 'schedule',
    component: { render: (h) => h('router-view') },
    redirect: '/schedule/nurseSchedule',
    meta: {
      label: '排班管理'
    },
    children: [
      {
        path: '/schedule/nurseSchedule',
        name: 'nurseSchedule',
        component: () => import('@/views/schedule/schedule'),
        meta: {
          label: '护士排班'
        }
      }
    ]
  },
  {
    path: '/record',
    name: 'record',
    component: { render: (h) => h('router-view') },
    redirect: '/record/record1',
    meta: {
      label: '记录查看'
    },
    children: [
      {
        path: '/record/record1',
        name: 'record1',
        component: () => import('@/views/record/record'),
        meta: {
          label: '记录查看'
        }
      }
    ]
  },
  {
    path: '/statistic',
    name: 'statistic',
    component: { render: (h) => h('router-view') },
    redirect: '/statistic/statistic1',
    meta: {
      label: '信息统计'
    },
    children: [
      {
        path: '/statistic/statistic1',
        name: 'statistic1',
        component: () => import('@/views/statistic/statistic'),
        meta: {
          label: '统计报表'
        }
      }
    ]
  },
  {
    path: '/opinion',
    name: 'opinion',
    component: { render: (h) => h('router-view') },
    redirect: '/opinion/opinion1',
    meta: {
      label: '留言板管理'
    },
    children: [
      {
        path: '/opinion/opinion1',
        name: 'opinion1',
        component: () => import('@/views/opinion/opinion'),
        meta: {
          label: '留言板管理'
        }
      }
    ]
  }
];

// 路由集合
const routes = [
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/',
    name: 'home',
    component: Layout,
    redirect: '/user/nurses',
    // 子模块的路由注册
    children: subRoutes.concat(
      {
        path: '/record/personalCatheter',
        name: 'personalCatheter',
        component: () => import('@/views/record/personalCatheter')
      },
      {
        path: '/record/personalMaintain',
        name: 'personalMaintain',
        component: () => import('@/views/record/personalMaintain')
      },
      {
        path: '/record/personalExtubation',
        name: 'personalExtubation',
        component: () => import('@/views/record/personalExtubation')
      },
      {
        path: '/record/newPersonalCatheter',
        name: 'newPersonalCatheter',
        component: () => import('@/views/record/newPersonalCatheter')
      },
      {
        path: '/record/newPersonalMaintain',
        name: 'newPersonalMaintain',
        component: () => import('@/views/record/newPersonalMaintain')
      },
      {
        path: '/record/newPersonalExtubation',
        name: 'newPersonalExtubation',
        component: () => import('@/views/record/newPersonalExtubation')
      },
      {
        path: '/record/personalRecord',
        name: 'personalRecord',
        component: () => import('@/views/record/personalRecord')
      }
    ),
    beforeEnter: (to, from, next) => {
      if (localDataUser.get()) {
        next();
      } else {
        next('/login');
      }
    }
  },
  {
    path: '**',
    redirect: '/'
  }
];

Vue.use(Router);

export default new Router({
  // mode: 'history',
  scrollBehavior: () => ({ x: 0, y: 0 }),
  routes
});
