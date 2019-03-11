# native_plugin

- get status bar height
- get battery level

## Getting Started
Add 
```
native_plugin:
      git:
        url: git://github.com/CLOUD-wuzichen/native_plugin.git
        verson: 0.0.1
```

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