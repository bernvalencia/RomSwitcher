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

	private static final CharSequence KEY_CATEGORY_FIFTHROM = "key_category_fifth";
	private static final CharSequence KEY_ENABLE_FIFTH = "key_enable_fifth";
	private static final CharSequence KEY_SETNAME_FIFTH = "key_setname_fifth";

	private static final CharSequence KEY_CATEGORY_MISC = "key_category_misc";
	private static final CharSequence KEY_REBOOT_RECOVERY = "key_reboot_recovery";
	private static final CharSequence KEY_INSTALL_RECOVERY = "key_install_recovery";
	private static final CharSequence KEY_MANUAL_BOOT = "key_manual_boot";
	private static final CharSequence KEY_APP_SHARING = "key_app_sharing";
	private static final CharSequence KEY_DATA_SHARING = "key_data_sharing";

	private static PreferenceCategory mCategoryMisc;

	private static CheckBoxPreference mSecond, mThird, mFourth, mFifth,
			mManualboot, mAppSharing, mDataSharing;

	private static Preference mFirstname, mSecondname, mThirdname, mFourthname,
			mFifthname;
	private static EditText mName;

	private static final String sdcard = "/sdcard";

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

	private static final String FIFTH_FILE = sdcard + "/romswitcher-tmp/fifth";
	private static final String FIFTH_NAME_FILE = sdcard
			+ "/romswitcher-tmp/fifthname";

	private static final String BOOT_IMAGE_FILE = sdcard
			+ "/romswitcher/second.img";
	private static final String MANUAL_BOOT_FILE = sdcard
			+ "/romswitcher-tmp/manualboot";
	private static final String APP_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/appshare";
	private static final String DATA_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/datashare";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_header);
		PreferenceScreen prefScreen = getPreferenceScreen();

		mFirstname = (Preference) findPreference(KEY_SETNAME_FIRST);
		mSecondname = (Preference) findPreference(KEY_SETNAME_SECOND);
		mThirdname = (Preference) findPreference(KEY_SETNAME_THIRD);
		mFourthname = (Preference) findPreference(KEY_SETNAME_FOURTH);
		mFifthname = (Preference) findPreference(KEY_SETNAME_FIFTH);

		try {
			setSummary(mFirstname, Utils.readLine(FIRST_NAME_FILE));
			setSummary(mSecondname, Utils.readLine(SECOND_NAME_FILE));
			setSummary(mThirdname, Utils.readLine(THIRD_NAME_FILE));
			setSummary(mFourthname, Utils.readLine(FOURTH_NAME_FILE));
			setSummary(mFifthname, Utils.readLine(FIFTH_NAME_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}

		mSecond = (CheckBoxPreference) findPreference(KEY_ENABLE_SECOND);
		mSecond.setOnPreferenceChangeListener(this);
		mThird = (CheckBoxPreference) findPreference(KEY_ENABLE_THIRD);
		mThird.setOnPreferenceChangeListener(this);
		mFourth = (CheckBoxPreference) findPreference(KEY_ENABLE_FOURTH);
		mFourth.setOnPreferenceChangeListener(this);
		mFifth = (CheckBoxPreference) findPreference(KEY_ENABLE_FIFTH);
		mFifth.setOnPreferenceChangeListener(this);

		mSecond.setChecked(!Utils.existFile(SECOND_FILE));
		mThird.setChecked(Utils.existFile(THIRD_FILE));
		mFourth.setChecked(Utils.existFile(FOURTH_FILE));
		mFifth.setChecked(Utils.existFile(FIFTH_FILE));

		mManualboot = (CheckBoxPreference) findPreference(KEY_MANUAL_BOOT);
		mManualboot.setOnPreferenceChangeListener(this);
		mAppSharing = (CheckBoxPreference) findPreference(KEY_APP_SHARING);
		mAppSharing.setOnPreferenceChangeListener(this);
		mDataSharing = (CheckBoxPreference) findPreference(KEY_DATA_SHARING);
		mDataSharing.setOnPreferenceChangeListener(this);

		mManualboot.setChecked(Utils.existFile(MANUAL_BOOT_FILE));

		mAppSharing.setChecked(Utils.existFile(APP_SHARING_FILE));
		mDataSharing.setEnabled(Utils.existFile(APP_SHARING_FILE));
		mDataSharing.setChecked(Utils.existFile(APP_SHARING_FILE));

		mDataSharing.setChecked(Utils.existFile(APP_SHARING_FILE)
				&& Utils.existFile(DATA_SHARING_FILE));
		if (Utils.existFile(APP_SHARING_FILE)
				&& Utils.existFile(DATA_SHARING_FILE))
			Utils.runCommand("rm -f " + DATA_SHARING_FILE);

		if (SupportedDevices.roms < 3)
			prefScreen.removePreference(findPreference(KEY_CATEGORY_THIRDROM));

		if (SupportedDevices.roms < 4)
			prefScreen.removePreference(findPreference(KEY_CATEGORY_FOURTHROM));

		if (SupportedDevices.roms < 5)
			prefScreen.removePreference(findPreference(KEY_CATEGORY_FIFTHROM));

		mCategoryMisc = (PreferenceCategory) findPreference(KEY_CATEGORY_MISC);

		if (SupportedDevices.recoverypartition.isEmpty())
			mCategoryMisc
					.removePreference(findPreference(KEY_INSTALL_RECOVERY));

		if (!SupportedDevices.onekernel)
			mCategoryMisc.removePreference(findPreference(KEY_REBOOT_RECOVERY));

		if (!SupportedDevices.manualboot)
			mCategoryMisc.removePreference(findPreference(KEY_MANUAL_BOOT));
	}

	public boolean onPreferenceChange(Preference preference, Object objValue) {
		if (preference == mSecond) {
			mSecond.setChecked(!mSecond.isChecked());
			Utils.runCommand(!mSecond.isChecked() ? "echo \"disabled\" > "
					+ SECOND_FILE : "rm -f " + SECOND_FILE);
		} else if (preference == mThird) {
			mThird.setChecked(!mThird.isChecked());
			Utils.runCommand(!mThird.isChecked() ? "rm -f " + THIRD_FILE
					: "echo \"enabled\" > " + THIRD_FILE);
		} else if (preference == mFourth) {
			mFourth.setChecked(!mFourth.isChecked());
			Utils.runCommand(!mFourth.isChecked() ? "rm -f " + FOURTH_FILE
					: "echo \"enabled\" > " + FOURTH_FILE);
		} else if (preference == mFifth) {
			mFifth.setChecked(!mFifth.isChecked());
			Utils.runCommand(!mFifth.isChecked() ? "rm -f " + FIFTH_FILE
					: "echo \"enabled\" > " + FIFTH_FILE);
		} else if (preference == mManualboot) {
			mManualboot.setChecked(!mManualboot.isChecked());
			Utils.runCommand(!mManualboot.isChecked() ? "rm -f "
					+ MANUAL_BOOT_FILE : "echo \"enabled\" > "
					+ MANUAL_BOOT_FILE);
		} else if (preference == mAppSharing) {
			if (mAppSharing.isChecked()) {
				mAppSharing.setChecked(false);
				mDataSharing.setEnabled(false);
				mDataSharing.setChecked(false);
				Utils.runCommand("rm -f " + APP_SHARING_FILE + " && rm -f "
						+ DATA_SHARING_FILE);
			} else {
				mAppSharing.setChecked(true);
				mDataSharing.setEnabled(true);
				Utils.runCommand("echo \"enabled\" > " + APP_SHARING_FILE);
			}
		} else if (preference == mDataSharing) {
			if (mDataSharing.isChecked()) {
				mDataSharing.setChecked(false);
				Utils.runCommand("rm -f " + DATA_SHARING_FILE);
			} else {
				mDataSharing.setChecked(true);
				Utils.runCommand("echo \"enabled\" > " + DATA_SHARING_FILE);
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
		} else if (preference.getKey().equals(KEY_SETNAME_FIFTH)) {
			alertEdit(getActivity(), 4);
		} else if (preference.getKey().equals(KEY_REBOOT_RECOVERY)) {
			File rom = new File("/.firstrom");
			String folder = rom.exists() ? "/.firstrom/media/rebootrs"
					: "/data/media/rebootrs";
			Utils.runCommand("echo 1 > " + folder + " && reboot");
		} else if (preference.getKey().equals(KEY_INSTALL_RECOVERY)) {
			Utils.runCommand("dd if=" + BOOT_IMAGE_FILE + " of="
					+ SupportedDevices.recoverypartition);
			Utils.toast(getActivity(), getString(R.string.recoveryinstalled), 0);
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void alertEdit(final Context context, final int name) {
		Builder alert = new Builder(context);
		mName = new EditText(context);
		try {
			switch (name) {
			case 0:
				mName.setHint(Utils.readLine(FIRST_NAME_FILE));
				break;
			case 1:
				mName.setHint(Utils.readLine(SECOND_NAME_FILE));
				break;
			case 2:
				mName.setHint(Utils.readLine(THIRD_NAME_FILE));
				break;
			case 3:
				mName.setHint(Utils.readLine(FOURTH_NAME_FILE));
				break;
			case 4:
				mName.setHint(Utils.readLine(FIFTH_NAME_FILE));

				break;
			}
		} catch (IOException e) {
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
					+ "\" > " + FIRST_NAME_FILE);
			break;
		case 1:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + SECOND_NAME_FILE);
			break;
		case 2:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + THIRD_NAME_FILE);
			break;
		case 3:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + FOURTH_NAME_FILE);
			break;
		case 4:
			Utils.runCommand("echo \"" + mName.getText().toString().trim()
					+ "\" > " + FIFTH_NAME_FILE);
			break;
		}
		try {
			setSummary(mFirstname, Utils.readLine(FIRST_NAME_FILE));
			setSummary(mSecondname, Utils.readLine(SECOND_NAME_FILE));
			setSummary(mThirdname, Utils.readLine(THIRD_NAME_FILE));
			setSummary(mFourthname, Utils.readLine(FOURTH_NAME_FILE));
			setSummary(mFifthname, Utils.readLine(FIFTH_NAME_FILE));
		} catch (IOException e) {
		}
	}
}
