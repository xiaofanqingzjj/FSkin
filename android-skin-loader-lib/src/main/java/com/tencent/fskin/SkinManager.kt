package com.tencent.fskin

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import com.tencent.fskin.util.PreferencesUtils
import com.tencent.fskin.util.async
import java.io.File


/**
 * 皮肤管理器
 *
 */
object SkinManager {

    private const val TAG = "SkinManager"

    private lateinit var mApplication: Application

    /**
     * 皮肤包的Resource
     */
    private var mSkinResources: Resources? = null

    /**
     * 通用的资源加载器
     */
    lateinit var resources: SkinResources

    internal var skinPackageName: String? = null

    private val mSkins: MutableMap<Activity, ActivitySkinChange> = mutableMapOf()

    /**
     * 初始化皮肤管理
     */
    fun init(context: Application) {
        mApplication = context

        // 加载资源的resource，有皮肤从皮肤中记载，没有默认的资源包加载
        resources = SkinResources(context.resources)

        // 把activity中所有的View收集起来
        addActivityCallback()

        // 如果设置了皮肤，加载设置的皮肤
        async {
            val skinPath = currentSkinPath()
            if (!skinPath.isNullOrEmpty()) {
                applySkin(skinPath)
            }
        }
    }


    /**
     * 该对象用来收集Activity里带有支持换肤属性的组件
     */
    private class ActivitySkinChange(activity: Activity) : OnSkinChange {
        val mSkinInflaterFactory = SkinInflaterFactory()

        init {
            // 给ActivityLayoutInflater设置一个Factory来拦截所有的View创建
            activity.layoutInflater.factory = mSkinInflaterFactory
        }

        override fun onChange() {
            mSkinInflaterFactory.applySkin()
        }
    }


    private fun addActivityCallback() {

        mApplication.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                mSkins[activity] = ActivitySkinChange(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                mSkins[activity]?.mSkinInflaterFactory?.clean()
                mSkins.remove(activity)
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }



            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

        })
    }

    /**
     *
     */
    internal fun getCurrentSkinResource(): Resources? {
        return this.mSkinResources
    }



    /**
     * 加载一个皮肤包
     *
     * @return 加载成功Resources返回的是皮肤包的Resources对象，String为失败原因
     */
    private fun loadSkin(skinPkgPath: String): Pair<Resources?, String?> {
        val context = mApplication

        val file = File(skinPkgPath)

        if (!file.exists()) {
            return Pair(null, "Skin file is not exist")
        }

        val pm = context.packageManager

        return try {
            val skinPackageInfo = pm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES)

            skinPackageName = skinPackageInfo.packageName

            val skinAssetManager = AssetManager::class.java.newInstance()
            val addAssetPathMethod = skinAssetManager.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPathMethod.invoke(skinAssetManager, skinPkgPath)

            val resources: Resources = context.resources
            val skinResource = Resources(skinAssetManager, resources.displayMetrics, resources.configuration)

            Pair(skinResource, null)
        } catch (e: java.lang.Exception) {
            Pair(null, e.message)
        }
    }


    /**
     * 应用皮肤
     */
    fun applySkin(skinPkgPath: String, changeCallback: ILoaderListener? = null) {

        Log.d(TAG, "applySkin:$skinPkgPath")

        async (
                preExecute = {
                    changeCallback?.onStart()
                },
                doBackground = {
                    loadSkin(skinPkgPath)
                },
                postExecute =  {
                    val result = it!!

                    Log.d(TAG, "applySkinComplete:$skinPkgPath, result:$it")

                    if (result.first != null) { // 皮肤切换成功

                        // 保存皮肤路径
                        saveCurrentSkin(skinPkgPath)

                        // 加载皮肤包的Resource对象
                        mSkinResources = result.first

                        // 通知已经创建了界面换肤
                        notifySkinChange()

                        // 皮肤切换成功回调
                        changeCallback?.onSuccess()
                    } else { // 皮肤切换失败
                        changeCallback?.onFailed(result.second)
                    }
                })
    }

    private fun saveCurrentSkin(skinPkgPath: String) {
        val context = mApplication ?: return
        PreferencesUtils.putString(context, "skinPath", skinPkgPath)
    }

    /**
     * 当前应用的皮肤包
     */
    private fun currentSkinPath(): String? {
        val context = mApplication ?: return null
        return PreferencesUtils.getString(context, "skinPath")
    }

    private fun notifySkinChange() {
        mSkins.forEach {
            it.value.onChange()
        }
    }


    fun restoreDefaultTheme() {
        mSkinResources = null
        PreferencesUtils.putString(mApplication, "skinPath", "")
        notifySkinChange()
    }


    interface ILoaderListener {
        fun onStart()
        fun onSuccess()
        fun onFailed(reason: String?)
    }

}