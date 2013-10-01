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
import static com.stericson.RootTools.RootTools.isBusyboxAvailable;
import static com.stericson.RootTools.RootTools.isRootAvailable;

import java.io.File;
import java.io.IOException;

import com.grarak.romswitcher.Utils.ChooseRom;
import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class CheckforFilesActivity extends Activity {

	private static final File secondrom = new File("/.firstrom/app");

	private static String sdcard = getExternalStorageDirectory().getPath();

	private static final File firstimg = new File(sdcard
			+ "/romswitcher/first.img");
	private static final File secondimg = new File(sdcard
			+ "/romswitcher/second.img");
	private static final File zip = new File(sdcard
			+ "/romswitcher/download.zip");
	private static final String FIRST_NAME_FILE = sdcard
			+ "/romswitcher-tmp/firstname";
	private static final String SECOND_NAME_FILE = sdcard
			+ "/romswitcher-tmp/secondname";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		File rs = new File(sdcard + "/romswitcher");
		rs.mkdirs();

		File rstmp = new File(sdcard + "/romswitcher-tmp");
		rstmp.mkdirs();

		if (!isRootAvailable()) {
			Utils.toast(getApplicationContext(), getString(R.string.noroot), 0);
			finish();
		} else if (!isBusyboxAvailable()) {
			Utils.toast(getApplicationContext(), getString(R.string.nobusybox),
					0);
			finish();
		}

		if (!firstimg.exists()) {
			if (secondrom.exists()) {
				Utils.alert(this, getString(R.string.app_name),
						getString(R.string.nofirstimg));
			} else {
				Utils.displayprogress(getString(R.string.getfirstimg), this);
				Thread pause = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							GetKernel.pullkernel();
							Thread.sleep(1000);
							if (!firstimg.exists()) {
								Utils.toast(CheckforFilesActivity.this,
										getString(R.string.somethingwrong), 0);
								finish();
							} else {
								restartIntent(CheckforFilesActivity.this);
							}
							Utils.hideprogress();
						} catch (Exception e) {
							e.getLocalizedMessage();
						}
					}
				});
				pause.start();
			}
		} else if (!secondimg.exists() && zip.exists()) {
			unzip(this);
		} else if (!secondimg.exists() && !zip.exists()) {
			Intent i = new Intent(this, LinkActivity.class);
			startActivity(i);
			finish();
		} else if (firstimg.exists() && secondimg.exists()) {
			start(this);
		}
	}

	private static void start(Context context) {
		File mFirstname = new File(FIRST_NAME_FILE);
		File mSecondname = new File(SECOND_NAME_FILE);
		if (mFirstname.exists() && mSecondname.exists()) {
			try {
				ChooseRom.chooserom(context,
						context.getString(R.string.app_name),
						Utils.readLine(FIRST_NAME_FILE),
						Utils.readLine(SECOND_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ChooseRom.chooserom(context, context.getString(R.string.app_name),
					context.getString(R.string.firstrom),
					context.getString(R.string.secondrom));
		}
	}

	private static void restartIntent(Context context) {
		Intent i = new Intent(context, StartActivity.class);
		context.startActivity(i);
		((Activity) context).finish();
	}

	private static void unzip(Context context) {
		Utils.runCommand(
				"unzip /sdcard/romswitcher/download.zip -d /sdcard/romswitcher/",
				0);
		restartIntent(context);
	}
}
