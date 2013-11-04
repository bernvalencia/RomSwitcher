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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/* Thanks guys!
 * http://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
 */

public class Unzip {

	public static void unpackZip(String path, String zipname) {
		InputStream is;
		ZipInputStream zis;
		try {
			String filename;
			is = new FileInputStream(path + zipname);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[2048];
			int count;

			while ((ze = zis.getNextEntry()) != null) {
				filename = ze.getName();
				if (ze.isDirectory()) {
					File fmd = new File(path + filename);
					fmd.mkdirs();
					continue;
				}
				FileOutputStream fout = new FileOutputStream(path + filename);
				while ((count = zis.read(buffer)) != -1) {
					fout.write(buffer, 0, count);
				}
				fout.close();
				zis.closeEntry();
			}
			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
