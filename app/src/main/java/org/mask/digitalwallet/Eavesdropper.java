package org.mask.digitalwallet;

/**
 * Created by eagleone on 11/15/14.
 */

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Eavesdropper implements IXposedHookLoadPackage {

    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.nfc"))
            return;

        XposedBridge.log("NFC HCE has been enabled!");

        XposedHelpers.findAndHookMethod("com.android.nfc.cardemulation.HostEmulationManager", lpparam.classLoader, "onHostEmulationData", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("onHostEmulationData Calling");
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("onHostEmulationData Called");
            }
        });
    }
}

