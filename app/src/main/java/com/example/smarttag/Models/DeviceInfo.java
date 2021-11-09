package com.example.smarttag.Models;

import android.os.Build;

public class DeviceInfo {

    String cltDev_Imei;
    String cltDev_Brand;
    String cltDev_Cpu;
    String cltDev_Model;
    String cltDev_System;

    public DeviceInfo() {
        this.cltDev_Imei = buildPseudoImei();
        this.cltDev_Brand = Build.BRAND;
        this.cltDev_Cpu = Build.SUPPORTED_ABIS[0];
        this.cltDev_Model = Build.MODEL;
        this.cltDev_System = Build.MODEL;
    }


    private String buildPseudoImei(){
        return  "35" + Build.BOARD.length()%10 + Build.BRAND.length()%10 +
                Build.SUPPORTED_ABIS[0].length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10;
    }
}
