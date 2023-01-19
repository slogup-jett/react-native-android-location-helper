import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-android-location-helper' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const AndroidLocationHelper =
  NativeModules.AndroidLocationHelper
    ? NativeModules.AndroidLocationHelper
    : new Proxy({ }, { get() { throw new Error(LINKING_ERROR) } });

namespace LocationHelper {
  export const isMock = (): Promise<boolean> => {
    if (Platform.OS === 'ios') return new Promise(resolve => resolve(false));

    return AndroidLocationHelper.isMock();
  }

  export const isAllowedMockLocation = (): boolean => {
    if (Platform.OS === 'ios') return false;

    return AndroidLocationHelper.isAllowedMockLocation();
  }
}

export default LocationHelper;