package com.example.coolweather.activity;

import com.example.coolweather.R;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private TextView tvCityName;
	private TextView tvPublishTime;
	private TextView tvCurrentDate;
	private TextView tvWeatherDes;
	private TextView tvTemp;
	private Button btHome;
	private Button btReflesh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		initialVariable();
		String _countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(_countyCode)){
        	tvPublishTime.setText("同步中...");
    		queryWeatherCode(_countyCode);
		}
        else{
        	showWeather();
        }
	}

	private void initialVariable(){
		
		tvCityName = (TextView)findViewById(R.id.tvCityName);
		tvPublishTime = (TextView)findViewById(R.id.tvPublishTime);
		tvCurrentDate = (TextView)findViewById(R.id.tvCurrentDate); 
		tvWeatherDes = (TextView)findViewById(R.id.tvWeatherDes);
		tvTemp = (TextView)findViewById(R.id.tvTemp);
		btHome = (Button)findViewById(R.id.btHome);
		btReflesh = (Button)findViewById(R.id.btReflesh);
		btHome.setOnClickListener(this);
		btReflesh.setOnClickListener(this);
	}
	
	private void queryWeatherCode(String pCountyCode){
		
		String _address = "http://www.weather.com.cn/data/list3/city" + pCountyCode +".xml";
		queryFromServer(_address, "countyCode");
	}
	
	private void queryWeatherInfo(String pWeatherCode){
		
		String _address = "http://www.weather.com.cn/data/cityinfo/" + pWeatherCode + ".html";
		queryFromServer(_address, "weatherCode");
	}
	
	private void queryFromServer(final String pAddress, final String pType){
		
		HttpCallbackListener _listener = new HttpCallbackListener(){

			@Override
			public void onFinish(String pResponse) {

				if("countyCode".equals(pType)){
					if(!TextUtils.isEmpty(pResponse)){
						String[] _array = pResponse.split("\\|");
						String _weatherCode = _array[1];
						queryWeatherInfo(_weatherCode);
					}
				}
				else if("weatherCode".equals(pType)){
					if(!TextUtils.isEmpty(pResponse)){
						Utility.handleWeatherResponse(WeatherActivity.this, pResponse);
						runOnUiThread(new Runnable(){

							@Override
							public void run() {

								showWeather();
							}
							
						});
					}
				}
			}

			@Override
			public void onError(Exception pException) {

				runOnUiThread(new Runnable(){

					@Override
					public void run() {

						tvPublishTime.setText("同步失败");
					}
					
				});
			}};
		HttpUtil.sendHttpRequest(pAddress, _listener);
	}
	
	private void showWeather(){
		
		SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		tvCityName.setText(_sharedPreferences.getString("city_name", ""));
		tvPublishTime.setText("今天" + _sharedPreferences.getString("publish_time", "") + "发布");
		tvCurrentDate.setText(_sharedPreferences.getString("current_date", ""));
		tvWeatherDes.setText(_sharedPreferences.getString("weather_des", ""));
		tvTemp.setText(_sharedPreferences.getString("temp1", "") + "~" + _sharedPreferences.getString("temp2", ""));
		Intent _intent = new Intent(this,AutoUpdateService.class);
		startService(_intent);
	}

	@Override
	public void onClick(View v) {

		int _id = v.getId();
		switch(_id){
		case R.id.btHome:
			Intent _intent = new Intent(this,ChooseAreaActivity.class);
			_intent.putExtra("back_home", true);
			startActivity(_intent);
			break;
		case R.id.btReflesh:
			tvPublishTime.setText("同步中...");
			SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			String _weatherCode = _sharedPreferences.getString("weather_code", "");
			if(!TextUtils.isEmpty(_weatherCode)){
			    queryWeatherInfo(_weatherCode);
			}
			break;
		default:
			break;
		}
	}
	
}
