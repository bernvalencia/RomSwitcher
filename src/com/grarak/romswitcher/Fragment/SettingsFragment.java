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
import android.text.InputType;
import android.widget.EditText;

public class SettingsFragment extends PreferenceFragment implements
		Preference.OnPreferenceChangeListener {

	private static EditText mPasstext;
	private static final CharSequence KEY_OTA = "key_ota";
	private static final CharSequence KEY_PASS = "key_password";
	private static final CharSequence KEY_SETUP_PASS = "key_setup_pass";

	private static CheckBoxPreference mOTA, mPass;
	private static Preference mSetupPass;

	private static final String sdcard = "/sdcard";

	private static final String OTA_FILE = sdcard + "/romswitcher-tmp/ota";
	private static final String PASS_FILE = sdcard + "/romswitcher-tmp/pass";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_header);

		mSetupPass = (Preference) findPreference(KEY_SETUP_PASS);

		mOTA = (CheckBoxPreference) findPreference(KEY_OTA);
		mOTA.setOnPreferenceChangeListener(this);
		mPass = (CheckBoxPreference) findPreference(KEY_PASS);
		mPass.setOnPreferenceChangeListener(this);

		mOTA.setChecked(!Utils.existFile(OTA_FILE));

		mPass.setChecked(Utils.existFile(PASS_FILE));
		mSetupPass.setEnabled(Utils.existFile(PASS_FILE));
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mOTA) {
			mOTA.setChecked(!mOTA.isChecked());
			Utils.runCommand(!mOTA.isChecked() ? "echo \"disabled\" > "
					+ OTA_FILE : "rm -f " + OTA_FILE);
		} else if (preference == mPass) {
			if (mPass.isChecked())
				if (Utils.existFile(PASS_FILE))
					checkPassword(getActivity());
				else
					disablePass();
			else {
				mPass.setChecked(true);
				mSetupPass.setEnabled(true);
			}
		}
		return false;
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(KEY_SETUP_PASS)) {
			Utils.setupPassword(getActivity());
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void checkPassword(final Context context) {
		mPasstext = new EditText(context);
		mPasstext.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		Builder builder = new Builder(context);
		builder.setView(mPasstext)
				.setTitle(context.getString(R.string.password))
				.setNegativeButton(context.getString(R.string.button_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						})
				.setPositiveButton(context.getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								checkPass(context);
							}
						}).show();
	}

	private static void checkPass(Context context) {
		try {
			String password = Utils.readLine(PASS_FILE);
			if (mPasstext.getText().toString().trim().equals(password))
				disablePass();
			else {
				Utils.toast(context, context.getString(R.string.passwordwrong),
						0);
				checkPassword(context);
			}
		} catch (IOException e) {
		}
	}

	private static void disablePass() {
		mPass.setChecked(false);
		mSetupPass.setEnabled(false);
		Utils.runCommand("rm -f " + PASS_FILE);
	}
}
