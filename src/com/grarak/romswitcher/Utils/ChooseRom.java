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

import com.grarak.romswitcher.MoreSettingsActitvity;
import com.grarak.romswitcher.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ChooseRom {

	private static int selected = 0;
	private static int buffKey = 0;

	public static void chooserom(final Context context, String title,
			final String firstrom, String secondrom) {
		Builder builder = new Builder(context);
		builder.setTitle(title);

		final CharSequence[] choiceList = { firstrom, secondrom };

		builder.setSingleChoiceItems(choiceList, selected,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (choiceList[buffKey].toString().equals(firstrom)) {
						}
						buffKey = which;
					}
				})
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						((Activity) context).finish();
					}
				})
				.setPositiveButton(context.getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (choiceList[buffKey].toString().equals(
										firstrom)) {
									flashkernel("first", context);
								} else {
									flashkernel("second", context);
									selected = buffKey;
									((Activity) context).finish();
								}
							}
						})
				.setNegativeButton(context.getString(R.string.more),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(context,
										MoreSettingsActitvity.class);
								context.startActivity(i);
								((Activity) context).finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private static void flashkernel(String rom, Context context) {
		Utils.runCommand("dd if=" + getExternalStorageDirectory().getPath()
				+ "/romswitcher/" + rom + ".img of="
				+ SupportedDevices.bootpartition
				+ " && echo 1 > /proc/sys/kernel/sysrq"
				+ " && echo b > /proc/sysrq-trigger", 0);
		Utils.runCommand("reboot", 1);
	}
}
