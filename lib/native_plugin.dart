import 'dart:async';

import 'package:flutter/services.dart';

class NativePlugin {
  static const MethodChannel _channel = const MethodChannel('kwl_native');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> getBatteryLevel() async {
    String batteryLevel;
    try {
      final int result = await _channel.invokeMethod('getBatteryLevel');
      batteryLevel = '$result';
    } on PlatformException catch (e) {
      batteryLevel = "error exception: '${e.message}'.";
    }
    return batteryLevel;
  }

  static Future<String> getStatusBarHeight() async {
    String height;
    try {
      height = "${await _channel.invokeMethod('getStatusBarHeight')}";
    } on PlatformException catch (e) {
      height = "error exception: '${e.message}'.";
    }
    return height;
  }

}
