#import "MicroFlutterWechatPlugin.h"
#import <micro_flutter_wechat/micro_flutter_wechat-Swift.h>

@implementation MicroFlutterWechatPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMicroFlutterWechatPlugin registerWithRegistrar:registrar];
}
@end
