// Copyright 2017 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

enum ImageSource {
  camera,

  gallery,
}

class ImagePicker {
  static const MethodChannel _channel = MethodChannel('kwl_native');

  static Future<File> pickImage({
    @required ImageSource source,
    double maxWidth,
    double maxHeight,
  }) async {
    assert(source != null);

    if (maxWidth != null && maxWidth < 0) {
      throw ArgumentError.value(maxWidth, 'maxWidth cannot be negative');
    }

    if (maxHeight != null && maxHeight < 0) {
      throw ArgumentError.value(maxHeight, 'maxHeight cannot be negative');
    }

    final String path = await _channel.invokeMethod(
      'pickImage',
      <String, dynamic>{
        'source': source.index,
        'maxWidth': maxWidth,
        'maxHeight': maxHeight,
      },
    );

    return path == null ? null : File(path);
  }

  static Future<File> pickVideo({
    @required ImageSource source,
  }) async {
    assert(source != null);
    final String path = await _channel.invokeMethod(
      'pickVideo',
      <String, dynamic>{
        'source': source.index,
      },
    );
    return path == null ? null : File(path);
  }
}
