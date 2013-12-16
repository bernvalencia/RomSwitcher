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

package com.grarak.romswitcher.Fragment;

import com.grarak.romswitcher.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class AboutUsFragment extends PreferenceFragment {

	private static final CharSequence KEY_ME = "key_grarak";
	private static final CharSequence KEY_MITHUN = "key_mithun46";
	private static final CharSequence KEY_BLUE = "key_bluefa1con";
	private static final CharSequence KEY_VIK = "key_disturbed";

	private static final String grarakDonate = "http://forum.xda-developers.com/donatetome.php?u=4443334";
	private static final String mithunDonate = "http://forum.xda-developers.com/donatetome.php?u=4922752";
	private static final String blueDonate = "http://forum.xda-developers.com/donatetome.php?u=4209428";
	private static final String vikDonate = "http://forum.xda-developers.com/donatetome.php?u=4655143";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.about_us_header);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals(KEY_ME))
			openLink(grarakDonate, getActivity());
		else if (preference.getKey().equals(KEY_MITHUN))
			openLink(mithunDonate, getActivity());
		else if (preference.getKey().equals(KEY_BLUE))
			openLink(blueDonate, getActivity());
		else if (preference.getKey().equals(KEY_VIK))
			openLink(vikDonate, getActivity());
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private static void openLink(String link, Context context) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		context.startActivity(browserIntent);
	}
}
