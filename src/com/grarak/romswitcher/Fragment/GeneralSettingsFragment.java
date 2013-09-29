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

		SharedPreferences FIRST_NAME = getActivity().getSharedPreferences(PREF, 0);
		SharedPreferences SECOND_NAME = getActivity().getSharedPreferences(PREF, 0);
	}
}
