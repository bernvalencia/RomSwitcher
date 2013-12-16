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

import com.grarak.romswitcher.Utils.SupportedDevices;
import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class LinkActivity extends Activity {

	private static Context context;
	private static String HTML;
	public static String mDownload;

	private static final String sdcard = "/sdcard";

	private static final String HTML_FILE = sdcard
			+ "/romswitcher-tmp/htmllink";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		Utils.displayprogress(context.getString(R.string.checkconnection),
				context);
		Utils.getconnection(SupportedDevices.downloadlink);

		DisplayString task = new DisplayString();
		task.execute();
	}

	private static class DisplayString extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			return Utils.mHtmlstring;
		}

		@Override
		protected void onPostExecute(String result) {
			HTML = Utils.mHtmlstring.toString();
			if (HTML.isEmpty())
				Utils.toast(context, context.getString(R.string.nointernet), 0);
			else if (HTML.contains("ffline"))
				Utils.toast(context, context.getString(R.string.serverdown), 0);
			else {
				Utils.runCommand("echo \"" + HTML + "\" > " + HTML_FILE);
				PackageDownloader.mDownloadfolder = "/romswitcher/";
				PackageDownloader.mDownloadlink = HTML;
				PackageDownloader.mDownloadname = "download.zip";
				context.startActivity(new Intent(context,
						PackageDownloader.class));
			}
			Utils.hideprogress();
			((Activity) context).finish();
		}
	}
}
