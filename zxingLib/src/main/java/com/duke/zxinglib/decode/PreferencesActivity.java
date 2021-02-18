package com.duke.zxinglib.decode;

import android.app.Activity;
import android.os.Bundle;

/**
 * The main settings activity.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class PreferencesActivity extends Activity {

    public static final String KEY_DECODE_1D_PRODUCT = "preferences_decode_1D_product";
    public static final String KEY_DECODE_1D_INDUSTRIAL = "preferences_decode_1D_industrial";
    public static final String KEY_DECODE_QR = "preferences_decode_QR";
    public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
    public static final String KEY_DECODE_AZTEC = "preferences_decode_Aztec";
    public static final String KEY_DECODE_PDF417 = "preferences_decode_PDF417";

    public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";
    public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
    public static final String KEY_INVERT_SCAN = "preferences_invert_scan";

    public static final String KEY_DISABLE_CONTINUOUS_FOCUS = "preferences_disable_continuous_focus";
    public static final String KEY_DISABLE_EXPOSURE = "preferences_disable_exposure";
    public static final String KEY_DISABLE_METERING = "preferences_disable_metering";
    public static final String KEY_DISABLE_BARCODE_SCENE_MODE = "preferences_disable_barcode_scene_mode";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }

}
