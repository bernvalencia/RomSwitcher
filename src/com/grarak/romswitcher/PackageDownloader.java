/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grarak.romswitcher;

import static android.app.DownloadManager.COLUMN_REASON;
import static android.app.DownloadManager.COLUMN_STATUS;
import static android.app.DownloadManager.ERROR_CANNOT_RESUME;
import static android.app.DownloadManager.ERROR_DEVICE_NOT_FOUND;
import static android.app.DownloadManager.ERROR_FILE_ALREADY_EXISTS;
import static android.app.DownloadManager.ERROR_FILE_ERROR;
import static android.app.DownloadManager.ERROR_HTTP_DATA_ERROR;
import static android.app.DownloadManager.ERROR_INSUFFICIENT_SPACE;
import static android.app.DownloadManager.ERROR_TOO_MANY_REDIRECTS;
import static android.app.DownloadManager.ERROR_UNHANDLED_HTTP_CODE;
import static android.app.DownloadManager.ERROR_UNKNOWN;
import static android.app.DownloadManager.PAUSED_QUEUED_FOR_WIFI;
import static android.app.DownloadManager.PAUSED_UNKNOWN;
import static android.app.DownloadManager.PAUSED_WAITING_FOR_NETWORK;
import static android.app.DownloadManager.PAUSED_WAITING_TO_RETRY;
import static android.app.DownloadManager.STATUS_FAILED;
import static android.app.DownloadManager.STATUS_PAUSED;
import static android.app.DownloadManager.STATUS_PENDING;
import static android.app.DownloadManager.STATUS_RUNNING;
import static android.app.DownloadManager.STATUS_SUCCESSFUL;

import com.grarak.romswitcher.Utils.Utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class PackageDownloader extends Activity {

	public static String mDownloadlink;
	public static String mDownloadfolder;
	public static String mDownloadname;

	private static final String strPref_Download_ID = "PREF_DOWNLOAD_ID";

	private static SharedPreferences preferenceManager;
	private static DownloadManager downloadManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		preferenceManager = PreferenceManager
				.getDefaultSharedPreferences(PackageDownloader.this);
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		Download(mDownloadlink, mDownloadname);

		Utils.alert(this, getString(R.string.app_name), getString(R.string.openagain));
	}

	@Override
	protected void onResume() {
		super.onResume();

		CheckDwnloadStatus();

		IntentFilter intentFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(downloadReceiver);
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			CheckDwnloadStatus();
		}
	};

	private void CheckDwnloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		long id = preferenceManager.getLong(strPref_Download_ID, 0);
		query.setFilterById(id);

		Cursor cursor = downloadManager.query(query);
		if (cursor.moveToFirst()) {
			int columnIndex = cursor.getColumnIndex(COLUMN_STATUS);
			int status = cursor.getInt(columnIndex);
			int columnReason = cursor.getColumnIndex(COLUMN_REASON);
			int reason = cursor.getInt(columnReason);

			switch (status) {
			case STATUS_FAILED:
				String failedReason = "";
				Log.d(getString(R.string.app_name), "Failedreason:"
						+ failedReason);

				switch (reason) {
				case ERROR_CANNOT_RESUME:
					failedReason = "ERROR_CANNOT_RESUME";
					break;

				case ERROR_DEVICE_NOT_FOUND:
					failedReason = "ERROR_DEVICE_NOT_FOUND";
					break;

				case ERROR_FILE_ALREADY_EXISTS:
					failedReason = "ERROR_FILE_ALREADY_EXISTS";
					break;

				case ERROR_FILE_ERROR:
					failedReason = "ERROR_FILE_ERROR";
					break;

				case ERROR_HTTP_DATA_ERROR:
					failedReason = "ERROR_HTTP_DATA_ERROR";
					break;

				case ERROR_INSUFFICIENT_SPACE:
					failedReason = "ERROR_INSUFFICIENT_SPACE";
					break;

				case ERROR_TOO_MANY_REDIRECTS:
					failedReason = "ERROR_TOO_MANY_REDIRECTS";
					break;

				case ERROR_UNHANDLED_HTTP_CODE:
					failedReason = "ERROR_UNHANDLED_HTTP_CODE";
					break;

				case ERROR_UNKNOWN:
					failedReason = "ERROR_UNKNOWN";
					break;
				}
				break;

			case STATUS_PAUSED:
				String pausedReason = "";
				Log.d(getString(R.string.app_name), "Pausedreason:"
						+ pausedReason);

				switch (reason) {
				case PAUSED_QUEUED_FOR_WIFI:
					pausedReason = "PAUSED_QUEUED_FOR_WIFI";
					break;

				case PAUSED_UNKNOWN:
					pausedReason = "PAUSED_UNKNOWN";
					break;

				case PAUSED_WAITING_FOR_NETWORK:
					pausedReason = "PAUSED_WAITING_FOR_NETWORK";
					break;

				case PAUSED_WAITING_TO_RETRY:
					pausedReason = "PAUSED_WAITING_FOR_RETRY";
					break;
				}
				break;

			case STATUS_PENDING:
				break;

			case STATUS_RUNNING:
				break;

			case STATUS_SUCCESSFUL:
				break;
			}
		}
	}

	private void Download(String LINK, String NAME) {

		Uri downloadUri = Uri.parse(LINK);
		DownloadManager.Request request = new DownloadManager.Request(
				downloadUri);
		request.setTitle(NAME);
		request.allowScanningByMediaScanner();
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setDestinationInExternalPublicDir(mDownloadfolder, NAME);
		long id = downloadManager.enqueue(request);

		Editor PrefEdit = preferenceManager.edit();
		PrefEdit.putLong(strPref_Download_ID, id);
		PrefEdit.commit();
	}
}
