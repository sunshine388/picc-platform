# vue-integrate

## 目录结构说明

```
picc-admin
  - public
    - config.js - 后端的服务地址配置

  - src - 主业务代码目录
    - api - 数据的请求方法
    - assets - 共用的静态资源
    - components - 共用的组件
    - layout - 主页的布局容器
    - mock - 模拟接口数据
    - plugins - 使用的插件
    - router - 路由配置
    - store - 全局的状态管理
    - styles - 全局的样式配置
    - utils - 工具函数存放目录
    - views - 主业务的页面路径
    - tests - 测试类目录
    - main.js - 入口文件

  - .env - 环境变量配置文件
  - .eslintrc.js - eslint 配置文件
  - .svnignore - svn 忽略文件
  - .babel.config.js - babel 编译配置文件
  - jest.config.js - jest 测试配置文件
  - perttier.config.js - perttier 配置文件
  - proxy.config.js - 开发服务器代理配置
  - vue.config.js - vue-cli 的配置文件
```

## 注意事项

- **组件的 `name` 与路由的 `name` 属性请坚持命名，路由切换时它们是必要的，否则会造成其他隐患问题**
- 本地存储信息全部放在了 `src/utils/local-data` 下，获取时请通过此模块获取
- 路由与导航，标签页都有关联配置：
  - 导航：读取的路由 /home 下的子路由结构，meta.icon，meta.label，其他 `isNotMenu` 控制是否展示在左边
  - 标签页：读取的当前路由中的 path，meta.label 数据，其中路由参数配置 `params.id` 则是副级标签名

```js
// 路由配置实例
const subRoutes = [
  {
    name: 'example',
    path: '/home/example',
    component: Component,
    // 注意，配置信息都在这里
    meta: {
      label: '实例', // 1.作为左侧导航的名称 2.作为标签页的命名
      icon: 'home', // 作为左侧导航的图标，el-*，只写后面的，这里是 el-home
    },
    // 这里是子路由（子菜单）
    children: [
      {
         name: '...',
         path: '...',
         meta: {
           ...
         }
      }
    ]
  }
]

// 跳转时设置标签页的子名称
this.$router.push({name: 'example', params: {id: '副名称'}})
```

## 开始

- `npm start` 开启开发服务器
- `npm run build` 进行打包
- `npm run build-analyzer` 进行打包与体积分析
- `npm run lint` 运行代码校验
- `npm run test` 运行单元测试
