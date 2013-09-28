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
