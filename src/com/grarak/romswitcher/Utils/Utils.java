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

import static com.stericson.RootTools.RootTools.getShell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.grarak.romswitcher.R;
import com.stericson.RootTools.CommandCapture;

import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	public static String mHtmlstring = "";
	private static ProgressDialog mProgressDialog;
	private static final String FILENAME_PROC_VERSION = "/proc/version";

	public static void deleteFiles(String path) {

		File file = new File(path);

		if (file.exists()) {
			String deleteCmd = "rm -rf " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (IOException e) {
			}
		}
	}

	public static void displayprogress(String message, final Context context) {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(message);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog
				.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						((Activity) context).finish();
					}
				});
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
	}

	public static void hideprogress() {
		mProgressDialog.hide();
	}

	public static void alert(final Context context, String title, String message) {
		Builder builder = new Builder(context);
		builder.setTitle(title)
				.setMessage(message)
				.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						((Activity) context).finish();
					}
				})
				.setNeutralButton(context.getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Activity) context).finish();
							}
						}).show();
	}

	public static void toast(Context context, String text, int time) {
		if (time == 1) {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}
	}

	private static String readLine(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename),
				256);
		try {
			return reader.readLine();
		} finally {
			reader.close();
		}
	}

	public static String getFormattedKernelVersion() {
		try {
			return formatKernelVersion(readLine(FILENAME_PROC_VERSION));

		} catch (IOException e) {
			Log.e("RomSwitcher",
					"IO Exception when getting kernel version for Device Info screen",
					e);

			return "Unavailable";
		}
	}

	public static String formatKernelVersion(String rawKernelVersion) {

		final String PROC_VERSION_REGEX = "Linux version (\\S+) "
				+ "\\((\\S+?)\\) " + "(?:\\(gcc.+? \\)) " + "(#\\d+) "
				+ "(?:.*?)?" + "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)";

		Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(
				rawKernelVersion);
		if (!m.matches()) {
			Log.e("RomSwitcher", "Regex did not match on /proc/version: "
					+ rawKernelVersion);
			return "Unavailable";
		} else if (m.groupCount() < 4) {
			Log.e("RomSwitcher", "Regex match on /proc/version only returned "
					+ m.groupCount() + " groups");
			return "Unavailable";
		}
		return m.group(1);
	}

	public static void getconnection(String url) {

		DownloadWebPageTask task = new DownloadWebPageTask();
		task.execute(new String[] { url });
	}

	private static class DownloadWebPageTask extends
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			mHtmlstring = Html.fromHtml(result).toString();
		}
	}

	public static void runCommand(String run) {
		try {
			getShell(true).add(new CommandCapture(0, run)).waitForFinish();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
