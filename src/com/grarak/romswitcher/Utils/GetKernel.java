/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher.Utils;

import com.grarak.romswitcher.StartActivity;

import static android.os.Environment.getExternalStorageDirectory;

public class GetKernel {

	private static String sdcard = getExternalStorageDirectory().getPath();

	public static void pullkernel() {
		Utils.runCommand("dd if=" + StartActivity.bootpartition + " of="
				+ sdcard + "/romswitcher/first.img");
	}

}
