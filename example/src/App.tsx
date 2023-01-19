import React from 'react';
import { StyleSheet, View, Button } from 'react-native';
import LocationHelper from 'react-native-android-location-helper';

export default function App() {
  const onPress = async () => {
    console.log(await LocationHelper.isMock());
    console.log(LocationHelper.isAllowedMockLocation());
  }

  return (
    <View style={styles.container}>
      <Button title="TEST" onPress={onPress} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
