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
import static com.stericson.RootTools.RootTools.debugMode;
import static com.stericson.RootTools.RootTools.isBusyboxAvailable;
import static com.stericson.RootTools.RootTools.isRootAvailable;

import java.io.File;

import com.grarak.romswitcher.Utils.ChooseRom;
import com.grarak.romswitcher.Utils.Unzip;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class CheckforFilesActivity extends Activity {

	private static String PREF_NAME_FIRST = "first_rom_name";
	private static String PREF_NAME_SECOND = "second_rom_name";
	private static String sdcard = getExternalStorageDirectory().getPath();
	private static final File secondtimg = new File(sdcard
			+ "/romswitcher/second.img");
	private static final File zip = new File(sdcard
			+ "/romswitcher/download.zip");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		debugMode = true;

		if (!isRootAvailable()) {
			Utils.toast(getApplicationContext(), getString(R.string.noroot), 0);
			finish();
		} else if (!isBusyboxAvailable()) {
			Utils.toast(getApplicationContext(), getString(R.string.nobusybox),
					0);
			finish();
		}

		if (secondtimg.exists()) {
			start(this);
		} else if (zip.exists()) {
			Unzip.unzip(sdcard + "/romswitcher/download.zip", sdcard
					+ "/romswitcher/");
			if (zip.exists()) {
				start(this);
			} else {
				Unzip.unzip(sdcard + "/romswitcher/download.zip", sdcard
						+ "/romswitcher/");
			}
		} else {
			Intent i = new Intent(this, LinkActivity.class);
			startActivity(i);
			finish();
		}
	}

	private static void start(Context context) {
		SharedPreferences FIRST_NAME = context.getSharedPreferences(
				PREF_NAME_FIRST, 0);
		SharedPreferences SECOND_NAME = context.getSharedPreferences(
				PREF_NAME_SECOND, 0);

		ChooseRom.chooserom(context, context.getString(R.string.app_name),
				FIRST_NAME.getString("firstname", "nothing"),
				SECOND_NAME.getString("secondname", "nothing"));
	}
}
