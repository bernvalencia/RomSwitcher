/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.grarak.romswitcher.StartActivity;
import com.stericson.RootTools.CommandCapture;

import static android.os.Environment.getExternalStorageDirectory;
import static com.stericson.RootTools.RootTools.getShell;

public class GetKernel {

	private static String sdcard = getExternalStorageDirectory().getPath();

	public static void pullkernel() {
		try {
			getShell(true).add(
					new CommandCapture(0, "dd if="
							+ StartActivity.bootpartition + " of=" + sdcard
							+ "/romswitcher/first.img")).waitForFinish();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
