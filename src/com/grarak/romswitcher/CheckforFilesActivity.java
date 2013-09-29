/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher;

import static android.os.Environment.getExternalStorageDirectory;
import static com.stericson.RootTools.RootTools.isBusyboxAvailable;
import static com.stericson.RootTools.RootTools.isRootAvailable;

import java.io.File;

import com.grarak.romswitcher.Utils.ChooseRom;
import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class CheckforFilesActivity extends Activity {

	private static final File secondrom = new File("/.firstrom/app");
	private static String PREF = "prefs";
	private static String sdcard = getExternalStorageDirectory().getPath();
	private static final File firstimg = new File(sdcard
			+ "/romswitcher/first.img");
	private static final File secondimg = new File(sdcard
			+ "/romswitcher/second.img");
	private static final File zip = new File(sdcard
			+ "/romswitcher/download.zip");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!isRootAvailable()) {
			Utils.toast(getApplicationContext(), getString(R.string.noroot), 0);
			finish();
		} else if (!isBusyboxAvailable()) {
			Utils.toast(getApplicationContext(), getString(R.string.nobusybox),
					0);
			finish();
		}

		if (!firstimg.exists() && !secondrom.exists()) {

			Thread pause = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						GetKernel.pullkernel();
						Thread.sleep(1500);
						if (!firstimg.exists()) {
							Utils.toast(CheckforFilesActivity.this,
									getString(R.string.somethingwrong), 0);
							finish();
						}
					} catch (Exception e) {
						e.getLocalizedMessage();
					}
				}
			});
			pause.start();
		} else if (secondimg.exists() && firstimg.exists()) {
			start(this);
		} else if (zip.exists()) {
			unzip(this);
		} else {
			Intent i = new Intent(this, LinkActivity.class);
			startActivity(i);
			finish();
		}
	}

	private static void start(Context context) {
		SharedPreferences mPref = context.getSharedPreferences(PREF, 0);

		ChooseRom.chooserom(context, context.getString(R.string.app_name),
				mPref.getString("firstname", "nothing"),
				mPref.getString("secondname", "nothing"));
	}

	private static void unzip(Context context) {
		Utils.runCommand("unzip /sdcard/romswitcher/download.zip -d /sdcard/romswitcher/");
	}
}
