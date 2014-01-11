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

package com.grarak.romswitcher.Utils;

public class SupportedDevices {

	private static String device = android.os.Build.DEVICE.toString();
	private static String board = android.os.Build.BOARD.toString();
	public static String downloadlink = "";
	public static String bootpartition;
	public static String recoverypartition = "";
	public static int roms = 2;
	public static boolean onekernel = false;
	public static boolean manualboot = false;

	public static void getDevices() {

		// Samsung Galaxy Mega 6.3
		if (device.startsWith("melius") || device.startsWith("i920")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/melius";
			bootpartition = "dev/block/platform/msm_sdcc.1/by-name/boot";
			recoverypartition = "/dev/block/platform/msm_sdcc.1/by-name/recovery";
			roms = 5;
			onekernel = true;
		}

		// Samsung Galaxy Nexus
		if (board.equals("tuna")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/tuna";
			bootpartition = "/dev/block/platform/omap/omap_hsmmc.0/by-name/boot";
			roms = 5;
			onekernel = true;
			manualboot = true;
		}

		// Samsung Galaxy S4 Exynos
		if (device.equals("ja3g") || device.equals("i9500")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/i9500";
			bootpartition = "/dev/block/mmcblk0p9";
			recoverypartition = "/dev/block/mmcblk0p10";
			roms = 4;
			onekernel = true;
			manualboot = true;
		}

		// HTC One
		if (device.contains("m7")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/m7ul";
			bootpartition = "/dev/block/mmcblk0p33";
			recoverypartition = "/dev/block/mmcblk0p34";
			roms = 5;
		}

		// Google Nexus 10
		if (device.equals("manta")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/manta";
			bootpartition = "/dev/block/platform/dw_mmc.0/by-name/boot";
		}

		// Sony Xperia Tablet Z
		if (device.equals("pollux_windy") || device.contains("SGP31")) {
			downloadlink = "https://raw.github.com/RomSwitchers/Downloadlinks/master/pollux_windy";
			bootpartition = "/dev/block/platform/msm_sdcc.1/by-name/boot";
		}

	}

}
