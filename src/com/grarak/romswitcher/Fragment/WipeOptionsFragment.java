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
	private static final CharSequence FACTORY_RESET_2ND = "key_factory_reset_2nd";
	private static final CharSequence WIPE_CACHE_2ND = "key_wipe_cache_2nd";
	private static final CharSequence WIPE_DALVIK_2ND = "key_wipe_dalvik_2nd";
	private static final CharSequence WIPE_BATTERY_2ND = "key_wipe_battery_2nd";

	private static final CharSequence REMOVE_3RDROM = "key_remove_3rdrom";
	private static final CharSequence FACTORY_RESET_3RD = "key_factory_reset_3rd";
	private static final CharSequence WIPE_CACHE_3RD = "key_wipe_cache_3rd";
	private static final CharSequence WIPE_DALVIK_3RD = "key_wipe_dalvik_3rd";
	private static final CharSequence WIPE_BATTERY_3RD = "key_wipe_battery_3rd";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.wipe_options_header);

		if (secondrom.exists()) {
			findPreference(REMOVE_2NDROM).setEnabled(false);
			findPreference(FACTORY_RESET_2ND).setEnabled(false);
			findPreference(WIPE_CACHE_2ND).setEnabled(false);
			findPreference(WIPE_DALVIK_2ND).setEnabled(false);
			findPreference(WIPE_BATTERY_2ND).setEnabled(false);

			findPreference(REMOVE_3RDROM).setEnabled(false);
			findPreference(FACTORY_RESET_3RD).setEnabled(false);
			findPreference(WIPE_CACHE_3RD).setEnabled(false);
			findPreference(WIPE_DALVIK_3RD).setEnabled(false);
			findPreference(WIPE_BATTERY_3RD).setEnabled(false);
		}
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(REMOVE_2NDROM)) {
			confirmDialog(getActivity(), getString(R.string.remove2ndrom),
					"secondrom", 0);
		} else if (preference.getKey().equals(FACTORY_RESET_2ND)) {
			confirmDialog(getActivity(), getString(R.string.factoryreset),
					"secondrom", 1);
		} else if (preference.getKey().equals(WIPE_CACHE_2ND)) {
			confirmDialog(getActivity(), getString(R.string.wipecache),
					"secondrom", 2);
		} else if (preference.getKey().equals(WIPE_DALVIK_2ND)) {
			confirmDialog(getActivity(), getString(R.string.wipedalvik),
					"secondrom", 3);
		} else if (preference.getKey().equals(WIPE_BATTERY_2ND)) {
			confirmDialog(getActivity(), getString(R.string.wipebattery),
					"secondrom", 4);

		} else if (preference.getKey().equals(REMOVE_3RDROM)) {
			confirmDialog(getActivity(), getString(R.string.remove2ndrom),
					"thirdrom", 0);
		} else if (preference.getKey().equals(FACTORY_RESET_3RD)) {
			confirmDialog(getActivity(), getString(R.string.factoryreset),
					"thirdrom", 1);
		} else if (preference.getKey().equals(WIPE_CACHE_3RD)) {
			confirmDialog(getActivity(), getString(R.string.wipecache),
					"thirdrom", 2);
		} else if (preference.getKey().equals(WIPE_DALVIK_3RD)) {
			confirmDialog(getActivity(), getString(R.string.wipedalvik),
					"thirdrom", 3);
		} else if (preference.getKey().equals(WIPE_BATTERY_3RD)) {
			confirmDialog(getActivity(), getString(R.string.wipebattery),
					"thirdrom", 4);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void confirmDialog(Context context, String title,
			final String rom, final int mode) {
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
									Utils.runCommand("rm -rf /data/media/."
											+ rom, 0);
									break;
								case 1:
									Utils.runCommand("rm -rf /data/media/."
											+ rom, 1);
									break;
								case 2:
									Utils.runCommand(
											"rm -rf /data/media/."
													+ rom
													+ "/data/dalvik-cache && rm -rf /data/media/."
													+ rom + "/cache", 2);
									break;
								case 3:
									Utils.runCommand("rm -rf /data/media/."
											+ rom + "/data/dalvik-cache", 3);
									break;
								case 4:
									Utils.runCommand("rm -rf /data/media/."
											+ rom
											+ "/data/system/batterystats.bin",
											4);
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
