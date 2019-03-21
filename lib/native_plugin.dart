import 'dart:async';
import 'dart:io';

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

  static finishActivity() {
    if(Platform.isAndroid){  //暂时只有Android
      _channel.invokeMethod('finishActivity');
    }else if(Platform.isIOS){
    }
  }

  static Future<String> pickPhoto() async {
    String path;
    if(Platform.isAndroid){  //暂时只有Android
      try {
        path = "${await _channel.invokeMethod('pickPhoto')}";
      } on PlatformException catch (e) {
        path = "error: ${e.code}--${e.message}--${e.details}";
      }
    }else if(Platform.isIOS){
    }
    return path;
  }

  static Future<String> takePhoto() async {
    String path;
    if(Platform.isAndroid){  //暂时只有Android
      try {
        path = "${await _channel.invokeMethod('takePhoto')}";
      } on PlatformException catch (e) {
        path = "error: ${e.code}--${e.message}--${e.details}";
      }
    }else if(Platform.isIOS){
    }
    return path;
  }

}
