/**
 * @format
 */

import {AppRegistry, Platform} from 'react-native';
import {ScriptManager, Script} from '@callstack/repack/client';
import App from './src/routes/Update';
import {name as appName} from './app.json';

// ScriptManager.shared.addResolver(async (scriptId, caller) => {
//   if (__DEV__) {
//     return {
//       url: Script.getDevServerURL(scriptId),
//       cache: false,
//     };
//   }
//   if (Platform.OS === 'android') {
//     return {
//       url: Script.getFileSystemURL(`assets://${scriptId}`),
//     };
//   } else if (Platform.OS === 'ios') {
//     return {
//       url: Script.getFileSystemURL(`${scriptId}`),
//     };
//   }
//   return {
//     url: Script.getRemoteURL(`https://URL.com/assets/v1/chunks/${scriptId}`),
//   };
// });

AppRegistry.registerComponent(appName, () => App);
