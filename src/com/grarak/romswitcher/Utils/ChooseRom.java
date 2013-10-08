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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
	private static CharSequence[] choiceList;

	private static final String SECOND_ROM = "/data/media/.secondrom/system.img";
	private static final String THIRD_ROM = "/data/media/.thirdrom/system.img";

	private static final File mSecondSystem = new File(SECOND_ROM);
	private static final File mThirdSystem = new File(THIRD_ROM);

	public static void chooserom(final Context context, String title,
			final String firstrom, final String secondrom, final String thirdrom) {
		Builder builder = new Builder(context);
		builder.setTitle(title);

		List<String> listItems = new ArrayList<String>();
		listItems.add(firstrom);
		if (mSecondSystem.exists()) {
			listItems.add(secondrom);
		}
		if (mThirdSystem.exists()) {
			listItems.add(thirdrom);
		}

		choiceList = listItems.toArray(new CharSequence[listItems.size()]);

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
								} else if (choiceList[buffKey].toString()
										.equals(secondrom)) {
									flashkernel("second", context);
									selected = buffKey;
									Utils.runCommand(
											"echo 1 > /data/media/.rom", 0);
									((Activity) context).finish();
								} else if (choiceList[buffKey].toString()
										.equals(thirdrom)) {
									flashkernel("second", context);
									selected = buffKey;
									Utils.runCommand(
											"echo 2 > /data/media/.rom", 0);
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
