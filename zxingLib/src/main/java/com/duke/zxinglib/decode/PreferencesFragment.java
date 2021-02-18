package com.duke.zxinglib.decode;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.duke.zxinglib.R;

import java.util.ArrayList;
import java.util.Collection;

public final class PreferencesFragment
        extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CheckBoxPreference[] checkBoxPrefs;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.preferences);

        PreferenceScreen preferences = getPreferenceScreen();
        preferences.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        checkBoxPrefs = findDecodePrefs(preferences,
                PreferencesActivity.KEY_DECODE_1D_PRODUCT,
                PreferencesActivity.KEY_DECODE_1D_INDUSTRIAL,
                PreferencesActivity.KEY_DECODE_QR,
                PreferencesActivity.KEY_DECODE_DATA_MATRIX,
                PreferencesActivity.KEY_DECODE_AZTEC,
                PreferencesActivity.KEY_DECODE_PDF417);
        disableLastCheckedPref();

    }

    private static CheckBoxPreference[] findDecodePrefs(PreferenceScreen preferences, String... keys) {
        CheckBoxPreference[] prefs = new CheckBoxPreference[keys.length];
        for (int i = 0; i < keys.length; i++) {
            prefs[i] = (CheckBoxPreference) preferences.findPreference(keys[i]);
        }
        return prefs;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        disableLastCheckedPref();
    }

    private void disableLastCheckedPref() {
        Collection<CheckBoxPreference> checked = new ArrayList<>(checkBoxPrefs.length);
        for (CheckBoxPreference pref : checkBoxPrefs) {
            if (pref.isChecked()) {
                checked.add(pref);
            }
        }
        boolean disable = checked.size() <= 1;
        for (CheckBoxPreference pref : checkBoxPrefs) {
            pref.setEnabled(!(disable && checked.contains(pref)));
        }
    }

}
