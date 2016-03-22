package com.example.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.Log;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;
import com.example.coolweather.model.WeatherInfo;

public class Utility {

	public synchronized static boolean handleProvincesResponse(CoolWeatherDB pCoolWeatherDB, String pResponse){
		
		if(!TextUtils.isEmpty(pResponse)){
			String[] _allProvinces = pResponse.split(",");
			if(_allProvinces != null && _allProvinces.length > 0){
				SQLiteDatabase _database = pCoolWeatherDB.getDatabase();
				_database.beginTransaction();
				for(String _p : _allProvinces){
					String[] _array = _p.split("\\|");//注意转义字符
					Province _province = new Province();
					_province.setName(_array[1]);
					_province.setCode(_array[0]);
					pCoolWeatherDB.saveProvince(_province);
				}
				_database.setTransactionSuccessful();
				_database.endTransaction();
			}
			return true;
		}
		return false;
	}
	
	public static boolean handleCitiesResponse(CoolWeatherDB pCoolWeatherDB, String pResponse, int pProvinceId){
		
		if(!TextUtils.isEmpty(pResponse)){
			String[] _allCities = pResponse.split(",");
			if(_allCities != null && _allCities.length > 0){
				SQLiteDatabase _database = pCoolWeatherDB.getDatabase();
				_database.beginTransaction();
				for(String _c : _allCities){
					String[] _array = _c.split("\\|");
					City _city = new City();
					_city.setName(_array[1]);
					_city.setCode(_array[0]);
					_city.setProviceId(pProvinceId);
					pCoolWeatherDB.saveCity(_city);
				}
				_database.setTransactionSuccessful();
				_database.endTransaction();
			}
			return true;
		}
		return false;
	}
	
	public static boolean handleCountiesResponse(CoolWeatherDB pCoolWeatherDB, String pResponse, int pCityId){
		
		if(!TextUtils.isEmpty(pResponse)){
			String[] _allCounties = pResponse.split(",");
			if(_allCounties != null && _allCounties.length > 0){
				SQLiteDatabase _database = pCoolWeatherDB.getDatabase();
				_database.beginTransaction();
				for(String _c : _allCounties){
					String[] _array = _c.split("\\|");
					County _county = new County();
					_county.setName(_array[1]);
					_county.setCode(_array[0]);
					_county.setCityId(pCityId);
					pCoolWeatherDB.saveCounty(_county);
				}
				_database.setTransactionSuccessful();
				_database.endTransaction();
			}
			return true;
		}
		return false;
	}
	
	public static void handleWeatherResponse(Context pContext, String pResponse){
		
		try {
			JSONObject _jsonObject = new JSONObject(pResponse);
			JSONObject _weatherInfo =_jsonObject.getJSONObject("weatherinfo");
			String _cityName = _weatherInfo.getString("city");
			String _weatherCode = _weatherInfo.getString("cityid");
			String _temp1 = _weatherInfo.getString("temp1");
			String _temp2 = _weatherInfo.getString("temp2");
			String _weatherDes = _weatherInfo.getString("weather");
			String _publishTime = _weatherInfo.getString("ptime");
			WeatherInfo _info = new WeatherInfo();
			_info.setCityName(_cityName);
			_info.setWeatherCode(_weatherCode);
			_info.setTemp1(_temp1);
			_info.setTemp2(_temp2);
			_info.setWeatherDes(_weatherDes);
			_info.setPublishTime(_publishTime);
			saveWeatherInfo(pContext, _info);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveWeatherInfo(Context pContext, WeatherInfo pWeatherInfo){
		
		SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);
		Editor _editor = _sharedPreferences.edit();
		_editor.putBoolean("city_selected", true);
		_editor.putString("city_name", pWeatherInfo.getCityName());
		_editor.putString("weather_code", pWeatherInfo.getWeatherCode());
		_editor.putString("temp1", pWeatherInfo.getTemp1());
		_editor.putString("temp2", pWeatherInfo.getTemp2());
		_editor.putString("weather_des", pWeatherInfo.getWeatherDes());
		_editor.putString("publish_time", pWeatherInfo.getPublishTime());
		_editor.putString("current_date", _dateFormat.format(new Date()));
		_editor.commit();
	}
}
