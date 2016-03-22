package com.example.coolweather.service;

import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		new Thread(new Runnable(){

			@Override
			public void run() {
				updateWeather();
			}
			
		}).start();
		AlarmManager _manager = (AlarmManager)getSystemService(ALARM_SERVICE);
		int _time = 8 * 60 * 60 * 1000;
		long _triggerTime = SystemClock.elapsedRealtime() + _time;
		Intent _intent = new Intent(this,this.getClass());
		PendingIntent _pendingIntent = PendingIntent.getService(this, 0, _intent, 0);
		_manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, _triggerTime, _pendingIntent);
		
		return super.onStartCommand(intent, flags, startId);

	}

    private void updateWeather(){
    	
    	SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	String _weatherCode = _sharedPreferences.getString("weather_code", "");
    	String _address = "http://www.weather.com.cn/data/cityinfo/" + _weatherCode + ".html";
    	HttpCallbackListener _listener = new HttpCallbackListener(){

			@Override
			public void onFinish(String pResponse) {

				Utility.handleWeatherResponse(AutoUpdateService.this, pResponse);
			}

			@Override
			public void onError(Exception pException) {

				pException.printStackTrace();
			}
    		
    	};
    	HttpUtil.sendHttpRequest(_address, _listener);
    }
}
