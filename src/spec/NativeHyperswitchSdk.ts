import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  initPaymentSession(params: Object): Promise<Object>;
  presentPaymentSheet(sessionParams: Object): Promise<Object>;
  getCustomerSavedPaymentMethods(sessionParams: Object): Promise<Object>;
  getCustomerDefaultSavedPaymentMethodData(
    sessionParams: Object
  ): Promise<Object>;
  getCustomerLastUsedPaymentMethodData(sessionParams: Object): Promise<Object>;
  getCustomerSavedPaymentMethodData(
    sessionParams: Object
  ): Promise<Array<Object>>;
  confirmWithCustomerDefaultPaymentMethod(params: Object): Promise<Object>;
  confirmWithCustomerLastUsedPaymentMethod(params: Object): Promise<Object>;
  confirmWithCustomerPaymentToken(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>(
  'HyperSwitchSpec'
) as Spec;
