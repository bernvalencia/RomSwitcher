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

import static android.os.Environment.getExternalStorageDirectory;

import com.grarak.romswitcher.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class GetKernel {

	private static String sdcard = getExternalStorageDirectory().getPath();

	public static void pullkernel() {
		Utils.runCommand("dd if=" + SupportedDevices.bootpartition + " of="
				+ sdcard + "/romswitcher/first.img", 0);
	}

	public static void flashKernel(final Context context) {
		Utils.runCommand("dd if=" + sdcard + "/romswitcher/second.img of="
				+ SupportedDevices.bootpartition, 0);

		Builder builder = new Builder(context);
		builder.setTitle(context.getString(R.string.app_name))
				.setMessage(context.getString(R.string.kernelinstalled))
				.setNegativeButton(context.getString(R.string.button_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Activity) context).finish();
							}
						})
				.setPositiveButton(context.getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Utils.runCommand("reboot", 0);
							}
						}).show();
	}
}
