package com.jukka666.xdualstatusclock;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePreference extends DialogPreference {
    private int lastHour=0;
    private int lastMinute=0;
    private TimePicker picker=null;
    static String offtime;

    public static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }
    
    public static String getOffset () {
    	String hour = "0", minute="0";
    	if (getHour(offtime)<10) hour = "0"+String.valueOf(getHour(offtime));
    	else hour = String.valueOf(getHour(offtime));
    	if (getMinute(offtime)<10) minute = "0"+String.valueOf(getMinute(offtime));
    	else minute = String.valueOf(getMinute(offtime));
    	return "GMT+" + hour + ":" + minute;
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView() {
        picker=new TimePicker(getContext());

        return(picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour=picker.getCurrentHour();
            lastMinute=picker.getCurrentMinute();
            
            String hour="00", minute="00";
        	if (lastHour<10) hour = "0"+String.valueOf(lastHour);
        	else hour = String.valueOf(lastHour);
        	if (lastMinute<10) minute = "0"+String.valueOf(lastMinute);
        	else minute = String.valueOf(getMinute(offtime));

            String time=hour+":"+minute;
            offtime = time;

            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time=null;

        if (restoreValue) {
            if (defaultValue==null) {
                time=getPersistedString("00:00");
            }
            else {
                time=getPersistedString(defaultValue.toString());
            }
        }
        else {
            time=defaultValue.toString();
        }

        lastHour=getHour(time);
        lastMinute=getMinute(time);
    }
}
