import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:native_plugin/native_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _text = "";
  File _image;

  _getBatteryLevel() {
    NativePlugin.getBatteryLevel().then((t) {
      if (!mounted) return;
      setState(() {
        _text = t;
      });
    });
    ////上面异步，下面阻塞
//    String batteryLevel = await NativePlugin.getBatteryLevel();
//    if (!mounted) return;
//    setState(() {
//      _text = batteryLevel;
//    });
  }

  Future _getStatusBarHeight() async {
    String height = await NativePlugin.getStatusBarHeight();
    if (!mounted) return;
    setState(() {
      _text = height;
    });
  }

  Future _takePhoto() async {
    String path = await NativePlugin.takePhoto();
    print("path=$path");
    setState(() {
      _text = path;
      _image = File(path);
    });
  }

  Future _pickPhoto() async{
    String path = await NativePlugin.pickPhoto();
    setState(() {
      _image = File(path);
    });
  }

  Future _showToast() async {
    await NativePlugin.showToast("这是个提示");
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
        home: Scaffold(
            appBar: AppBar(),
            body: Column(
              children: <Widget>[
                Padding(padding: EdgeInsets.only(top: 20)),
                Wrap(
                  children: [
                    RaisedButton(
                      child: new Text('status bar heigh'),
                      onPressed: _getStatusBarHeight,
                    ),
                    RaisedButton(
                      child: Text('battery level'),
                      onPressed: _getBatteryLevel,
                    ),
                    RaisedButton(
                      child: Text('照相'),
                      onPressed: _takePhoto,
                    ),
                    RaisedButton(
                      child: Text('图库'),
                      onPressed: _pickPhoto,
                    ),
                    RaisedButton(
                      child: Text('提示'),
                      onPressed: _showToast,
                    ),
                  ],
                ),
                Padding(padding: EdgeInsets.only(top: 20)),
                Text("$_text"),
                Padding(padding: EdgeInsets.only(top: 20)),
                _buildImage(),
              ],
            )));
  }

  Widget _buildImage() {
    if (_image == null) return Text("no photo");
    return Image.file(
      _image,
      width: 300,
      height: 300,
    );
  }
}
