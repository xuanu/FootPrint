package apk.cn.zeffect.footprint.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import apk.cn.zeffect.footprint.service.LocationService

/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/09/06
 *      desc    ：
 *      version:：1.0
 * </pre>
 * @author zzx
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.startService(Intent(p0, LocationService::class.java))
    }

}
