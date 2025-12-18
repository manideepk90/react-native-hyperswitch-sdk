const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');

const rootDir = path.resolve(__dirname, '..');
const appDirectory = rootDir;

const babelLoaderConfiguration = {
  test: /\.[jt]sx?$/,
  include: [
    path.resolve(appDirectory, 'index.js'),
    path.resolve(appDirectory, 'App.tsx'),
    path.resolve(appDirectory, 'src'),
    path.resolve(appDirectory, 'web'),
    path.resolve(appDirectory, 'node_modules/react-native-lib-demo'),
    path.resolve(appDirectory, 'node_modules/@react-native/new-app-screen'),
    path.resolve(appDirectory, 'node_modules/@callstack/repack'),
    path.resolve(appDirectory, 'node_modules/react-native'),
  ],
  use: {
    loader: 'babel-loader',
    options: {
      cacheDirectory: true,
      presets: ['module:@react-native/babel-preset'],
      plugins: ['react-native-web'],
    },
  },
};

module.exports = {
  entry: path.resolve(appDirectory, 'web', 'index.js'),
  output: {
    path: path.resolve(appDirectory, 'dist'),
    filename: 'bundle.web.js',
    publicPath: '/',
  },
  module: {
    rules: [
      babelLoaderConfiguration,
      {
        test: /\.(png|jpe?g|gif|svg)$/i,
        type: 'asset/resource',
      },
    ],
  },
  resolve: {
    alias: {
      'react-native$': 'react-native-web',
    },
    extensions: ['.web.js', '.web.ts', '.web.tsx', '.js', '.ts', '.tsx'],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(appDirectory, 'web', 'index.html'),
    }),
    new webpack.DefinePlugin({
      __DEV__: process.env.NODE_ENV !== 'production',
    }),
  ],
  devServer: {
    historyApiFallback: true,
    static: {
      directory: path.resolve(appDirectory, 'dist'),
    },
    hot: true,
    port: 3000,
  },
};
