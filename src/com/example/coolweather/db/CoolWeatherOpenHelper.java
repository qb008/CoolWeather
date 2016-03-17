package com.example.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	public static final String CREATE_PROVINCE = "create table T_Province (id integer primary key " +
			"autoincrement,province_name text not null,province_code text not null)";
	public static final String CREATE_CITY = "create table T_City (id integer primary key autoincrement," +
			"city_name text not null,city_code text not null,province_id integer not null)";
	public static final String CREATE_COUNTY = "create table T_County (id integer primary key autoincrement," +
			"county_name text not null,county_code text not null,city_id integer not null)";
	
	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
