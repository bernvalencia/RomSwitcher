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
import com.grarak.romswitcher.Utils.SupportedDevices;
import com.grarak.romswitcher.Utils.Utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.widget.EditText;

public class GeneralFragment extends PreferenceFragment implements
		Preference.OnPreferenceChangeListener {

	private static final CharSequence KEY_SETNAME_FIRST = "key_setname_first";

	private static final CharSequence KEY_ENABLE_SECOND = "key_enable_second";
	private static final CharSequence KEY_SETNAME_SECOND = "key_setname_second";

	private static final CharSequence KEY_CATEGORY_THIRDROM = "key_category_third";
	private static final CharSequence KEY_ENABLE_THIRD = "key_enable_third";
	private static final CharSequence KEY_SETNAME_THIRD = "key_setname_third";

	private static final CharSequence KEY_CATEGORY_FOURTHROM = "key_category_fourth";
	private static final CharSequence KEY_ENABLE_FOURTH = "key_enable_fourth";
	private static final CharSequence KEY_SETNAME_FOURTH = "key_setname_fourth";

	private static final CharSequence KEY_CATEGORY_MISC = "key_category_misc";
	private static final CharSequence KEY_INSTALL_RECOVERY = "key_install_recovery";
	private static final CharSequence KEY_APP_SHARING = "key_app_sharing";
	private static final CharSequence KEY_DATA_SHARING = "key_data_sharing";

	private static PreferenceCategory mCategoryMisc;

	private static CheckBoxPreference mSecond, mThird, mFourth, mAppSharing,
			mDataSharing;

	private static Preference mFirstname, mSecondname, mThirdname, mFourthname;
	private static EditText mName;

	private static String sdcard = getExternalStorageDirectory().getPath();

	private static final String FIRST_NAME_FILE = sdcard
			+ "/romswitcher-tmp/firstname";

	private static final String SECOND_FILE = sdcard
			+ "/romswitcher-tmp/second";
	private static final String SECOND_NAME_FILE = sdcard
			+ "/romswitcher-tmp/secondname";

	private static final String THIRD_FILE = sdcard + "/romswitcher-tmp/third";
	private static final String THIRD_NAME_FILE = sdcard
			+ "/romswitcher-tmp/thirdname";

	private static final String FOURTH_FILE = sdcard
			+ "/romswitcher-tmp/fourth";
	private static final String FOURTH_NAME_FILE = sdcard
			+ "/romswitcher-tmp/fourthname";

	private static final String BOOT_IMAGE_FILE = sdcard
			+ "/romswitcher/second.img";
	private static final String APP_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/appshare";
	private static final String DATA_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/datashare";

	private static final File mSecondfile = new File(SECOND_FILE);
	private static final File mThirdfile = new File(THIRD_FILE);
	private static final File mFourthfile = new File(FOURTH_FILE);
	private static final File mAppSharingfile = new File(APP_SHARING_FILE);
	private static final File mDataSharingfile = new File(DATA_SHARING_FILE);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_header);
		PreferenceScreen prefScreen = getPreferenceScreen();

		mFirstname = (Preference) findPreference(KEY_SETNAME_FIRST);
		mSecondname = (Preference) findPreference(KEY_SETNAME_SECOND);
		mThirdname = (Preference) findPreference(KEY_SETNAME_THIRD);
		mFourthname = (Preference) findPreference(KEY_SETNAME_FOURTH);

		try {
			setSummary(mFirstname, Utils.readLine(FIRST_NAME_FILE));
			setSummary(mSecondname, Utils.readLine(SECOND_NAME_FILE));
			setSummary(mThirdname, Utils.readLine(THIRD_NAME_FILE));
			setSummary(mFourthname, Utils.readLine(FOURTH_NAME_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}

		mSecond = (CheckBoxPreference) findPreference(KEY_ENABLE_SECOND);
		mSecond.setOnPreferenceChangeListener(this);
		mThird = (CheckBoxPreference) findPreference(KEY_ENABLE_THIRD);
		mThird.setOnPreferenceChangeListener(this);
		mFourth = (CheckBoxPreference) findPreference(KEY_ENABLE_FOURTH);
		mFourth.setOnPreferenceChangeListener(this);

		if (mSecondfile.exists()) {
			mSecond.setChecked(false);
		} else {
			mSecond.setChecked(true);
		}

		if (mThirdfile.exists()) {
			mThird.setChecked(true);
		} else {
			mThird.setChecked(false);
		}

		if (mFourthfile.exists()) {
			mFourth.setChecked(true);
		} else {
			mFourth.setChecked(false);
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

		mCategoryMisc = (PreferenceCategory) findPreference(KEY_CATEGORY_MISC);

		if (SupportedDevices.recoverypartition.isEmpty()) {
			prefScreen.removePreference(findPreference(KEY_CATEGORY_THIRDROM));
			mCategoryMisc
					.removePreference(findPreference(KEY_INSTALL_RECOVERY));
		}

		if (!SupportedDevices.device_use_one_kernel) {
			prefScreen.removePreference(findPreference(KEY_CATEGORY_FOURTHROM));
		}
	}

	public boolean onPreferenceChange(Preference preference, Object objValue) {
		if (preference == mSecond) {
			if (mSecond.isChecked()) {
				mSecond.setChecked(false);
				Utils.runCommand("echo \"disabled\" > " + SECOND_FILE, 0);
			} else {
				mSecond.setChecked(true);
				Utils.runCommand("rm -f " + SECOND_FILE, 0);
			}
		} else if (preference == mThird) {
			if (mThird.isChecked()) {
				mThird.setChecked(false);
				Utils.runCommand("rm -f " + THIRD_FILE, 0);
			} else {
				mThird.setChecked(true);
				Utils.runCommand("echo \"enabled\" > " + THIRD_FILE, 0);
			}
		} else if (preference == mFourth) {
			if (mFourth.isChecked()) {
				mThird.setChecked(false);
				Utils.runCommand("rm -f " + FOURTH_FILE, 0);
			} else {
				mThird.setChecked(true);
				Utils.runCommand("echo \"enabled\" > " + FOURTH_FILE, 0);
			}
		} else if (preference == mAppSharing) {
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
		} else if (preference == mDataSharing) {
			if (mDataSharing.isChecked()) {
				mDataSharing.setChecked(false);
				Utils.runCommand("rm -f " + DATA_SHARING_FILE, 0);
			} else {
				mDataSharing.setChecked(true);
				Utils.runCommand("echo \"enabled\" > " + DATA_SHARING_FILE, 0);
			}
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
			alertEdit(getActivity(), 0);
		} else if (preference.getKey().equals(KEY_SETNAME_SECOND)) {
			alertEdit(getActivity(), 1);
		} else if (preference.getKey().equals(KEY_SETNAME_THIRD)) {
			alertEdit(getActivity(), 2);
		} else if (preference.getKey().equals(KEY_SETNAME_FOURTH)) {
			alertEdit(getActivity(), 3);
		} else if (preference.getKey().equals(KEY_INSTALL_RECOVERY)) {
			Utils.runCommand("dd if=" + BOOT_IMAGE_FILE + " of="
					+ SupportedDevices.recoverypartition, 3);
			Utils.toast(getActivity(), getString(R.string.recoveryinstalled), 0);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void alertEdit(final Context context, final int name) {
		Builder alert = new Builder(context);
		mName = new EditText(context);
		switch (name) {
		case 0:
			try {
				mName.setHint(Utils.readLine(FIRST_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			try {
				mName.setHint(Utils.readLine(SECOND_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				mName.setHint(Utils.readLine(THIRD_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				mName.setHint(Utils.readLine(FOURTH_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
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

	private static void setName(Context context, int name) {
		File rstmp = new File(sdcard + "/romswitcher-tmp");
		rstmp.mkdirs();

		switch (name) {
		case 0:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + FIRST_NAME_FILE, 0);
			break;
		case 1:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + SECOND_NAME_FILE, 1);
			break;
		case 2:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + THIRD_NAME_FILE, 2);
			break;
		case 3:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + FOURTH_NAME_FILE, 3);
			break;
		}

		try {
			setSummary(mFirstname, Utils.readLine(FIRST_NAME_FILE));
			setSummary(mSecondname, Utils.readLine(SECOND_NAME_FILE));
			setSummary(mThirdname, Utils.readLine(THIRD_NAME_FILE));
			setSummary(mFourthname, Utils.readLine(FOURTH_NAME_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
