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

package com.grarak.romswitcher;

import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class FeaturesActivity extends Activity implements View.OnClickListener {

	private static Button mButtonPassword, mButtonNext;
	private static CheckBox mAppSharing, mDataSharing, mPassword;
	private static final String sdcard = "/sdcard";
	private static final String PREF = "prefs";

	private static final String APP_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/appshare";
	private static final String DATA_SHARING_FILE = sdcard
			+ "/romswitcher-tmp/datashare";
	private static final String PASS_FILE = sdcard + "/romswitcher-tmp/pass";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.features);

		mButtonPassword = (Button) findViewById(R.id.setup_password);
		mButtonPassword.setOnClickListener(this);
		mButtonNext = (Button) findViewById(R.id.button_next_features);
		mButtonNext.setOnClickListener(this);

		mAppSharing = (CheckBox) findViewById(R.id.appsharing);
		mAppSharing.setOnClickListener(this);
		mDataSharing = (CheckBox) findViewById(R.id.datasharing);
		mPassword = (CheckBox) findViewById(R.id.check_password);
		mPassword.setOnClickListener(this);

		mAppSharing.setChecked(Utils.existFile(APP_SHARING_FILE));
		mDataSharing.setEnabled(Utils.existFile(APP_SHARING_FILE));
		mDataSharing.setChecked(Utils.existFile(APP_SHARING_FILE));

		mDataSharing.setChecked(Utils.existFile(APP_SHARING_FILE)
				&& Utils.existFile(DATA_SHARING_FILE));

		if (!(Utils.existFile(APP_SHARING_FILE) && Utils
				.existFile(DATA_SHARING_FILE)))
			Utils.runCommand("rm -f " + DATA_SHARING_FILE);

		mPassword.setChecked(Utils.existFile(PASS_FILE));
		mButtonPassword.setVisibility(Utils.existFile(PASS_FILE) ? View.VISIBLE
				: View.GONE);
	}

	@Override
	public void onClick(View v) {
		boolean forward = true;
		if (v == mButtonNext) {
			Utils.runCommand(mAppSharing.isChecked() ? "echo \"enabled\" > "
					+ APP_SHARING_FILE : "rm -f " + APP_SHARING_FILE
					+ " && rm -f " + DATA_SHARING_FILE);

			Utils.runCommand(mDataSharing.isChecked() ? "echo \"enabled\" > "
					+ DATA_SHARING_FILE : "rm -f " + DATA_SHARING_FILE);

			if (!mPassword.isChecked())
				Utils.runCommand("rm -f " + PASS_FILE);
			else if (!Utils.existFile(PASS_FILE)) {
				Utils.toast(this, getString(R.string.nopassword), 0);
				forward = false;
			}

			if (forward) {
				SharedPreferences mPref = getSharedPreferences(PREF, 0);
				SharedPreferences.Editor editorPref = mPref.edit();
				editorPref.putBoolean("firstuse", false);
				editorPref.commit();

				startActivity(new Intent(this, CheckforFilesActivity.class));
				finish();
			}
		} else if (v == mButtonPassword) {
			Utils.setupPassword(this);
		} else if (v == mAppSharing) {
			mAppSharing.setChecked(mAppSharing.isChecked());
			mDataSharing.setEnabled(mAppSharing.isChecked());
			mDataSharing.setChecked(mAppSharing.isChecked());
		} else if (v == mPassword) {
			mPassword.setChecked(mPassword.isChecked());
			mButtonPassword.setVisibility(mPassword.isChecked() ? View.VISIBLE
					: View.GONE);
		}
	}
}
