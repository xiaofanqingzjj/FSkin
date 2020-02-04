package com.tencent.fskin.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.tencent.fskin.SkinInflaterFactory;
import com.tencent.fskin.SkinManager;
import com.tencent.fskin.SkinResources;

import java.util.List;

import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.listener.IDynamicNewView;
import cn.feng.skin.manager.listener.ISkinUpdate;

/**
 * Base Activity for development
 * 
 * <p>NOTICE:<br> 
 * You should extends from this if you what to do skin change
 * 
 * @author fengjun
 */
public class BaseActivity extends Activity {
	
	/**
//	 * Whether response to skin changing after create
//	 */
//	private boolean isResponseOnSkinChanging = true;
//
//	private SkinInflaterFactory mSkinInflaterFactory;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mSkinInflaterFactory = new SkinInflaterFactory();
//		getLayoutInflater().setFactory(mSkinInflaterFactory);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		SkinManager.INSTANCE.attach(this);
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		SkinManager.INSTANCE.detach(this);
//		mSkinInflaterFactory.clean();
//	}
//
////	/**
////	 * dynamic add a skin view
////	 *
////	 * @param view
////	 * @param attrName
////	 * @param attrValueResId
////	 */
////	protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId){
////		mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
////	}
////
////	protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs){
////		mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
////	}
////
////	final protected void enableResponseOnSkinChanging(boolean enable){
////		isResponseOnSkinChanging = enable;
////	}
//
//	@Override
//	public void onThemeUpdate() {
//		if(!isResponseOnSkinChanging){
//			return;
//		}
//		mSkinInflaterFactory.applySkin();
//	}
//
//	@Override
//	public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
////		mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
//	}

//	@Override
//	public Resources getResources() {
//		Resources org = super.getResources();
//		SkinResources skinResourcesWrapper = new SkinResources(org);
//		return skinResourcesWrapper;
//
//	}
}
