package com.wjapi.micro.micro_flutter_wechat;

import android.content.Intent;

//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.modelpay.PayReq;
//import com.tencent.mm.opensdk.modelpay.PayResp;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterNativeView;

/**
 * MicroFlutterWechatPlugin
 */
public class MicroFlutterWechatPlugin implements MethodCallHandler , PluginRegistry.ViewDestroyListener{

    private final Registrar registrar;

    private final MethodChannel channel;
    private final AtomicBoolean register = new AtomicBoolean(false);

    public MicroFlutterWechatPlugin(Registrar registrar, MethodChannel channel) {
        this.registrar = registrar;
        this.channel = channel;

        if (register.compareAndSet(false, true)) {
            WechatReceiver.registerReceiver(registrar.context(), wechatReceiver);
        }
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "micro_flutter_wechat");
        MicroFlutterWechatPlugin plugin = new MicroFlutterWechatPlugin(registrar, channel);
        registrar.addViewDestroyListener(plugin);
        channel.setMethodCallHandler(plugin);

    }


    private static final String METHOD_PAY = "pay";
    private static final String METHOD_ON_PAY = "onPay";

    private static final String METHOD_INIT = "init";


    private static final String ARGUMENT_KEY_APP_ID = "appId";

    private static final String ARGUMENT_KEY_PARTNER_ID = "partnerId";
    private static final String ARGUMENT_KEY_PREPAY_ID = "prepayId";

    private static final String ARGUMENT_KEY_NONCE_STR = "noncestr";
    private static final String ARGUMENT_KEY_TIMESTAMP = "timestamp";
    private static final String ARGUMENT_KEY_PACKAGE = "package";
    private static final String ARGUMENT_KEY_SIGN = "sign";

    private IWXAPI iwxapi;

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        //初始化微信支付sdk
        if (METHOD_PAY.equals(call.method)) {
            String appId = call.argument(ARGUMENT_KEY_APP_ID);
            iwxapi = WXAPIFactory.createWXAPI(registrar.context().getApplicationContext(), appId);
            iwxapi.registerApp(appId);
            result.success(null);

        } else if (METHOD_ON_PAY.equals(call.method)) {
            handlePayCall(call, result);

        } else if (METHOD_INIT.equals(call.method)) {

        } else {
            result.notImplemented();
        }
    }

    private void handlePayCall(MethodCall call, Result result) {
        PayReq req = new PayReq();
        req.appId = call.argument(ARGUMENT_KEY_APP_ID);
        req.partnerId = call.argument(ARGUMENT_KEY_PARTNER_ID);
        req.prepayId = call.argument(ARGUMENT_KEY_PREPAY_ID);
        req.nonceStr = call.argument(ARGUMENT_KEY_NONCE_STR);
        req.timeStamp = call.argument(ARGUMENT_KEY_TIMESTAMP);
        req.packageValue = call.argument(ARGUMENT_KEY_PACKAGE);
        req.sign = call.argument(ARGUMENT_KEY_SIGN);
        iwxapi.sendReq(req);
        result.success(null);
    }

    private WechatReceiver wechatReceiver = new WechatReceiver() {
        @Override
        public void handleIntent(Intent intent) {
            iwxapi.handleIntent(intent, iwxapiEventHandler);
        }
    };
    private static final String ARGUMENT_KEY_RESULT_RETURNKEY = "returnKey";

    private static final String ARGUMENT_KEY_RESULT_ERRORCODE = "errorCode";
    private static final String ARGUMENT_KEY_RESULT_ERRORMSG = "errorMsg";
    private IWXAPIEventHandler iwxapiEventHandler = new IWXAPIEventHandler() {
        @Override
        public void onReq(BaseReq req) {

        }

        @Override
        public void onResp(BaseResp resp) {
            Map<String, Object> map = new HashMap<>();
            map.put(ARGUMENT_KEY_RESULT_ERRORCODE, resp.errCode);
            map.put(ARGUMENT_KEY_RESULT_ERRORMSG, resp.errStr);
             if (resp instanceof PayResp) {
                // 支付
                PayResp payResp = (PayResp) resp;
                map.put(ARGUMENT_KEY_RESULT_RETURNKEY, payResp.returnKey);
                channel.invokeMethod(METHOD_ON_PAY, map);
            }
        }
    };


    @Override
    public boolean onViewDestroy(FlutterNativeView flutterNativeView) {
        if (register.compareAndSet(true, false)) {
            WechatReceiver.unregisterReceiver(registrar.context(), wechatReceiver);
        }
        return false;
    }
}
