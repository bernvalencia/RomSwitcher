package com.grarak.romswitcher.Utils;

import static android.os.Environment.getExternalStorageDirectory;
import static com.stericson.RootTools.RootTools.getShell;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.grarak.romswitcher.R;
import com.grarak.romswitcher.StartActivity;
import com.stericson.RootTools.CommandCapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class ChooseRom {

	private static int selected = 0;
	private static int buffKey = 0;

	public static void chooserom(final Context context, String title,
			final String firstrom, String secondrom) {
		Builder builder = new Builder(context);
		builder.setTitle(title);

		final CharSequence[] choiceList = { firstrom, secondrom };

		builder.setSingleChoiceItems(choiceList, selected,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (choiceList[buffKey].toString().equals(firstrom)) {
						}
						buffKey = which;
					}
				})
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						((Activity) context).finish();
					}
				})
				.setPositiveButton(context.getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (choiceList[buffKey].toString().equals(
										firstrom)) {
									flashkernel("first", context);
								} else {
									flashkernel("second", context);
									selected = buffKey;
									((Activity) context).finish();
								}
							}
						})
				.setNegativeButton(context.getString(R.string.more),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								((Activity) context).finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private static void flashkernel(String rom, Context context) {
		try {
			getShell(true).add(
					new CommandCapture(0, "dd if="
							+ getExternalStorageDirectory().getPath()
							+ "/romswitcher/" + rom + ".img of="
							+ StartActivity.bootpartition,
							"echo 1 > /proc/sys/kernel/sysrq",
							"echo b > /proc/sysrq-trigger", "reboot"))
					.waitForFinish();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
