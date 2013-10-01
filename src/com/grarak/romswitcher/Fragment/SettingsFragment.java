/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grarak.romswitcher.Fragment;

import static android.os.Environment.getExternalStorageDirectory;

import java.io.File;

import com.grarak.romswitcher.R;
import com.grarak.romswitcher.Utils.Utils;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements
		Preference.OnPreferenceChangeListener {

	private static final CharSequence KEY_OTA = "key_ota";

	private static CheckBoxPreference mOTA;

	private static final String sdcard = getExternalStorageDirectory()
			.getPath();

	private static final String OTA_FILE = sdcard + "/romswitcher-tmp/ota";

	private static final File mOTAfile = new File(OTA_FILE);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_header);

		mOTA = (CheckBoxPreference) findPreference(KEY_OTA);
		mOTA.setOnPreferenceChangeListener(this);

		if (mOTAfile.exists()) {
			mOTA.setChecked(false);
		} else {
			mOTA.setChecked(true);
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mOTA) {
			if (mOTA.isChecked()) {
				mOTA.setChecked(false);
				Utils.runCommand("echo \"disabled\" > " + mOTAfile, 0);
			} else {
				mOTA.setChecked(true);
				Utils.runCommand("rm -f " + mOTAfile, 0);
			}
		}
		return false;
	}
}
