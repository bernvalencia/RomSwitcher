/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher;

import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class StartActivity extends Activity {

	private static final String PREF_FIRST_USE = "firstuse";
	private static final String KERNEL_VERSION = "kernelversion";
	private static String device = android.os.Build.DEVICE.toString();
	private static String board = android.os.Build.BOARD.toString();
	private static String brand = android.os.Build.BRAND.toString();
	public static String downloadlink = "";
	public static String bootpartition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		
		SharedPreferences PREF_KERNEL_VERSION = context.getSharedPreferences(KERNEL_VERSION, 0);
		String mKernelVersion = PREF_KERNEL_VERSION.getString("kernelversion", "nothing");
		
		if (mKernelVersion.equals("nothing")) {
			SharedPreferences.Editor editor = PREF_KERNEL_VERSION.edit();
			editor.putString("kernelversion", Utils.getFormattedKernelVersion());
			editor.commit();
			setup(context);
		} else if (!mKernelVersion.equals(Utils.getFormattedKernelVersion())) {
			Utils.toast(context, context.getString(R.string.newkernel), 0);
			Utils.displayprogress(context.getString(R.string.setupnewkernel), context);
			GetKernel.pullkernel();
		} else {
			setup(context);
		}
	}
	
	private static void setup(Context context) {
		SharedPreferences FIRST_USE = context.getSharedPreferences(PREF_FIRST_USE, 0);
		boolean mFirstuse = FIRST_USE.getBoolean("firstuse", true);
		if (mFirstuse == true) {
			Intent i = new Intent(context, MainSetupActivity.class);
			context.startActivity(i);
			((Activity) context).finish();
		} else {
			
		}
	}
}
