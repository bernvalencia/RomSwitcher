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

import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.os.Bundle;

public class CheckforFilesActivity extends Activity {
	
	private static String sdcard = getExternalStorageDirectory().getPath();
	private static final File secondtimg = new File(sdcard + "/romswitcher/second.img");
	private static final File zip = new File(sdcard + "/romswitcher/download.zip");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!isRootAvailable()) {
			Utils.toast(getApplicationContext(),
					getString(R.string.noroot), 0);
			finish();
		} else if (!isBusyboxAvailable()) {
			Utils.toast(getApplicationContext(),
					getString(R.string.nobusybox), 0);
			finish();
		}
		
		if (secondtimg.exists()) {
			
		} else if (zip.exists()) {
			
		} else {
			
		}
	}
}
