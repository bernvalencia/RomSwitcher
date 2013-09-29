/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher.Fragment;

import com.grarak.romswitcher.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class AboutUsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.about_us_header);
	}
}
