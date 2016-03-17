package com.example.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	public static final String DB_NAME = "cool_weather";
	public static final int VERSION = 1;
	private SQLiteDatabase mDatabase ;
	private static CoolWeatherDB mCoolWeatherDB = null;
	
	private CoolWeatherDB(Context pContext){
		
		CoolWeatherOpenHelper _dbHelper = new CoolWeatherOpenHelper(pContext,DB_NAME,null,VERSION);
		mDatabase = _dbHelper.getWritableDatabase();
	}
	
	public synchronized static CoolWeatherDB getInstance(Context pContext){
		
		if(mCoolWeatherDB == null){
			mCoolWeatherDB = new CoolWeatherDB(pContext);
		}
		return mCoolWeatherDB;
	}
	
	public void saveProvince(Province pProvince){
		
		if(pProvince != null){
			ContentValues _values = new ContentValues();
			_values.put("province_name", pProvince.getName());
			_values.put("province_code", pProvince.getCode());
			mDatabase.insert("T_Province", null, _values);
		}
	}
	
	public List<Province> loadProvinces(){
		
		List<Province> _provinces = new ArrayList<Province>();
		Cursor _cursor = mDatabase.query("T_Province", null, null, null, null, null, null);
		while(_cursor.moveToNext()){
			Province _province = new Province();
			int _id = _cursor.getInt(_cursor.getColumnIndex("id"));
			_province.setId(_id);
			String _name = _cursor.getString(_cursor.getColumnIndex("province_name"));
			_province.setName(_name);
			String _code = _cursor.getString(_cursor.getColumnIndex("province_code"));
			_province.setCode(_code);
			_provinces.add(_province);
		}
		if(_cursor != null){
			_cursor.close();
		}
		return _provinces;
	}
	
	public void saveCity(City pCity){
		
		if(pCity != null){
			ContentValues _values = new ContentValues();
			_values.put("city_name", pCity.getName());
			_values.put("city_code", pCity.getCode());
			_values.put("province_id", pCity.getProvinceId());
			mDatabase.insert("T_City", null, _values);
		}
	}
	
	public List<City> loadCities(int pProvinceId){
		
		List<City> _cities = new ArrayList<City>();
		Cursor _cursor = mDatabase.query("T_City", null, "province_id = ?", 
				new String[]{String.valueOf(pProvinceId)}, null, null, null);
		while(_cursor.moveToNext()){
			City _city = new City();
			int _id = _cursor.getInt(_cursor.getColumnIndex("id"));
			_city.setId(_id);
			String _name = _cursor.getString(_cursor.getColumnIndex("city_name"));
			_city.setName(_name);
			String _code = _cursor.getString(_cursor.getColumnIndex("city_code"));
			_city.setCode(_code);
			_city.setProviceId(pProvinceId);
			_cities.add(_city);
		}
		return _cities;
	}
	
	public void saveCounty(County pCounty){
		
		if(pCounty != null){
			ContentValues _values = new ContentValues();
			_values.put("county_name", pCounty.getName());
			_values.put("county_code", pCounty.getCode());
			_values.put("city_id", pCounty.getCityId());
			mDatabase.insert("T_County", null, _values);
		}
	}
	
	public List<County> loadCounties(int pCityId){
		
		List<County> _counties = new ArrayList<County>();
		Cursor _cursor = mDatabase.query("T_County", null, "city_id = ?", 
				new String[]{String.valueOf(pCityId)}, null, null, null);
		while(_cursor.moveToNext()){
			County _county = new County();
			int _id = _cursor.getInt(_cursor.getColumnIndex("id"));
			_county.setId(_id);
			String _name = _cursor.getString(_cursor.getColumnIndex("county_name"));
			_county.setName(_name);
			String _code = _cursor.getString(_cursor.getColumnIndex("county_code"));
			_county.setCode(_code);
			_county.setCityId(pCityId);
			_counties.add(_county);
		}
		return _counties;
	}
}
