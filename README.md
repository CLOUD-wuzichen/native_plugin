# flutter_statusbar

native flutter plugin

## Getting Started
Add 

```bash

native_plugin : ^0.0.1

```
to your pubspec.yaml ,and run 

```bash
flutter packages get 
```

## How to use
```
import 'package:native_plugin/native_plugin.dart';
```
```
String _height = await NativePlugin.getStatusBarHeight();
String _batteryLevel = await NativePlugin.getBatteryLevel();
```