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

        XposedHelpers.findAndHookMethod("com.android.nfc.cardemulation.HostEmulationManager",
                lpparam.classLoader, "notifyHostEmulationData", byte[].class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        byte[] data = (byte[]) param.args[0];
                        XposedBridge.log("com.android.nfc.cardemulation.HostEmulationManager " +
                                "onHostEmulationData Calling");
                        XposedBridge.log(bytesToHexString(data, 0, data.length));
                    }
                });

        XposedHelpers.findAndHookMethod("com.android.nfc.NfcService",
                lpparam.classLoader, "sendData", byte[].class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        byte[] data = (byte[]) param.args[0];
                        XposedBridge.log("com.android.nfc.NfcService sendData Calling");
                        XposedBridge.log(bytesToHexString(data, 0, data.length));
                    }
                });
    }


    static String bytesToHexString(byte[] bytes, int offset, int length) {
        final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] chars = new char[length * 2];
        int byteValue;
        for (int j = 0; j < length; j++) {
            byteValue = bytes[offset + j] & 0xFF;
            chars[j * 2] = hexChars[byteValue >>> 4];
            chars[j * 2 + 1] = hexChars[byteValue & 0x0F];
        }
        return new String(chars);
    }
}