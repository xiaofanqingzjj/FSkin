# FSkin 

> 一个通过动态加载外部皮肤包的开源组件

## 项目介绍

一个使用简单、无侵入性的换肤组件


## 换肤效果

![换肤效果](./screenshot/screem.gif)



## 用法


#### 0. 在项目中添加依赖

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.xiaofanqingzjj:FSkin:Tag'
}
```


#### 1. 在`Application`中进行初始化皮肤组件

```kotlin
class YourApplication : Application {
    fun onCreate() {
        super.onCreate()
        SkinManager.getInstance().init(this)
    }
}
```


#### 2. 创建一个独立的module作为皮肤包

在皮肤包的module里放置要换肤的同名资源。把编译好的apk包放在sd卡下的特定目录下。


#### 3. 加载特定的皮肤

在你需要切换皮肤的地方，调用applySkin方法切换到对应的皮肤包，当然别忘了申请sd卡读写权限。

```kotlin
 SkinManager.applySkin("<Your skin apk path>", object : SkinManager.ILoaderListener {
        override fun onStart() {
        }

        override fun onSuccess() {
            Toast.makeText(context, "皮肤切换成功", Toast.LENGTH_SHORT).show()
        }

        override fun onFailed(reason: String?) {
            Toast.makeText(context, "皮肤切换失败$reason", Toast.LENGTH_SHORT).show()
        }
    })

```

皮肤组件会自动检测所有的xml布局文件中所有支持的换肤的属性，如果属性值是使用的引用资源且皮肤包里也有对应的资源，那么换肤组件会自动切换到皮肤组件里的资源。

#### 4.动态资源设置的换肤

相比与xml中的静态资源的引用，实际情况下更多的是使用代码动态的给界面设置资源，那么这种情况下，该如何使用换肤组件呢：

你只需要调用SkinManager的addSkinAttr方法来把activity、view、属性名以及资源id添加到皮肤组件即可。

``` kotlin
    // SkinManager.addSkinAttr(<actiview>, <View>, <attr name>, <resourceId>)
    SkinManager.addSkinAttr(this, view.iv_icon, "src", icon)
```

#### 5.可扩张的换肤属性

##### 目前换肤组件支持的换肤属性有：

* background

View的background属性

* src

ImageView的src属性

* textColor

TextView的TextColor属性

* textSize

TextView的TextSize属性

* drawableLeft、drawableTop、drawableRight、drawableBottom

TextView的drawableXXX相关的属性

* padding、paddingLeft、paddingTop、paddingRight、paddingBottom

View的padding相关属性

* margin、marginLeft、marginTop、marginRight、marginBottom

View LayoutMargin相关的属性


##### 扩展属性

对于目前支持的换肤属性来说可以满足大多数的换肤场景，对于不满足的场景，换肤组件提供了扩展换肤属性的功能。

你只需要扩展SkinElementAttr类，实现apply方法即刻，该方法会在皮肤切换的时候调用，在该方法里调用SkinManager的skinResourceProxy的资源加载方法来加载资源即可。

下面是一个自定义换肤属性的例子：

``` kotlin
/**
 * ImageView android:src
 */
class ImageSrcAttr : SkinElementAttr() {
    override fun apply(view: View?) {
        super.apply(view)
        (view as? ImageView)?.run {
        
            // 当皮肤变化的时候更新View的src属性
            setImageDrawable(SkinManager.skinResourcesProxy.getDrawable(attrValueRefId))
        }
    }
}
```

定义好扩展的Attr之后通过SkinManager的registerSkinAttr方法来注册支持的属性：

```kotlin
    // 注册ImageView的src换肤属性
    SkinManager.registerSkinAttr("src", ImageSrcAttr::class.java)
```




