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
import static com.stericson.RootTools.RootTools.isBusyboxAvailable;
import static com.stericson.RootTools.RootTools.isRootAvailable;

import java.io.File;
import java.io.IOException;

import com.grarak.romswitcher.Utils.ChooseRom;
import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

public class CheckforFilesActivity extends Activity {

	private static EditText mPasstext;
	private static final File secondrom = new File("/.firstrom/app");

	private static String sdcard = getExternalStorageDirectory().getPath();

	private static final File firstimg = new File(sdcard
			+ "/romswitcher/first.img");
	private static final File secondimg = new File(sdcard
			+ "/romswitcher/second.img");
	private static final File zip = new File(sdcard
			+ "/romswitcher/download.zip");
	private static final String FIRST_NAME_FILE = sdcard
			+ "/romswitcher-tmp/firstname";
	private static final String SECOND_NAME_FILE = sdcard
			+ "/romswitcher-tmp/secondname";
	private static final String THIRD_NAME_FILE = sdcard
			+ "/romswitcher-tmp/thirdname";
	private static final String PASS_FILE = sdcard + "/romswitcher-tmp/pass";

	private static final File mFirstname = new File(FIRST_NAME_FILE);
	private static final File mSecondname = new File(SECOND_NAME_FILE);
	private static final File mThirdname = new File(THIRD_NAME_FILE);
	private static final File mPassfile = new File(PASS_FILE);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mPassfile.exists() && firstimg.exists() && secondimg.exists()) {
			checkPassword(this);
		} else {
			secondPart(this);
		}
	}

	private static void secondPart(final Context context) {
		File rs = new File(sdcard + "/romswitcher");
		rs.mkdirs();

		File rstmp = new File(sdcard + "/romswitcher-tmp");
		rstmp.mkdirs();

		if (!isRootAvailable()) {
			Utils.toast(context, context.getString(R.string.noroot), 0);
			((Activity) context).finish();
		} else if (!isBusyboxAvailable()) {
			Utils.toast(context, context.getString(R.string.nobusybox), 0);
			((Activity) context).finish();
		}

		if (!firstimg.exists()) {
			if (secondrom.exists()) {
				Utils.alert(context, context.getString(R.string.app_name),
						context.getString(R.string.nofirstimg));
			} else {
				Utils.displayprogress(context.getString(R.string.getfirstimg),
						context);
				Thread pause = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							GetKernel.pullkernel();
							Thread.sleep(1000);
							if (!firstimg.exists()) {
								Utils.toast(context, context
										.getString(R.string.somethingwrong), 0);
								((Activity) context).finish();
							} else {
								restartIntent(context);
							}
							Utils.hideprogress();
						} catch (Exception e) {
							e.getLocalizedMessage();
						}
					}
				});
				pause.start();
			}
		} else if (!secondimg.exists() && zip.exists()) {
			unzip(context);
		} else if (!secondimg.exists() && !zip.exists()) {
			Intent i = new Intent(context, LinkActivity.class);
			context.startActivity(i);
			((Activity) context).finish();
		} else if (firstimg.exists() && secondimg.exists()) {
			start(context);
		}
	}

	private static void start(Context context) {
		if (mFirstname.exists() && mSecondname.exists() && mThirdname.exists()) {
			try {
				ChooseRom.chooserom(context,
						context.getString(R.string.app_name),
						Utils.readLine(FIRST_NAME_FILE),
						Utils.readLine(SECOND_NAME_FILE),
						Utils.readLine(THIRD_NAME_FILE));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ChooseRom.chooserom(context, context.getString(R.string.app_name),
					context.getString(R.string.firstrom),
					context.getString(R.string.secondrom),
					context.getString(R.string.thirdrom));
		}
	}

	private static void restartIntent(Context context) {
		Intent i = new Intent(context, StartActivity.class);
		context.startActivity(i);
		((Activity) context).finish();
	}

	private static void unzip(Context context) {
		Utils.runCommand(
				"unzip /sdcard/romswitcher/download.zip -d /sdcard/romswitcher/",
				0);
		restartIntent(context);
	}

	private static void checkPassword(final Context context) {
		mPasstext = new EditText(context);
		mPasstext.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		Builder builder = new Builder(context);
		builder.setView(mPasstext)
				.setTitle(context.getString(R.string.password))
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						((Activity) context).finish();
					}
				})
				.setNegativeButton(context.getString(R.string.button_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Activity) context).finish();
							}
						})
				.setPositiveButton(context.getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								checkPass(context);
							}
						}).show();
	}

	private static void checkPass(Context context) {
		String password = "";
		try {
			password = Utils.readLine(PASS_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (mPasstext.getText().toString().trim().equals(password)) {
			secondPart(context);
		} else {
			Utils.toast(context, context.getString(R.string.passwordwrong), 0);
			checkPassword(context);
		}
	}
}
