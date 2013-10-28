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

import java.io.File;

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

	private static final File mAppSharingfile = new File(APP_SHARING_FILE);
	private static final File mDataSharingfile = new File(DATA_SHARING_FILE);
	private static final File mPassfile = new File(PASS_FILE);

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

		if (mPassfile.exists()) {
			mPassword.setChecked(true);
			mButtonPassword.setVisibility(View.VISIBLE);
		} else {
			mPassword.setChecked(false);
			mButtonPassword.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		boolean forward = true;

		if (v == mButtonNext) {
			if (mAppSharing.isChecked()) {
				Utils.runCommand("echo \"enabled\" > " + APP_SHARING_FILE, 0);
			} else {
				Utils.runCommand("rm -f " + APP_SHARING_FILE + " && rm -f "
						+ DATA_SHARING_FILE, 0);
			}
			if (mDataSharing.isChecked()) {
				Utils.runCommand("echo \"enabled\" > " + DATA_SHARING_FILE, 0);
			} else {
				Utils.runCommand("rm -f " + DATA_SHARING_FILE, 0);
			}
			if (!mPassword.isChecked()) {
				Utils.runCommand("rm -f " + PASS_FILE, 0);
			} else if (!mPassfile.exists()) {
				Utils.toast(this, getString(R.string.nopassword), 0);
				forward = false;
			}
			if (forward) {
				SharedPreferences mPref = getSharedPreferences(PREF, 0);
				SharedPreferences.Editor editorPref = mPref.edit();
				editorPref.putBoolean("firstuse", false);
				editorPref.commit();

				Intent i = new Intent(this, CheckforFilesActivity.class);
				startActivity(i);
				finish();
			}
		} else if (v == mButtonPassword) {
			Utils.setupPassword(this);
		} else if (v == mAppSharing) {
			if (mAppSharing.isChecked()) {
				mAppSharing.setChecked(true);
				mDataSharing.setEnabled(true);
			} else {
				mAppSharing.setChecked(false);
				mDataSharing.setEnabled(false);
				mDataSharing.setChecked(false);
			}
		} else if (v == mPassword) {
			if (mPassword.isChecked()) {
				mPassword.setChecked(true);
				mButtonPassword.setVisibility(View.VISIBLE);
			} else {
				mPassword.setChecked(false);
				mButtonPassword.setVisibility(View.GONE);
			}
		}
	}
}
