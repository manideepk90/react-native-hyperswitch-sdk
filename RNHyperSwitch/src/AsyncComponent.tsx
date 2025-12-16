import React, { useEffect } from 'react';
import { View, Text, StyleSheet } from 'react-native';

const AsyncComponent = () => {
  const [data, setData] = React.useState(0);

  const calculate = async () => {
    const NativeLibDemo = await import(
      'react-native-lib-demo'
    );
    setData(NativeLibDemo.multiply(3, 4));
  };
  useEffect(() => {
    calculate();
  }, []);
  return (
    <View style={styles.container}>
      <Text style={styles.text}>The Module return this {data}</Text>
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
