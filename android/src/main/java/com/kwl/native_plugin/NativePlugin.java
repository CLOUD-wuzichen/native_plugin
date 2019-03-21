package com.kwl.native_plugin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;

import com.kwl.native_plugin.image.ImagePickDelegate;

import java.io.File;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.Context.BATTERY_SERVICE;


/**
 * NativePlugin
 */
public class NativePlugin implements MethodCallHandler {

    private Activity activity;
    private ImagePickDelegate imagePickDelegate;

    public NativePlugin(Activity activity, ImagePickDelegate imagePickDelegate) {
        this.activity = activity;
        this.imagePickDelegate = imagePickDelegate;
    }

    /**
     * Plugin registration.
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void registerWith(Registrar registrar) {
        final File externalFilesDirectory = registrar.activity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        ImagePickDelegate imagePickDelegate = new ImagePickDelegate(registrar.activity(), externalFilesDirectory.getPath()+"/");
        registrar.addActivityResultListener(imagePickDelegate);
        registrar.addRequestPermissionsResultListener(imagePickDelegate);

        final MethodChannel channel = new MethodChannel(registrar.messenger(), "kwl_native");
        channel.setMethodCallHandler(new NativePlugin(registrar.activity(), imagePickDelegate));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "getBatteryLevel":
                getBatteryLevel(result);
                break;
            case "getStatusBarHeight":
                getStatusBarHeight(result);
                break;
            case "finishActivity":
                finishActivity(result);
                break;
            case "takePhoto":
                takePhoto(result);
                break;
            case "pickPhoto":
                pickPhoto(result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }
    private void takePhoto(Result result) {
        imagePickDelegate.takePhoto(result);
    }
    private void pickPhoto(Result result) {
        imagePickDelegate.pickPhoto(result);
    }

    private void finishActivity(Result result) {
        activity.finish();
        result.success("");
    }

    private void getBatteryLevel(MethodChannel.Result result) {
        int batteryLevel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) activity.getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(activity).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }
        if (batteryLevel != -1) {
            result.success(batteryLevel);
        } else {
            result.error("ERROR", "Battery level not available.", null);
        }
    }

    private void getStatusBarHeight(MethodChannel.Result result) {
        int height = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = activity.getResources().getDimensionPixelSize(resourceId);
        }
        result.success(height);
    }


}
