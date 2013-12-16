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

import static com.stericson.RootTools.RootTools.debugMode;

import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.SupportedDevices;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class StartActivity extends Activity {

	private static final String SECONDROM = "/.firstrom";
	private static final String sdcard = "/sdcard";
	private static final String FIRST_IMG = sdcard + "/romswitcher/first.img";
	private static final String PREF = "prefs";
	private static SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		debugMode = true;

		SupportedDevices.getDevices();

		if (SupportedDevices.downloadlink.isEmpty()) {
			Utils.alert(this, getString(R.string.app_name),
					getString(R.string.nosupport));
		} else {
			checkkernel(this);
		}
	}

	private static void checkkernel(Context context) {
		mPref = context.getSharedPreferences(PREF, 0);
		if (!SupportedDevices.onekernel) {
			String mKernelVersion = mPref.getString("kernelversion", "nothing");
			SharedPreferences.Editor editor = mPref.edit();

			if (!Utils.existFile(SECONDROM)) {
				if (mKernelVersion.equals("nothing")
						|| mKernelVersion.equals(Utils
								.getFormattedKernelVersion())) {
					editor.putString("kernelversion",
							Utils.getFormattedKernelVersion());
					editor.commit();
				} else {
					Utils.toast(context, context.getString(R.string.newkernel),
							0);
					Utils.displayprogress(
							context.getString(R.string.setupnewkernel), context);
					loadKernel(context);
					editor.putString("kernelversion",
							Utils.getFormattedKernelVersion());
					editor.commit();
				}
			}
		}
		setup(context);
	}

	private static void setup(Context context) {
		boolean mFirstuse = mPref.getBoolean("firstuse", true);
		if (mFirstuse && !Utils.existFile(SECONDROM)) {
			context.startActivity(new Intent(context, MainSetupActivity.class));
			((Activity) context).finish();
		} else {
			context.startActivity(new Intent(context,
					CheckforFilesActivity.class));
			((Activity) context).finish();
		}
	}

	private static void loadKernel(final Context context) {
		Thread pause = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					GetKernel.pullkernel();
					Thread.sleep(1000);
					if (!Utils.existFile(FIRST_IMG)) {
						Utils.toast(context,
								context.getString(R.string.somethingwrong), 0);
						((Activity) context).finish();
					}
					Utils.hideprogress();
				} catch (Exception e) {
				}
			}
		});
		pause.start();
	}
}
