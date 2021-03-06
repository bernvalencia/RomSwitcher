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

import com.grarak.romswitcher.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class GetKernel {

	private static final String sdcard = "/sdcard";
	private static final String SECOND_IMG = sdcard + "/romswitcher/second.img";

	public static void pullkernel() {
		Utils.runCommand("dd if=" + SupportedDevices.bootpartition + " of="
				+ sdcard + "/romswitcher/first.img");
	}

	public static void flashKernel(final Context context) {
		if (Utils.existFile(SECOND_IMG)) {
			Utils.runCommand("dd if=" + sdcard + "/romswitcher/second.img of="
					+ SupportedDevices.bootpartition);

			Builder builder = new Builder(context);
			builder.setTitle(context.getString(R.string.app_name))
					.setMessage(context.getString(R.string.kernelinstalled))
					.setNegativeButton(
							context.getString(R.string.button_cancel),
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
									Utils.runCommand("reboot");
								}
							}).show();

		} else {
			Builder builder = new Builder(context);
			builder.setTitle(context.getString(R.string.app_name))
					.setMessage(context.getString(R.string.somethingwrong))
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									((Activity) context).finish();
								}
							})
					.setNeutralButton(context.getString(R.string.showsolution),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									showSolution(context);
								}
							}).show();
		}
	}

	private static void showSolution(final Context context) {
		Builder builder = new Builder(context);
		builder.setMessage(context.getString(R.string.solution))
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						((Activity) context).finish();
					}
				}).show();
	}
}
