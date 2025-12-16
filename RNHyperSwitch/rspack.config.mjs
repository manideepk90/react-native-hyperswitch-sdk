import path from 'node:path';
import { fileURLToPath } from 'node:url';
import * as Repack from '@callstack/repack';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

/**
 * Rspack configuration enhanced with Re.Pack defaults for React Native.
 *
 * Learn about Rspack configuration: https://rspack.dev/config/
 * Learn about Re.Pack configuration: https://re-pack.dev/docs/guides/configuration
 */

export default Repack.defineRspackConfig((env) => {
  const { platform, mode } = env;

  const defaultDest =
    platform === 'android'
      ? path.resolve(__dirname, 'android/app/src/main/assets')
      : path.resolve(__dirname, 'ios/RNHyperSwitch/resources');

  return {
    context: __dirname,
    entry: './index.js',
    resolve: {
      ...Repack.getResolveOptions(),
    },
    output: {
      filename: 'hyperswitch.bundle',
      chunkFilename: '[name].chunk.bundle',
      path: defaultDest,
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
            test: /react-native-lib-demo/,
            type: 'remote',
            outputPath:
              platform === 'android'
                ? path.resolve(
                    __dirname,
                    'react-native-lib-demo/android/src/main/assets'
                  )
                : path.resolve(
                    __dirname,
                    'react-native-lib-demo/ios/Resources'
                ),
          },
          {
            exclude: /react-native-lib-demo/,
            type: 'remote',
            outputPath: defaultDest,
          },
        ],
      }),
    ],
  };
});
