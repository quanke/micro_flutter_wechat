package com.wjapi.micro.micro_flutter_wechat;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * MicroFlutterWechatPlugin
 */
public class MicroFlutterWechatPlugin implements MethodCallHandler {

    private final Registrar registrar;

    private final MethodChannel channel;

    public MicroFlutterWechatPlugin(Registrar registrar, MethodChannel channel) {
        this.registrar = registrar;
        this.channel = channel;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "micro_flutter_wechat");
        channel.setMethodCallHandler(new MicroFlutterWechatPlugin(registrar, channel));
    }


    private static final String METHOD_PAY = "pay";
    private static final String METHOD_ON_PAY = "onPay";

    private static final String METHOD_INIT = "init";



    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (METHOD_PAY.equals(call.method)) {


            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (METHOD_ON_PAY.equals(call.method)) {

        } else if (METHOD_INIT.equals(call.method)) {

        } else {
            result.notImplemented();
        }
    }


    private void init(){

    }
}
