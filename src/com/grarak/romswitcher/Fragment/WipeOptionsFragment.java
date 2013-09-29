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

import java.io.File;

import com.grarak.romswitcher.R;
import com.grarak.romswitcher.Utils.Utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class WipeOptionsFragment extends PreferenceFragment {

	private static final File secondrom = new File("/.firstrom/app");
	private static final CharSequence REMOVE_2NDROM = "key_remove_2ndrom";
	private static final CharSequence FACTORY_RESET = "key_factory_reset";
	private static final CharSequence WIPE_CACHE = "key_wipe_cache";
	private static final CharSequence WIPE_DALVIK = "key_wipe_dalvik";
	private static final CharSequence WIPE_BATTERY = "key_wipe_battery";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.wipe_options_header);

		if (secondrom.exists()) {
			findPreference(REMOVE_2NDROM).setEnabled(false);
			findPreference(FACTORY_RESET).setEnabled(false);
			findPreference(WIPE_CACHE).setEnabled(false);
			findPreference(WIPE_DALVIK).setEnabled(false);
			findPreference(WIPE_BATTERY).setEnabled(false);
		}
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(REMOVE_2NDROM)) {
			confirmDialog(getActivity(), getString(R.string.remove2ndrom), 0);
		} else if (preference.getKey().equals(FACTORY_RESET)) {
			confirmDialog(getActivity(), getString(R.string.factoryreset), 1);
		} else if (preference.getKey().equals(WIPE_CACHE)) {
			confirmDialog(getActivity(), getString(R.string.wipecache), 2);
		} else if (preference.getKey().equals(WIPE_DALVIK)) {
			confirmDialog(getActivity(), getString(R.string.wipedalvik), 3);
		} else if (preference.getKey().equals(WIPE_BATTERY)) {
			confirmDialog(getActivity(), getString(R.string.wipebattery), 4);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void confirmDialog(Context context, String title,
			final int mode) {
		Builder builder = new Builder(context);
		builder.setTitle(title)
				.setMessage(context.getString(R.string.areyousure))
				.setPositiveButton(context.getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (mode) {
								case 0:
									Utils.runCommand("rm -rf /cache/* && rm -rf /data/media/.secondrom");
									break;
								case 1:
									Utils.runCommand("rm -rf /data/media/.secondrom");
									break;
								case 2:
									Utils.runCommand("rm -rf /data/media/.secondrom/data/dalvik-cache && rm -rf /data/media/.secondrom/cache");
									break;
								case 3:
									Utils.runCommand("rm -rf /data/media/.secondrom/data/dalvik-cache");
									break;
								case 4:
									Utils.runCommand("rm -rf /data/media/.secondrom/data/system/batterystats.bin");
									break;
								}
							}
						})
				.setNegativeButton(context.getString(R.string.button_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
	}
}
