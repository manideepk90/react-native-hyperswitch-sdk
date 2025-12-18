import path from 'node:path';
import { fileURLToPath } from 'node:url';
import * as Repack from '@callstack/repack';
import { MoveAssetsPlugin } from './plugins/MoveAssetsPlugin.mjs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const optionalDependencies = [/react-native-lib-demo/];

export default Repack.defineRspackConfig(env => {
  const { platform, mode } = env;

  const appAssets =
    platform === 'android'
      ? path.resolve(__dirname, 'android/app/src/main/assets')
      : path.resolve(__dirname, 'ios/hyperswitch/resources');

  const appAssetsGenerated =
    platform === 'android'
      ? path.resolve(__dirname, 'build/hyperswitch/android')
      : path.resolve(__dirname, 'build/hyperswitch/ios');

  const libAssets =
    platform === 'android'
      ? path.resolve(__dirname, 'react-native-lib-demo/android/src/main/assets')
      : path.resolve(__dirname, 'react-native-lib-demo/ios/resources');

  return {
    context: __dirname,
    entry: './index.js',

    resolve: {
      ...Repack.getResolveOptions(),
    },
    module: {
      rules: [
        {
          test: /\.[cm]?[jt]sx?$/,
          type: 'javascript/auto',
          use: {
            loader: '@callstack/repack/babel-swc-loader',
            parallel: true,
            options: {},
          },
        },
        ...Repack.getAssetTransformRules(),
      ],
    },
    ...(mode === 'production' && {
      output: {
        filename: 'hyperswitch.bundle',
        path: appAssetsGenerated,
        chunkFilename: '[name].chunk.bundle',
        clean: true,
      },
    }),
    plugins: [
      new Repack.RepackPlugin(
        mode === 'production'
          ? {
              extraChunks: [
                {
                  test: optionalDependencies,
                  type: 'remote',
                  outputPath: libAssets,
                },
                {
                  exclude: optionalDependencies,
                  type: 'remote',
                  outputPath: appAssets,
                },
              ],
            }
          : {},
      ),
      ...(mode === 'production'
        ? [
            new MoveAssetsPlugin({
              appAssetsPath: appAssets,
              patterns: optionalDependencies,
            }),
          ]
        : []),
    ],
  };
});
