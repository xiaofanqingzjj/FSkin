package com.tencent.fskin.demo;

import android.app.Application;

import com.tencent.fskin.SkinManager;


//import com.tencent.fskin.SkinManager;

public class SkinApplication extends Application {
	
	public void onCreate() {
		super.onCreate();
		
		initSkinLoader();
	}

	/**
	 * Must call init first
	 */
	private void initSkinLoader() {
//		SkinManager.getInstance().init(this);
//		SkinManager.getInstance().load();

		SkinManager.INSTANCE.init(this);
	}
}