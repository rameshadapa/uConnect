/**
 * Created by jaggu on 4/18/2015.
 */
package com.uconnect.events.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * UCUtils is a utility class which provides several utility methods.
 */
public class UCUtils {

    /**
     * read the email if exists else read the phone no and return it as UCUserId.
     * If both does not exists return null.
     * @return UCUserId.
     */
    public static String getUCUserIdFromDevice(Context context) {
        String ucUserId = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                ucUserId = account.name == null || account.name.equals("") ? "" : account.name;
                break;
            }
        }

        return ucUserId;
    }

    public static String getUCUserPhoneNoFromDevice(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNo = telephonyManager.getLine1Number();
        phoneNo = phoneNo == null ? "" : phoneNo;

        return phoneNo;
    }

}
