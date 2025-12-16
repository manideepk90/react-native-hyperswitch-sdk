import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const AsyncComponent = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.text}>This is a lazy loaded component!</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
    backgroundColor: '#f0f0f0',
    borderRadius: 10,
    marginTop: 20,
    alignItems: 'center',
  },
  text: {
    fontSize: 18,
    color: '#333',
  },
});

export default AsyncComponent;
