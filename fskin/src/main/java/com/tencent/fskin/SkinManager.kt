package com.tencent.fskin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import com.tencent.fskin.util.PreferencesUtils
import com.tencent.fskin.util.async
import com.tencent.fskin.util.runUIThread
import java.io.File


/**
 * 皮肤管理器
 *
 *
 *
 *
 * @author fortunexiao
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
    lateinit var skinResourcesProxy: SkinResourcesProxy

    internal var skinPackageName: String? = null

    /**
     *
     */
    private val mSkins: MutableMap<Activity, ActivitySkinChange> = mutableMapOf()

    /**
     * 防止快速切换
     */
    private var isApplying = false

    private var currentSkinPath: String? = null

    /**
     * 当前皮肤组件是否进行了初始化
     */
    private val isInit: Boolean
        get() {
            return this::mApplication.isInitialized
        }

    /**
     *
     * 皮肤管理器的入口
     *
     * 初始化皮肤管理
     */
    fun init(context: Application) {
        mApplication = context

        // 加载资源的resource，有皮肤从皮肤中记载，没有默认的资源包加载
        skinResourcesProxy = SkinResourcesProxy(context.resources)

        // 把activity中所有的View收集起来
        addActivityCallback()

        // 如果用户设置了皮肤，则应用用户设置的皮肤
        applySkin()
    }

    private fun applySkin() {
        // 如果设置了皮肤，加载设置的皮肤
        async {
            val skinPath = currentSkinPath()
            Log.d(TAG, "applyInitSkin:$skinPath")
            if (!skinPath.isNullOrEmpty()) {
                applySkin(skinPath)
            }
        }
    }

    /**
     * 扩展自己的皮肤属性
     */
    fun registerSkinAttr(attrName: String, attr: Class<out SkinElementAttr>) {
        SkinElementAttrFactory.registerSkinAttr(attrName, attr)
    }


    /**
     * 当元素的属性值是通过代码设置的时候，需要手动把要换肤的元素和属性添加到皮肤框架中
     *
     * @param activity 换肤的界面
     * @param view 换肤的View对象
     * @param attrName View的xml属性名，要通过这个属性名来获取对应的SkinElementAttr对象的真实类型
     * @param value 资源id
     */
    fun addSkinAttr(activity: Activity, view: View, attrName: String, value: Int) {

        // 如果不支持，则直接退出
        if (!SkinElementAttrFactory.isSupportedAttr(attrName)) return

        //
        val activitySkinChange = mSkins[activity] ?: return

        val inflaterFactory = activitySkinChange.mSkinInflaterFactory


        val skinItems = inflaterFactory.mSkinItems

        if (!skinItems.keys.contains(view)) {
            skinItems[view] = SkinElement(view)
        }

        val attrs = skinItems[view]?.attrs

        val entryName = activity.resources.getResourceEntryName(value)
        val typeName = activity.resources.getResourceTypeName(value)

        val skinElementAttr = SkinElementAttrFactory.createSkinAttr(attrName, value, entryName, typeName) ?: return

        attrs?.add(skinElementAttr)

        // 初始加进来的时候需要重新设置一下
        skinElementAttr.initApply(view, skinResourcesProxy)
    }

//    fun addSkinAttr(activity: Activity, view: View, skinElementAttr: SkinElementAttr, value: Int) {
//
//        val activitySkinChange = mSkins[activity] ?: return
//
//        val inflaterFactory = activitySkinChange.mSkinInflaterFactory
//
//        val skinItems = inflaterFactory.mSkinItems
//
//        if (!skinItems.keys.contains(view)) {
//            skinItems[view] = SkinElement(view)
//        }
//
//        val attrs = skinItems[view]?.attrs
//
//        val entryName = activity.resources.getResourceEntryName(value)
//        val typeName = activity.resources.getResourceTypeName(value)
//
//        val skinElementAttr = SkinElementAttrFactory.createSkinAttr(attrName, value, entryName, typeName) ?: return
//
//        attrs?.add(skinElementAttr)
//
//        // 初始加进来的时候需要重新设置一下
//        skinElementAttr.initApply(view)
//    }


    /**
     * 该对象用来收集Activity里带有支持换肤属性的组件
     */
    private class ActivitySkinChange(activity: Activity) : SkinAble {
        val mSkinInflaterFactory = SkinInflaterFactory()

        init {

//            activity?.window?.setBackgroundDrawable()

            // 给ActivityLayoutInflater设置一个Factory来拦截所有的View创建
            activity.layoutInflater.factory = mSkinInflaterFactory
        }

        override fun onChange() {
            mSkinInflaterFactory.applySkin()
        }
    }


    private fun addActivityCallback() {

        // 注册所有的Activity的生命周期监听器
        //
        mApplication.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

                //
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
            skinPackageName = skinPackageInfo?.packageName

//            val skinAssetManager = AssetManager::class.java.newInstance()
//            val addAssetPathMethod = skinAssetManager.javaClass.getMethod("addAssetPath", String::class.java)
//            addAssetPathMethod.invoke(skinAssetManager, skinPkgPath)
//
//            val resources: Resources = context.resources
//            val skinResource = Resources(skinAssetManager, resources.displayMetrics, resources.configuration)

            val skinResource = getResourceFromSkinPath(context, skinPkgPath)

            Pair(skinResource, null)
        } catch (e: java.lang.Exception) {

            // 皮肤包加载失败
            Pair(null, e.message)
        }
    }


