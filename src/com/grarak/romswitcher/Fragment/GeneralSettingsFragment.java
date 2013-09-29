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

import com.grarak.romswitcher.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.EditText;

public class GeneralSettingsFragment extends PreferenceFragment {

	private static final CharSequence SETNAME_FIRST = "key_setname_first";
	private static final CharSequence SETNAME_SECOND = "key_setname_second";
	private static final String PREF = "prefs";
	private static Preference mFirstname, mSecondname;
	private static SharedPreferences mPref;
	private static EditText mName;
	private static SharedPreferences.Editor editPref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_settings_header);

		mPref = getActivity().getSharedPreferences(PREF, 0);
		editPref = mPref.edit();

		mFirstname = (Preference) findPreference(SETNAME_FIRST);
		mSecondname = (Preference) findPreference(SETNAME_SECOND);

		mFirstname.setSummary(mPref.getString("firstname", "nothing"));
		mSecondname.setSummary(mPref.getString("secondname", "nothing"));
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(SETNAME_FIRST)) {
			alertEdit(getActivity(), true);
		} else if (preference.getKey().equals(SETNAME_SECOND)) {
			alertEdit(getActivity(), false);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void alertEdit(final Context context, final boolean name) {
		Builder alert = new Builder(context);
		mName = new EditText(context);
		if (name) {
			mName.setHint(mPref.getString("firstname", "nothing"));
		} else {
			mName.setHint(mPref.getString("secondname", "nothing"));
		}
		alert.setView(mName)
				.setTitle(context.getString(R.string.setname))
				.setPositiveButton(context.getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								setName(context, name);
							}
						})
				.setNegativeButton(context.getString(R.string.button_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});
		alert.show();
	}

	private static void setName(Context context, boolean name) {
		if (name) {
			editPref.putString("firstname", mName.getText().toString().trim());
		} else {
			editPref.putString("secondname", mName.getText().toString().trim());

		}
		editPref.commit();
		mFirstname.setSummary(mPref.getString("firstname", "nothing"));
		mSecondname.setSummary(mPref.getString("secondname", "nothing"));
	}
}
