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

import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.SupportedDevices;
import com.grarak.romswitcher.Utils.Utils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import static com.stericson.RootTools.RootTools.isBusyboxAvailable;
import static com.stericson.RootTools.RootTools.isAccessGiven;

public class MainSetupActivity extends Activity {

	private static final String sdcard = "/sdcard";
	private static final File firstimg = new File(sdcard
			+ "/romswitcher/first.img");
	private static Button mNextButton, mCancelButton;
	public static boolean firstuse = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_setup);
		firstuse = true;

		File RSDirectory = new File(sdcard + "/romswitcher/");
		RSDirectory.mkdirs();

		mNextButton = (Button) findViewById(R.id.button_next);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!isAccessGiven()) {
					Utils.toast(getApplicationContext(),
							getString(R.string.noroot), 0);
					finish();
				} else if (!isBusyboxAvailable()) {
					Utils.toast(getApplicationContext(),
							getString(R.string.nobusybox), 0);
					finish();
				} else {
					if (!SupportedDevices.onekernel) {
						Utils.displayprogress(getString(R.string.setuprs),
								MainSetupActivity.this);
						Thread pause = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									GetKernel.pullkernel();
									Thread.sleep(1000);
									if (!firstimg.exists()) {
										Utils.toast(
												MainSetupActivity.this,
												getString(R.string.somethingwrong),
												0);
										finish();
									}
									Utils.hideprogress();
								} catch (Exception e) {
									e.getLocalizedMessage();
								}
							}
						});
						pause.start();
					}
					Intent i = new Intent(MainSetupActivity.this,
							SetNameActivity.class);
					startActivity(i);
					finish();
				}
			}
		});

		mCancelButton = (Button) findViewById(R.id.button_cancel);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
