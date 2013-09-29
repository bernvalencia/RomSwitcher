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
import java.io.IOException;

import com.grarak.romswitcher.R;
import com.grarak.romswitcher.Utils.Utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.EditText;

public class GeneralSettingsFragment extends PreferenceFragment {

	private static String sdcard = getExternalStorageDirectory().getPath();
	private static final CharSequence SETNAME_FIRST = "key_setname_first";
	private static final CharSequence SETNAME_SECOND = "key_setname_second";
	private static Preference mFirstname, mSecondname;
	private static EditText mName;
	private static final String FIRST_NAME_FILE = sdcard
			+ "/romswitcher-tmp/firstname";
	private static final String SECOND_NAME_FILE = sdcard
			+ "/romswitcher-tmp/secondname";
	private static final File mFirstfile = new File(FIRST_NAME_FILE);
	private static final File mSecondfile = new File(SECOND_NAME_FILE);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_settings_header);

		mFirstname = (Preference) findPreference(SETNAME_FIRST);
		mSecondname = (Preference) findPreference(SETNAME_SECOND);

		if (mFirstfile.exists() && mSecondfile.exists()) {
			try {
				setSummary(mFirstname, Utils.readLine(FIRST_NAME_FILE));
				setSummary(mSecondname, Utils.readLine(SECOND_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			setSummary(GeneralSettingsFragment.mFirstname,
					getString(R.string.firstrom));
			setSummary(GeneralSettingsFragment.mSecondname,
					getString(R.string.secondrom));
		}
	}

	private static void setSummary(Preference preference, String text) {
		preference.setSummary(text);
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
			if (mFirstfile.exists()) {
				try {
					mName.setHint(Utils.readLine(FIRST_NAME_FILE));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				mName.setHint(context.getString(R.string.firstrom));
			}
		} else {
			if (mSecondfile.exists()) {
				try {
					mName.setHint(Utils.readLine(SECOND_NAME_FILE));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				mName.setHint(context.getString(R.string.secondrom));
			}
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
		File rstmp = new File(sdcard + "/romswitcher-tmp");
		rstmp.mkdirs();

		if (name) {
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + sdcard + "/romswitcher-tmp/firstname", 0);
		} else {
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + sdcard + "/romswitcher-tmp/secondname", 1);
		}

		try {
			setSummary(mFirstname, Utils.readLine(FIRST_NAME_FILE));
			setSummary(mSecondname, Utils.readLine(SECOND_NAME_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
