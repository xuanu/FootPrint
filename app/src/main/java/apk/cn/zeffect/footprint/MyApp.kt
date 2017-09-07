package apk.cn.zeffect.footprint

import android.app.Application
import android.content.Context
import apk.cn.zeffect.footprint.uitls.OrmUtils
import com.baidu.mapapi.SDKInitializer
import com.litesuits.orm.LiteOrm

/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/08/26
 *      desc    ：
 *      version:：1.0
 * </pre>
 * @author zzx
 */
class MyApp : Application() {


    companion object {
        val instance by lazy { MyApp() }
    }

    override fun onCreate() {
        super.onCreate()
    }


    fun init(context: Context) {
        SDKInitializer.initialize(context.applicationContext)
        OrmUtils.init(context.applicationContext)
    }


}