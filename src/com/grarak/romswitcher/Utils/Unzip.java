/*
 * Copyright (C) 2013 The RomSwitcher Project
 *
 * * Licensed under the GNU GPLv2 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl-2.0.txt
 */

package com.grarak.romswitcher.Utils;

import static com.stericson.RootTools.RootTools.getShell;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.CommandCapture;

public class Unzip {

	public static void unzip(String zipfile, String unziplocation) {
		try {
			getShell(true).add(
					new CommandCapture(0, "unzip " + zipfile + " -d "
							+ unziplocation)).waitForFinish();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
