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

	private static String DATA;
	private static String ROM_SELECTION;

	private static File mSecondSystem;
	private static File mThirdSystem;
	private static File mSecondRom = new File("/.firstrom");

	public static void chooserom(final Context context, String title,
			final String firstrom, final String secondrom, final String thirdrom) {
		Builder builder = new Builder(context);
		builder.setTitle(title);

		if (mSecondRom.exists()) {
			DATA = "/.firstrom";
		} else {
			DATA = "/data";
		}

		ROM_SELECTION = DATA + "/media/.rom";
		mSecondSystem = new File(DATA + "/media/.secondrom/system.img");
		mThirdSystem = new File(DATA + "/media/.thirdrom/system.img");

		List<String> listItems = new ArrayList<String>();
		listItems.add(firstrom);
		if (mSecondSystem.exists() || mSecondRom.exists()) {
			listItems.add(secondrom);
		}
		if (mThirdSystem.exists() || mSecondRom.exists()) {
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
									flashKernel("first", 0, context);
								} else if (choiceList[buffKey].toString()
										.equals(secondrom)) {
									flashKernel("second", 1, context);
									selected = buffKey;
									((Activity) context).finish();
								} else if (choiceList[buffKey].toString()
										.equals(thirdrom)) {
									flashKernel("second", 2, context);
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

	private static void flashKernel(String rom, int romnumber, Context context) {
		Utils.runCommand("echo " + romnumber + " > " + ROM_SELECTION, 0);
		if (!mSecondRom.exists() || romnumber == 0) {
			Utils.runCommand("dd if=" + getExternalStorageDirectory().getPath()
					+ "/romswitcher/" + rom + ".img of="
					+ SupportedDevices.bootpartition
					+ " && echo 1 > /proc/sys/kernel/sysrq"
					+ " && echo b > /proc/sysrq-trigger", 0);
		}
		Utils.runCommand("reboot", 1);
	}
}
