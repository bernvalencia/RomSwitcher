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
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.EditText;

public class GeneralFragment extends PreferenceFragment implements
		Preference.OnPreferenceChangeListener {

	private static final CharSequence KEY_SETNAME_FIRST = "key_setname_first";
	private static final CharSequence KEY_SETNAME_SECOND = "key_setname_second";
	private static final CharSequence KEY_APP_SHARING = "key_app_sharing";
	private static final CharSequence KEY_DATA_SHARING = "key_data_sharing";

	private static CheckBoxPreference mAppSharing, mDataSharing;

	private static Preference mFirstname, mSecondname;
	private static EditText mName;

	private static String sdcard = getExternalStorageDirectory().getPath();

	private static final String FIRST_NAME_FILE = sdcard
			+ "/romswitcher-tmp/firstname";
	private static final String SECOND_NAME_FILE = sdcard
			+ "/romswitcher-tmp/secondname";
	private static final String APP_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/appshare";
	private static final String DATA_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/datashare";

	private static final File mFirstfile = new File(FIRST_NAME_FILE);
	private static final File mSecondfile = new File(SECOND_NAME_FILE);
	private static final File mAppSharingfile = new File(APP_SHARING_FILE);
	private static final File mDataSharingfile = new File(DATA_SHARING_FILE);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_header);

		mFirstname = (Preference) findPreference(KEY_SETNAME_FIRST);
		mSecondname = (Preference) findPreference(KEY_SETNAME_SECOND);

		if (mFirstfile.exists() && mSecondfile.exists()) {
			try {
				setSummary(mFirstname, Utils.readLine(FIRST_NAME_FILE));
				setSummary(mSecondname, Utils.readLine(SECOND_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			setSummary(GeneralFragment.mFirstname,
					getString(R.string.firstrom));
			setSummary(GeneralFragment.mSecondname,
					getString(R.string.secondrom));
		}

		mAppSharing = (CheckBoxPreference) findPreference(KEY_APP_SHARING);
		mAppSharing.setOnPreferenceChangeListener(this);
		mDataSharing = (CheckBoxPreference) findPreference(KEY_DATA_SHARING);
		mDataSharing.setOnPreferenceChangeListener(this);

		if (mAppSharingfile.exists()) {
			mAppSharing.setChecked(true);
			mDataSharing.setEnabled(true);
		} else {
			mAppSharing.setChecked(false);
			mDataSharing.setChecked(false);
			mDataSharing.setEnabled(false);
		}

		if (mAppSharingfile.exists() && mDataSharingfile.exists()) {
			mDataSharing.setChecked(true);
		} else {
			mDataSharing.setChecked(false);
			Utils.runCommand("rm -f " + DATA_SHARING_FILE, 0);
		}
	}

	public boolean onPreferenceChange(Preference preference, Object objValue) {
		if (preference == mAppSharing) {
			if (mAppSharing.isChecked()) {
				mAppSharing.setChecked(false);
				mDataSharing.setEnabled(false);
				mDataSharing.setChecked(false);
				Utils.runCommand("rm -f " + APP_SHARING_FILE + " && rm -f "
						+ DATA_SHARING_FILE, 0);
			} else {
				mAppSharing.setChecked(true);
				mDataSharing.setEnabled(true);
				Utils.runCommand("echo \"enabled\" > " + APP_SHARING_FILE, 0);
			}
		} else if (mDataSharing.isChecked()) {
			mDataSharing.setChecked(false);
			Utils.runCommand("rm -f " + DATA_SHARING_FILE, 0);
		} else {
			mDataSharing.setChecked(true);
			Utils.runCommand("echo \"enabled\" > " + DATA_SHARING_FILE, 0);
		}
		return false;
	}

	private static void setSummary(Preference preference, String text) {
		preference.setSummary(text);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(KEY_SETNAME_FIRST)) {
			alertEdit(getActivity(), true);
		} else if (preference.getKey().equals(KEY_SETNAME_SECOND)) {
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
