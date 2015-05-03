//***************************************************************************************************
//***************************************************************************************************
//      Project name                    		: Alesco Happy
//      Class Name                              : BaseService
//      Author                                  : PurpleTalk, Inc.
//***************************************************************************************************
//      Class Description: BaseService.
//***************************************************************************************************
//***************************************************************************************************

package com.uconnect.events.network.services;

import android.content.Context;

import com.alesco.suggestionsapp.network.CloudAdapter;

/**
* BaseService.
* */
public class BaseService {

    final CloudAdapter mCloudAdapter;

    public BaseService(Context context) {
        mCloudAdapter = new CloudAdapter(context);
    }

    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfByte = (b >>> 4) & 0x0F;
            int twoHalf = 0;
            do {
                buf.append((0 <= halfByte) && (halfByte <= 9) ? (char) ('0' + halfByte) : (char) ('a' + (halfByte - 10)));
                halfByte = b & 0x0F;
            } while (twoHalf++ < 1);
        }
        return buf.toString();
    }

    public interface DataUpdateCallback {
        public void dataLoaded(byte status, String responseString, boolean loadStatus, String statusTitle);
    }
}
