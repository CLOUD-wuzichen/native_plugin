import 'dart:async';

import 'package:flutter/material.dart';
import 'package:native_plugin/native_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _height = "";
  String _batteryLevel = "";

  Future<void> _getBatteryLevel() async {
    String batteryLevel = await NativePlugin.getBatteryLevel();
    if (!mounted) return;
    setState(() {
      _batteryLevel = batteryLevel;
    });
  }

  Future<void> _getStatusBarHeight() async {
  String height = await NativePlugin.getStatusBarHeight();
    if (!mounted) return;
    setState(() {
      _height = height;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
        home: new Scaffold(
            appBar: AppBar(),
            body: new Center(
              child: new Column(
                children: [
                  Padding(padding: EdgeInsets.only(top: 100)),
                  RaisedButton(
                    child: new Text('status bar heigh: $_height'),
                    onPressed: _getStatusBarHeight,
                  ),
                  RaisedButton(
                    child: Text('battery level: $_batteryLevel'),
                    onPressed: _getBatteryLevel,
                  ),
                ],
              ),
            )));
  }
}
