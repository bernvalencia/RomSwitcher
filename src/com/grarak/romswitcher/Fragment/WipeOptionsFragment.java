/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher.Fragment;

import java.io.File;

import com.grarak.romswitcher.R;

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
			confirmDialog(getActivity(), getString(R.string.remove2ndrom));
		} else if (preference.getKey().equals(FACTORY_RESET)) {
			confirmDialog(getActivity(), getString(R.string.factoryreset));
		} else if (preference.getKey().equals(WIPE_CACHE)) {
			confirmDialog(getActivity(), getString(R.string.wipecache));
		} else if (preference.getKey().equals(WIPE_DALVIK)) {
			confirmDialog(getActivity(), getString(R.string.wipedalvik));
		} else if (preference.getKey().equals(WIPE_BATTERY)) {
			confirmDialog(getActivity(), getString(R.string.wipebattery));
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void confirmDialog(Context context, String title) {
		Builder builder = new Builder(context);
		builder.setTitle(title)
				.setMessage(context.getString(R.string.areyousure))
				.setPositiveButton(context.getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
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