//    private class

    /**
     * 根据skinPath创建Resource对象
     */
    private fun getResourceFromSkinPath(context: Context, skinPkgPath: String): Resources? {
//        return try {
//            val skinAssetManager = AssetManager::class.java.newInstance()
//            val addAssetPathMethod = skinAssetManager.javaClass.getMethod("addAssetPath", String::class.java)
//            addAssetPathMethod.invoke(skinAssetManager, skinPkgPath)
//
//            val resources: Resources = context.resources
//            Resources(skinAssetManager, resources.displayMetrics, resources.configuration)
//        } catch (e: java.lang.Exception) {
//            null
//        }

        return try {
            //
            val packageInfo: PackageInfo = context.packageManager.getPackageArchiveInfo(skinPkgPath, 0) ?: return null
            packageInfo.applicationInfo.sourceDir = skinPkgPath
            packageInfo.applicationInfo.publicSourceDir = skinPkgPath

            val res: Resources = context.packageManager.getResourcesForApplication(packageInfo.applicationInfo)

            val superRes: Resources = context.resources

            return Resources(res.assets, superRes.displayMetrics, superRes.configuration)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /**
     * 获取皮肤包资源[Resources].
     *
     * @param skinPkgPath sdcard中皮肤包路径.
     * @return
     */
    fun getSkinResources(context: Context, skinPkgPath: String): Resources? {
        try {
            val packageInfo: PackageInfo = context.packageManager.getPackageArchiveInfo(skinPkgPath, 0) ?: return null
            packageInfo.applicationInfo.sourceDir = skinPkgPath
            packageInfo.applicationInfo.publicSourceDir = skinPkgPath

            val res: Resources = context.packageManager.getResourcesForApplication(packageInfo.applicationInfo)

            val superRes: Resources = context.resources
            return Resources(res.assets, superRes.displayMetrics, superRes.configuration)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }



    /**
     * 当前的皮肤是否是默认皮肤
     */
    internal fun isDefaultSkin(): Boolean {
        return mSkinResources == null
    }

    /**
     * 应用皮肤
     *
     * @param skinPkgPath 外部皮肤包的路径，如果为null表示恢复成默认皮肤
     * @param changeCallback 回调
     */
    fun applySkin(skinPkgPath: String?, changeCallback: ILoaderListener? = null) {

        // 未初始化皮肤组件
        if (!isInit) {
            throw RuntimeException("You should init SkinManager by call init() method.")
        }

        // 防止快速切换
        if (isApplying) return

        Log.d(TAG, "applySkin:$skinPkgPath, current:$currentSkinPath")

        // 防止无效切换
        if ((isDefaultSkin() && skinPkgPath.isNullOrEmpty()) || skinPkgPath == currentSkinPath) {
            return
        }

        isApplying = true


        if (skinPkgPath.isNullOrEmpty()) {

            changeCallback?.onStart()

            restoreDefaultTheme()


            runUIThread {
                changeCallback?.onSuccess()
                isApplying = false
            }
        } else {
            Log.d(TAG, "applySkin:$skinPkgPath")
            async(
                    preExecute = {
                        changeCallback?.onStart()
                    },
                    doBackground = {
                        loadSkin(skinPkgPath!!)
                    },
                    postExecute = {
                        val result = it!!

                        Log.d(TAG, "applySkinComplete:$skinPkgPath, result:$it")
                        if (result.first != null) { // 皮肤切换成功
                            // 保存皮肤路径
                            saveCurrentSkin(skinPkgPath!!)

                            // 加载皮肤包的Resource对象
                            mSkinResources = result.first

                            // 通知已经创建了界面换肤
                            notifySkinChange()

                            // 皮肤切换成功回调
                            changeCallback?.onSuccess()
                        } else { // 皮肤切换失败
                            changeCallback?.onFailed(result.second)
                        }

                        isApplying = false
                    })
        }
    }

    fun restoreDefaultTheme() {

        if (mSkinResources == null) return
        
        mSkinResources = null
        saveCurrentSkin("")
        notifySkinChange()
    }


    private fun saveCurrentSkin(skinPkgPath: String) {
        currentSkinPath = skinPkgPath
        val context = mApplication
        PreferencesUtils.putString(context, "skinPath", skinPkgPath)
    }

    /**
     * 当前应用的皮肤包
     */
    fun currentSkinPath(): String? {
        val context = mApplication
        return PreferencesUtils.getString(context, "skinPath")
    }

    private fun notifySkinChange() {
        mSkins.forEach {
            it.value.onChange()
        }
    }



    interface ILoaderListener {
        fun onStart()
        fun onSuccess()
        fun onFailed(reason: String?)
    }

}