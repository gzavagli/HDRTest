/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zavagli.hdrtest;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import java.util.Iterator;

/*
 * MainActivity class that loads {@link HdrTestMainFragment}.
 */
public class HdrTestMainActivity extends Activity
        implements HdrTestSimpleFragment.OnFragmentInteractionListener {

    private static final String TAG = "HDRTest";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdr_test_main);

        Log.d(TAG, "onCreate()");

        DisplayManager dm = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = dm.getDisplays();
        Display myDisplay = null;

        if (displays.length > 0) {
            Log.d(TAG, "Found " + displays.length + " display(s).");
            for (int i = 0; i < displays.length; i++) {
                Log.d(TAG, "Display #" + i + " ID: " + displays[i].getDisplayId());
            }
            if (displays.length == 1) {
                Log.d(TAG, "Found one display, using it.");
                myDisplay = displays[0];
            } else {
                Log.d(TAG, "Multiple displays found, using first one in list.");
                myDisplay = displays[0];
            }
        } else {
            Log.d(TAG, "No displays found");
        }

        if (myDisplay != null) {
            Display.HdrCapabilities hdrCaps = myDisplay.getHdrCapabilities();
            int[] hdrTypes = hdrCaps.getSupportedHdrTypes();
            if (hdrTypes.length > 0) {
                Log.d(TAG, "This display supports " + hdrTypes.length + " HDR types:");
                for (int i = 0; i < hdrTypes.length; i++) {
                    Log.d(TAG, "Supported HDR type #" + i + ": " + hdrTypes[i]);
                }
            } else {
                Log.d(TAG, "This display does not support any HDR types.");
            }
        }

        int numCodecs = MediaCodecList.getCodecCount();
        Log.d(TAG, "Found " + numCodecs + " media codecs.");

        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            Log.d(TAG, "Supported types for codec #" + i + ", " + codecInfo.getName());

            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                Log.d(TAG, " type " + j + ": " + types[j]);
            }

            if (i == 0) {
                MediaCodecInfo.CodecProfileLevel[] profileLevels = codecInfo.getCapabilitiesForType(MediaFormat.MIMETYPE_VIDEO_HEVC).profileLevels;
                // MediaCodecInfo.CodecProfileLevel[] profileLevels = codecInfo.getCapabilitiesForType(MediaFormat.MIMETYPE_VIDEO_DOLBY_VISION).profileLevels;

                if (profileLevels != null) {
                    Log.d(TAG, "  profile levels for MIMETYPE_VIDEO_HEVC: " + profileLevels.length);
                    // Log.d(TAG, "  profile levels for MIMETYPE_VIDEO_DOLBY_VISION: " + profileLevels.length);
                    if (profileLevels.length > 0) {
                        for (int k = 0; k < profileLevels.length; k++) {
                            Log.d(TAG, "  profile level #" + k + ": " + profileLevels[k].level + ", profile: " + profileLevels[k].profile);
                        }
                    }
                }
            }
        }



    }

    public void onFragmentInteraction(Uri uri) {
        Log.d(TAG, "onFragmentInteraction()");
    }

}
