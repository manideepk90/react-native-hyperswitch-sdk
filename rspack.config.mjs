import path from 'node:path';
import { fileURLToPath } from 'node:url';
import * as Repack from '@callstack/repack';
import { MoveAssetsPlugin } from './plugins/MoveAssetsPlugin.mjs';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const optionalDependencies = [/react-native-lib-demo/];

export default Repack.defineRspackConfig(env => {
  const { platform } = env;

  const appAssets =
    platform === 'android'
      ? path.resolve(__dirname, 'android/app/src/main/assets')
      : path.resolve(__dirname, 'ios/RNHyperSwitch/resources');

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

    output: {
      filename: 'hyperswitch.bundle',
      path: path.resolve(__dirname, "build/generated/rspack"),
      chunkFilename: platform === 'android' ? '[name].android.chunk.bundle' : '[name].ios.chunk.bundle',
      clean: true,
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

    plugins: [
      new Repack.RepackPlugin({
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
          }
        ],
      }),
      new MoveAssetsPlugin({
        appAssetsPath: appAssets,
        patterns: optionalDependencies,
      }),
    ],
  };
});
