import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:micro_flutter_wechat/micro_flutter_wechat.dart';

void main() {
  const MethodChannel channel = MethodChannel('micro_flutter_wechat');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await MicroFlutterWechat.platformVersion, '42');
  });
}
