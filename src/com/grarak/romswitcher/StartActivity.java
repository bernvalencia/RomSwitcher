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
import static com.stericson.RootTools.RootTools.debugMode;

import java.io.File;

import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class StartActivity extends Activity {

	private static final File secondrom = new File("/.firstrom/app");
	private static String sdcard = getExternalStorageDirectory().getPath();
	private static final File firstimg = new File(sdcard
			+ "/romswitcher/first.img");
	private static final String PREF = "prefs";
	private static String device = android.os.Build.DEVICE.toString();
	private static String board = android.os.Build.BOARD.toString();
	private static String brand = android.os.Build.BRAND.toString();
	public static String downloadlink = "";
	public static String bootpartition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		debugMode = true;

		// i9500
		if (device.equals("ja3g")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/i9500";
			bootpartition = "/dev/block/mmcblk0p9";
		}

		// M470
		if (device.equals("m470")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/m470";
			bootpartition = "/dev/block/platform/sdhci-tegra.3/by-name/LNX";
		}

		// m7ul
		if (device.contains("m7") && brand.contains("htc")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/m7ul";
			bootpartition = "/dev/block/mmcblk0p33";
		}

		// Manta
		if (device.equals("manta")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/manta";
			bootpartition = "/dev/block/platform/dw_mmc.0/by-name/boot";
		}

		// Mako
		if (device.equals("mako")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/mako";
			bootpartition = "/dev/block/platform/msm_sdcc.1/by-name/boot";
		}

		// Odin
		if (device.equals("odin") || device.equals("C6503")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/odin";
			bootpartition = "/dev/block/platform/msm_sdcc.1/by-name/boot";
		}

		// pollux_windy
		if (device.equals("pollux_windy") || device.contains("SGP31")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/pollux_windy";
			bootpartition = "/dev/block/platform/msm_sdcc.1/by-name/boot";
		}

		// Tuna
		if (board.equals("tuna")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/tuna";
			bootpartition = "/dev/block/platform/omap/omap_hsmmc.0/by-name/boot";
		}

		// Yuga
		if (device.equals("yuga") || device.equals("C6603")
				|| device.equals("C6602")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/yuga";
			bootpartition = "/dev/block/platform/msm_sdcc.1/by-name/boot";
		}

		if (downloadlink.isEmpty()) {
			Utils.alert(this, getString(R.string.app_name),
					getString(R.string.nosupport));
		} else {
			checkkernel(this);
		}
	}

	private static void checkkernel(Context context) {
		final SharedPreferences mPref = context.getSharedPreferences(PREF, 0);
		String mKernelVersion = mPref.getString("kernelversion", "nothing");
		SharedPreferences.Editor editor = mPref.edit();

		if (!secondrom.exists()) {
			if (mKernelVersion.equals("nothing")
					|| mKernelVersion.equals(Utils.getFormattedKernelVersion())) {
				editor.putString("kernelversion",
						Utils.getFormattedKernelVersion());
				editor.commit();
			} else {
				Utils.toast(context, context.getString(R.string.newkernel), 0);
				Utils.displayprogress(context.getString(R.string.setupnewkernel), context);
				loadKernel(context);
				editor.putString("kernelversion",
						Utils.getFormattedKernelVersion());
				editor.commit();
			}
		}

		setup(context, mPref);
	}

	private static void setup(Context context, SharedPreferences mPref) {
		boolean mFirstuse = mPref.getBoolean("firstuse", true);

		if (mFirstuse) {
			Intent i = new Intent(context, MainSetupActivity.class);
			context.startActivity(i);
			((Activity) context).finish();
		} else {
			Intent i = new Intent(context, CheckforFilesActivity.class);
			context.startActivity(i);
			((Activity) context).finish();
		}
	}

	private static void loadKernel(final Context context) {
		Thread pause = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					GetKernel.pullkernel();
					Thread.sleep(1000);
					if (!firstimg.exists()) {
						Utils.toast(context,
								context.getString(R.string.somethingwrong), 0);
						((Activity) context).finish();
					}
					Utils.hideprogress();
				} catch (Exception e) {
					e.getLocalizedMessage();
				}
			}
		});
		pause.start();
	}
}
