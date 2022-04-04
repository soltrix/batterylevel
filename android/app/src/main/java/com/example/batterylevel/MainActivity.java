package com.example.batterylevel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    // Теперь нам нужно создать MethodChannel с тем же именем, которое мы создали в приложении Flutter.
    private static final String CHANNEL = "channelname/network";

    // Создайте MethodChannel и установите MethodCallHandler внутри configureFlutterEngine() метода. Обязательно используйте то же имя канала, что и на стороне клиента Flutter.
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        // Наконец, завершите setMethodCallHandler() метод, добавленный ранее. Вам нужно обрабатывать один метод платформы getNetworkInfo(), поэтому проверьте его в call аргументе. Реализация этого метода платформы вызывает код Android, написанный на предыдущем шаге, и возвращает ответ как в случае успеха, так и в случае ошибки, используя result аргумент. Если вызывается неизвестный метод, сообщите об этом.
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            if (call.method.equals("getNetworkInfo")) {
                                String networkInfo = getNetworkInfo();

                                if (networkInfo != null) {
                                    result.success(networkInfo);
                                } else {
                                    result.error("UNAVAILABLE", "NetworkInfo not available.", null);
                                }
                            } else {
                                result.notImplemented();
                            }
                        }

                );
    }

    // Добавьте код Android Java, который использует API сети Android для получения информации о сети.
    private String getNetworkInfo() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return TODO;
        }

        //Calling the methods of TelephonyManager the returns the information
        String networkCountryISO=tm.getNetworkCountryIso();
        String SIMCountryISO=tm.getSimCountryIso();
        String softwareVersion=tm.getDeviceSoftwareVersion();
        String voiceMailNumber=tm.getVoiceMailNumber();

        //Get the phone type
        String strphoneType="";

        int phoneType=tm.getPhoneType();

        switch (phoneType)
        {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                strphoneType="CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                strphoneType="GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                strphoneType="NONE";
                break;
        }

        //getting information if phone is in roaming
        boolean isRoaming=tm.isNetworkRoaming();

        //Get the network type
        String strnetworkType="";

        int networkType = tm.getNetworkType();

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                strnetworkType = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                strnetworkType = "3G";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                strnetworkType = "4G";
                break;
            default:
                strnetworkType = "Unknown";
        }

        String info="Phone Network Details:\n";
        info+="\n Network Country ISO:"+networkCountryISO;
        info+="\n SIM Country ISO:"+SIMCountryISO;
        info+="\n Software Version:"+softwareVersion;
        info+="\n Voice Mail Number:"+voiceMailNumber;
        info+="\n Phone Network Type:"+strphoneType;
        info+="\n In Roaming? :"+isRoaming;
        info+="\n Phone Network Type:"+strnetworkType;

        return info;
    }
}
