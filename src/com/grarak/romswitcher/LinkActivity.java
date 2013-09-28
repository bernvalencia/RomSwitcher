/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher;

import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class LinkActivity extends Activity {

	private static Context context;
	private static String HTML = "";
	public static String mDownload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		Utils.displayprogress(context.getString(R.string.checkconnection),
				context);
		Utils.getconnection(StartActivity.downloadlink);

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
			if (HTML.isEmpty()) {
				Utils.toast(context, context.getString(R.string.nointernet), 0);
			} else {
				if (HTML.contains("ffline")) {
					Utils.toast(context,
							context.getString(R.string.serverdown), 0);
				} else {
					PackageDownloader.mDownloadfolder = "/romswitcher/";
					PackageDownloader.mDownloadlink = HTML;
					PackageDownloader.mDownloadname = "download.zip";
					Intent i = new Intent(context, PackageDownloader.class);
					context.startActivity(i);
				}
			}
			Utils.hideprogress();
			((Activity) context).finish();
		}
	}
}
