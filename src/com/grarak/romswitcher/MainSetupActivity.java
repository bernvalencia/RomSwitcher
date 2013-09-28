/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher;

import java.io.File;

import com.grarak.romswitcher.Utils.GetKernel;
import com.grarak.romswitcher.Utils.Utils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import static android.os.Environment.getExternalStorageDirectory;
import static com.stericson.RootTools.RootTools.debugMode;
import static com.stericson.RootTools.RootTools.isBusyboxAvailable;
import static com.stericson.RootTools.RootTools.isRootAvailable;

public class MainSetupActivity extends Activity {

	private static String sdcard = getExternalStorageDirectory().getPath();
	private static final File firstimg = new File(sdcard + "/romswitcher/first.img");
	private static Button mNextButton, mCancelButton;
	public static boolean firstuse = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_setup);
		firstuse = true;
		
		Utils.deleteFiles(sdcard + "/romswitcher");

		File RSDirectory = new File(sdcard + "/romswitcher/");
		RSDirectory.mkdirs();

		debugMode = true;

		mNextButton = (Button) findViewById(R.id.button_next);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!isRootAvailable()) {
					Utils.toast(getApplicationContext(),
							getString(R.string.noroot), 0);
					finish();
				} else if (!isBusyboxAvailable()) {
					Utils.toast(getApplicationContext(),
							getString(R.string.nobusybox), 0);
					finish();
				} else {
					Utils.displayprogress(getString(R.string.setuprs),
							MainSetupActivity.this);
					Thread pause = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								GetKernel.pullkernel();
								Thread.sleep(1500);
								if (!firstimg.exists()) {
									Utils.toast(MainSetupActivity.this, getString(R.string.somethingwrong), 0);
									finish();
								}
								Intent i = new Intent(MainSetupActivity.this,
										SetNameActivity.class);
								startActivity(i);
								finish();
								Utils.hideprogress();
							} catch (Exception e) {
								e.getLocalizedMessage();
							}
						}
					});
					pause.start();
				}
			}
		});

		mCancelButton = (Button) findViewById(R.id.button_cancel);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
