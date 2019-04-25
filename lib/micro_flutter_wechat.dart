import 'dart:async';

import 'package:flutter/services.dart';

class MicroFlutterWechat {
  static const MethodChannel _channel =
      const MethodChannel('micro_flutter_wechat');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
