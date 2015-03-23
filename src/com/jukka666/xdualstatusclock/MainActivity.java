package com.jukka666.xdualstatusclock;

import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

@SuppressLint("WorldReadableFiles")
public class MainActivity extends PreferenceActivity {
	private SharedPreferences mPrefs;
	private ListPreference selected, divider;
	private CheckBoxPreference dst, twentyfour;
	ListPreference ampm;
	private TimePreference customoff;
	

	@SuppressWarnings("deprecation")
	public SharedPreferences getSharedPreferences(String name, int mode) {
		return super.getSharedPreferences(Misc.PREFERENCE,
				Context.MODE_WORLD_READABLE);
	}


	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
		addPreferencesFromResource(R.xml.pref);

		selected = (ListPreference) findPreference(Misc.PREF_SELECTED);

		mPrefs = getSharedPreferences(Misc.PREFERENCE,
				Context.MODE_WORLD_READABLE);

		selected.setDefaultValue(mPrefs.getString(Misc.PREF_SELECTED, "GMT"));
		selected.setSummary(" ");
		selected.setSummary("%s");
		selected.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				updatePreference(preference, newValue);
				selected.setSummary(" ");
				selected.setSummary("%s");

				Context context = getApplicationContext();
				SharedPreferences prefs = getSharedPreferences(Misc.PREFERENCE,
						Context.MODE_WORLD_READABLE);
				CharSequence text = "Offset selected: "
						+ prefs.getString(Misc.PREF_SELECTED, "GMT")+". Reboot to apply changes.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return true;
			}
		});
		
		dst = (CheckBoxPreference) findPreference(Misc.PREF_DST);
		dst.setDefaultValue(mPrefs.getBoolean(Misc.PREF_DST, false));
		dst.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				updatePreference(preference, newValue);

				Context context = getApplicationContext();
				SharedPreferences prefs = getSharedPreferences(Misc.PREFERENCE,
						Context.MODE_WORLD_READABLE);
				CharSequence text = "";
				if(prefs.getBoolean(Misc.PREF_DST, false))
					{text = "DST selected. Reboot to apply changes.";}
				else {text ="DST not selected. Reboot to apply changes.";}
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return true;
			}
		});
		
		divider = (ListPreference) findPreference(Misc.PREF_DIV);
		divider.setDefaultValue(mPrefs.getString(Misc.PREF_DIV, "|"));
		divider.setSummary(" ");
		divider.setSummary("%s");
		divider.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				updatePreference(preference, newValue);
				divider.setSummary(" ");
				divider.setSummary("%s");

				Context context = getApplicationContext();
				SharedPreferences prefs = getSharedPreferences(Misc.PREFERENCE,
						Context.MODE_WORLD_READABLE);
				CharSequence text = "Divider selected: "
						+ prefs.getString(Misc.PREF_DIV, "|")+" Reboot to apply changes.";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				return true;
			}
		});
		

		
		twentyfour = (CheckBoxPreference) findPreference(Misc.PREF_24);
		twentyfour.setDefaultValue(mPrefs.getBoolean(Misc.PREF_24, false));
		twentyfour.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				updatePreference(preference, newValue);
				updatePreference(preference, newValue);
				Context context = getApplicationContext();
				SharedPreferences prefs = getSharedPreferences(Misc.PREFERENCE,
						Context.MODE_WORLD_READABLE);
				ampm.setEnabled(!prefs.getBoolean(Misc.PREF_24, true));
				return true;
			}
		});
		ampm = (ListPreference) findPreference(Misc.PREF_AMPM);
		ampm.setEnabled(!mPrefs.getBoolean(Misc.PREF_24, false));
		ampm.setDefaultValue(mPrefs.getString(Misc.PREF_AMPM, "none"));
		ampm.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {


				return true;
			}
		});
		
		
		
//		customoff = (TimePreference) findPreference(Misc.PREF_OFF);
//		customoff.setDefaultValue(mPrefs.getString(Misc.PREF_OFF, "00:00"));
//		customoff.setSummary(" ");
//		customoff.setSummary("GMT+"+mPrefs.getString(Misc.PREF_OFF, "0"));
//		customoff.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			@Override
//			public boolean onPreferenceChange(Preference preference,
//					Object newValue) {
//				updatePreference(preference, newValue);
//				customoff.setSummary(" ");
//
//				Context context = getApplicationContext();
//				SharedPreferences prefs = getSharedPreferences(Misc.PREFERENCE,
//						Context.MODE_WORLD_READABLE);
//				customoff.setSummary(prefs.getString(Misc.PREF_OFF, "GMT"));
//				CharSequence text = "Custom offset set: GMT+"
//						+ prefs.getString(Misc.PREF_OFF, "0")+" Reboot to apply changes.";
//				int duration = Toast.LENGTH_SHORT;
//				Toast toast = Toast.makeText(context, text, duration);
//				toast.show();
//				return true;
//			}
//		});
		
		
	}

	protected void commited() {
		Intent i = new Intent(Misc.SETTINGS_UPDATED_INTENT);
		sendBroadcast(i);
	}

	@SuppressWarnings("unchecked")
	private void updatePreference(Preference preference, Object newValue) {
		Editor editor = mPrefs.edit();
		if (newValue instanceof String) {
			editor.putString(preference.getKey(), (String) newValue);
		} else if (newValue instanceof Integer) {
			editor.putInt(preference.getKey(), (Integer) newValue);
		} else if (newValue instanceof Set<?>) {
			editor.putStringSet(preference.getKey(), (Set<String>) newValue);
		} else if (newValue instanceof Boolean) {
			editor.putBoolean(preference.getKey(), (Boolean) newValue);
		}
		editor.commit();
		commited();
	}

}


