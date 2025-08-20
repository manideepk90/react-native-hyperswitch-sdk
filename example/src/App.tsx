import React, { useEffect, useState } from 'react';
import { Text, View, StyleSheet, TouchableOpacity } from 'react-native';
import {
  HyperProvider,
  useHyper,
  type sessionParams,
} from 'react-native-hyperswitch-sdk';
import getSecrets from './utils/secrets';

export default function App() {
  const { initPaymentSession, presentPaymentSheet } = useHyper();
   const [paymentSheetParams, setPaymentSheetParams] = React.useState({});

  const [publishableKey, setPublishableKey] = useState<string>("");
  const [status, setStatus] = useState<string>("");
  const [ready, setReady] = useState<boolean>(false);

  const handleReloadClientSecret = async () => {
    const newClientSecret = await getSecrets();
    // if (newClientSecret.error) {
    //   console.error("Error fetching client secret:", newClientSecret.error);
    //   setStatus("Reload Client Secret : " + newClientSecret.error?.message);
    //   return;
    // }
    if (publishableKey !== newClientSecret.publishableKey) {
      setPublishableKey(newClientSecret.publishableKey || "edfghjmnbvcx");
    }
    setReady(true);
    try { 
      const params = await initPaymentSession({
        publishableKey: newClientSecret.publishableKey || "edfghjmnbvcx",
        clientSecret: newClientSecret.clientSecret || "sdfghjgfds",
      });
      if (params) {
        console.log("Payment session initialized with params:", params);
        setPaymentSheetParams(params);
        setStatus("Ready");
      }else{
        setStatus("Error initializing payment session");
      }
    } catch (error) {
      console.error("Error initializing payment session:", error);
      setStatus("Error initializing payment session");
      return;
    }


  };


  const handleLaunchPaymentSheet = async () => {
    if (!ready) {
      console.warn("SDK is not ready");
      setStatus("Reload Client Secret");
      return;
    }

    await presentPaymentSheet(
      {
        ...paymentSheetParams as sessionParams
      }
    )
    // Launch the payment sheet


  };

  useEffect(() => {
    handleReloadClientSecret();
  }, []);



  return (
    <HyperProvider publishableKey={publishableKey}>
      <View style={styles.container}>
        <Text style={styles.titleText}>Hyperswitch SDK</Text>
        <TouchableOpacity style={styles.button} onPress={handleReloadClientSecret}>
          <Text style={styles.buttonText}>Reload Client Secret</Text>
        </TouchableOpacity>
        <TouchableOpacity disabled={!ready}
          style={[styles.button, !ready && { opacity: 0.8 }]}
          onPress={handleLaunchPaymentSheet}>
          <Text style={styles.buttonText}>Launch Payment Sheet</Text>
        </TouchableOpacity>
        <Text style={styles.statusText}>{status}</Text>
      </View>
    </HyperProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    textAlign: "center",
    width: '100%',
    height: '100%',
    padding: 30,
    gap: 10
  },
  titleText: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#007bff',
    borderRadius: 5,
    padding: 16,
    width: '100%',
    justifyContent: 'center',
    display: 'flex'
  },
  buttonText: {
    color: '#fff',
    textAlign: 'center',
    fontSize: 16
  },
  statusText: {
    marginTop: 20,
    fontSize: 18,
    color: '#666',
    textAlign: 'center',
  },
});
