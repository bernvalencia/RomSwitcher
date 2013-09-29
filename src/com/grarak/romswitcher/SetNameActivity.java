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

import static android.os.Environment.getExternalStorageDirectory;

import java.io.File;

import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetNameActivity extends Activity {

	private static Button mButtonNext;
	private static EditText mFirstEdit, mSecondEdit;
	private static String sdcard = getExternalStorageDirectory().getPath();
	private static final String PREF = "prefs";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_name);

		mFirstEdit = (EditText) findViewById(R.id.firstrom_edit);
		mSecondEdit = (EditText) findViewById(R.id.secondrom_edit);

		mButtonNext = (Button) findViewById(R.id.button_next_setname);

		mButtonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFirstEdit.getText().toString().isEmpty()
						|| mSecondEdit.getText().toString().isEmpty()) {
					Utils.toast(SetNameActivity.this,
							getString(R.string.emptynames), 0);
				} else {
					SharedPreferences mPref = getSharedPreferences(PREF, 0);
					SharedPreferences.Editor editorPref = mPref.edit();
					editorPref.putBoolean("firstuse", false);
					editorPref.commit();

					File rstmp = new File(sdcard + "/romswitcher-tmp");
					rstmp.mkdirs();

					Utils.runCommand("echo \""
							+ mFirstEdit.getText().toString().trim() + "\" > "
							+ sdcard + "/romswitcher-tmp/firstname", 0);

					Utils.runCommand("echo \""
							+ mSecondEdit.getText().toString().trim() + "\" > "
							+ sdcard + "/romswitcher-tmp/secondname", 1);

					Intent i = new Intent(SetNameActivity.this,
							CheckforFilesActivity.class);
					startActivity(i);
					finish();
				}
			}
		});
	}
}
