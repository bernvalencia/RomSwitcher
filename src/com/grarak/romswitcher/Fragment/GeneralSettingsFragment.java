/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher.Fragment;

import com.grarak.romswitcher.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class GeneralSettingsFragment extends PreferenceFragment {

	private static final CharSequence SETNAME_FIRST = "key_setname_first";
	private static final CharSequence SETNAME_SECOND = "key_setname_second";
	private static final String PREF = "prefs";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.more_settings_header);

		SharedPreferences mPref = getActivity().getSharedPreferences(PREF, 0);
		
		findPreference(SETNAME_FIRST).setSummary(mPref.getString("firstname", "nothing"));
		findPreference(SETNAME_SECOND).setSummary(mPref.getString("secondname", "nothing"));
	}
}
