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
import java.io.IOException;

import com.grarak.romswitcher.R;
import com.grarak.romswitcher.UpdateFinishActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class BootReceiver extends BroadcastReceiver {

	private static String HTML;
	private static String mHTMLtext = "";

	private static final String sdcard = getExternalStorageDirectory()
			.getPath();

	private static final String HTML_FILE = sdcard
			+ "/romswitcher-tmp/htmllink";
	private static final String OTA_FILE = sdcard + "/romswitcher-tmp/ota";

	private static final File mOTAfile = new File(OTA_FILE);
	private static final File mHTMLfile = new File(HTML_FILE);

	private static final int NOTIFY_ME_ID = 2;
	private static int count = 0;
	private static Context context;
	private static NotificationManager notifyMgr = null;

	@SuppressWarnings("static-access")
	@Override
	public void onReceive(Context activity, Intent intent) {
		context = activity;

		notifyMgr = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		try {
			mHTMLtext = Utils.readLine(HTML_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!mOTAfile.exists() || mHTMLfile.exists() || !mHTMLtext.isEmpty()
				|| !SupportedDevices.downloadlink.isEmpty()) {
			SupportedDevices.getDevices();
			Utils.getconnection(SupportedDevices.downloadlink);
			DisplayString task = new DisplayString();
			task.execute();
		}
	}

	private static class DisplayString extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			return Utils.mHtmlstring;
		}

		@Override
		protected void onPostExecute(String result) {
			HTML = Utils.mHtmlstring.toString();
			if (!HTML.isEmpty() && !HTML.contains("ffline")
					&& !mHTMLtext.equals(HTML)) {
				showNotification();
			}
		}
	}

	@SuppressWarnings("deprecation")
	private static void showNotification() {
		Notification notifyObj = new Notification(R.drawable.ic_launcher,
				context.getString(R.string.app_name),
				System.currentTimeMillis());
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				new Intent(context, UpdateFinishActivity.class), 0);
		notifyObj.setLatestEventInfo(context,
				context.getString(R.string.app_name),
				context.getString(R.string.updateavaible), intent);
		notifyObj.number = ++count;
		notifyObj.flags |= Notification.FLAG_AUTO_CANCEL;
		notifyMgr.notify(NOTIFY_ME_ID, notifyObj);
	}
}