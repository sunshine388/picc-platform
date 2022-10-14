/**
 * vue config
 * @description
 * vue构建配置文件，[文档地址](https://cli.vuejs.org/zh/config/#vue-config-js)
 */
const argv = process.argv.splice(2);
const path = require('path');
const FileManagerWebpackPlugin = require('filemanager-webpack-plugin');

// 打包体积分析插件
const BundleAnalyzerPlugin = argv.includes('--analyzer')
  ? [new (require('webpack-bundle-analyzer').BundleAnalyzerPlugin)()]
  : [];

let plugins = [];
plugins.concat(BundleAnalyzerPlugin);
if (process.env.NODE_ENV === 'production') {
  let buildedPath = path.resolve(__dirname, './dist');
  let packageName = path.resolve(__dirname, './package');
  const fileManagerWebpackPlugin = new FileManagerWebpackPlugin({
    onEnd: [
      {
        //打包成压缩包
        archive: [
          {
            source: buildedPath,
            destination: packageName + '/picc-admin.zip'
          }
        ]
      }
    ]
  });
  plugins.push(fileManagerWebpackPlugin);
}
module.exports = {
  publicPath: process.env.VUE_APP_BASE_URL,
  productionSourceMap: false,
  filenameHashing: false, //测试net::ERR_ABORTED 404

  // 开发服务器配置
  devServer: {
    // 代理配置
    proxy: require('./proxy.config')
  },

  // webpack config
  configureWebpack: {
    plugins: plugins,
    // 引入完整版 vue，因为需要编译
    resolve: {
      alias: {
        vue$: 'vue/dist/vue.esm.js'
      }
    }
  },

  // node_modules 中需要编译的库
  transpileDependencies: [/vue-awesome/]
};
