package apk.cn.zeffect.footprint.uitls

import android.content.Context
import android.os.Environment
import com.litesuits.orm.LiteOrm
import java.io.File

/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/08/28
 *      desc    ：
 *      version:：1.0
 * </pre>
 * @author zzx
 */
class OrmUtils private constructor() {


    companion object {
        /**本地存放地址的路径，防止卸载应用后被删除**/
        private val DB_PATH = "${Environment.getExternalStorageDirectory()}${File.separator}footprint${File.separator}db${File.separator}location.db"
        val instance = OrmUtils()
        var liteOrm: LiteOrm? = null
        fun init(context: Context) {
            val file = File(DB_PATH)
            if (!file.exists()) file.parentFile.mkdirs()
            liteOrm = LiteOrm.newSingleInstance(context.applicationContext, DB_PATH)
        }
    }
}
