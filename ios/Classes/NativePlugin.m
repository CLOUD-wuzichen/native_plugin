#import "NativePlugin.h"

@implementation NativePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"kwl_native"
            binaryMessenger:[registrar messenger]];
  NativePlugin* instance = [[NativePlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getStatusBarHeight" isEqualToString:call.method]) {
    result( @(  [[UIApplication sharedApplication] statusBarFrame].size.height  ) );
  }if ([@"getBatteryLevel" isEqualToString:call.method]) {
       int batteryLevel = [self getBatteryLevel];
       if (batteryLevel == -1) {
       result([FlutterError errorWithCode:@"UNAVAILABLE"
                                         message:@"电池信息不可用"
                                         details:nil]);
       }else {
         result(@(batteryLevel));
       }
  }else {
    result(FlutterMethodNotImplemented);
  }
}

- (int)getBatteryLevel {
  UIDevice* device = UIDevice.currentDevice;
  device.batteryMonitoringEnabled = YES;
  if (device.batteryState == UIDeviceBatteryStateUnknown) {
    return -1;
  } else {
    return (int)(device.batteryLevel * 100);
  }
}



@end
