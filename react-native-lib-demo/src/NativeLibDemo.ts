import { TurboModuleRegistry, type TurboModule } from 'react-native';

export interface Spec extends TurboModule {
  multiply(a: number, b: number): number;
}

var MyModule = TurboModuleRegistry.get<Spec>('LibDemo');

if (!MyModule) {
  console.log("Manideep", "TurboModule 'LibDemo' is not linked. Falling back to mock implementation.");
  MyModule = {
    multiply: (a: number, b: number) => {
      return a * b;
    },
  };
}

export default MyModule;
