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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetNameActivity extends Activity {
	
	private static final String PREF_FIRST_USE = "firstuse";
	private static String PREF_NAME_FIRST = "first_rom_name";
	private static String PREF_NAME_SECOND = "second_rom_name";
	private static Button mButtonNext;
	private static EditText mFirstEdit, mSecondEdit;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.set_name);
	    
	    mFirstEdit = (EditText)findViewById(R.id.firstrom_edit);
	    mSecondEdit = (EditText)findViewById(R.id.secondrom_edit);
	    
	    mButtonNext = (Button)findViewById(R.id.button_next_setname);
	    
	    mButtonNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFirstEdit.getText().toString().isEmpty() || mSecondEdit.getText().toString().isEmpty()) {
					Utils.toast(SetNameActivity.this, getString(R.string.emptynames), 0);
				} else {
					SharedPreferences FIRST_NAME = getSharedPreferences(PREF_NAME_FIRST, 0);
					SharedPreferences SECOND_NAME = getSharedPreferences(PREF_NAME_SECOND, 0);
					SharedPreferences FIRST_USE = getSharedPreferences(PREF_FIRST_USE, 0);
					
					SharedPreferences.Editor eFirstname = FIRST_NAME.edit();
					SharedPreferences.Editor eSecondname = SECOND_NAME.edit();
					SharedPreferences.Editor eFirstuse = FIRST_USE.edit();
					
					eFirstname.putString("firstname", mFirstEdit.getText().toString());
					eSecondname.putString("secondname", mSecondEdit.getText().toString());
					eFirstuse.putBoolean("firstuse", false);
					
					eFirstname.commit();
					eSecondname.commit();
					eFirstuse.commit();
				}
			}
		});
	}
}
