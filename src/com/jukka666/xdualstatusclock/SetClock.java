package com.jukka666.xdualstatusclock;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.SharedPreferences;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class SetClock implements IXposedHookZygoteInit, IXposedHookLoadPackage {

	private static final String PACKAGE_NAME = SetClock.class.getPackage()
			.getName();
	private SharedPreferences prefs;

	public void initZygote(IXposedHookZygoteInit.StartupParam startupParam)
			throws Throwable {
		prefs = new XSharedPreferences(PACKAGE_NAME, Misc.PREFERENCE);
	}

	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {
		if (!lpparam.packageName.equals("com.android.systemui"))
			return;
		

		findAndHookMethod("com.android.systemui.statusbar.policy.Clock",
				lpparam.classLoader, "updateClock", new XC_MethodHook() {
					@Override
					protected void afterHookedMethod(MethodHookParam param)
							throws Throwable {

						String sb = prefs.getString(Misc.PREF_SELECTED, "GMT");
						Boolean dst = prefs.getBoolean(Misc.PREF_DST, false);
						String div = prefs.getString(Misc.PREF_DIV, "|");
//						String custom = prefs.getString(Misc.PREF_OFF, "GMT");
						Boolean twentyfour = prefs.getBoolean(Misc.PREF_24, true);
						String ampm = prefs.getString(Misc.PREF_AMPM, "none");

//						if (sb=="custom") sb="GMT+"+custom;
						
						TimeZone tz = TimeZone.getTimeZone(sb);
						Calendar c = Calendar.getInstance(tz);
						int hour;
						int minute;
						String ampmtext ="";
						if (twentyfour) {
							hour = c.get(Calendar.HOUR_OF_DAY);
							minute = c.get(Calendar.MINUTE);
							if (dst)
								hour++;
							if (hour == 24) {
								hour = 00;
							}
							;
						}
						else {
							if (dst) c.roll(Calendar.HOUR, true);
							 hour = c.get(Calendar.HOUR);
							 minute = c.get(Calendar.MINUTE);
							if (ampm=="major") {
								if (c.get(Calendar.AM_PM) == Calendar.AM ) ampmtext = "AM";
								if (c.get(Calendar.AM_PM) == Calendar.PM ) ampmtext = "PM";
							}
							if (ampm=="minor") {
								if (c.get(Calendar.AM_PM) == Calendar.AM ) ampmtext = "am";
								if (c.get(Calendar.AM_PM) == Calendar.PM ) ampmtext = "pm";
							}
							
							
								}
						
						TextView tv = (TextView) param.thisObject;
						String text = tv.getText().toString();

						String stringhours = String.valueOf(hour);
						if (stringhours.length() == 1)
							stringhours = "0" + stringhours;

						String strminutes = String.valueOf(minute);
						if (strminutes.length() == 1)
							strminutes = "0" + strminutes;
						strminutes = strminutes.replaceAll(" ", "");

						tv.setText(stringhours + ":" + strminutes +  ampmtext + div + text);
					}
				});
	}
}