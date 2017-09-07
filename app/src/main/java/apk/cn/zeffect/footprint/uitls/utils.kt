package apk.cn.zeffect.footprint.uitls

import android.text.TextUtils
import android.util.Log
import apk.cn.zeffect.footprint.MyApp
import apk.cn.zeffect.footprint.data.Zlocation
import com.baidu.location.BDLocation
import java.text.SimpleDateFormat
import java.util.*
import com.baidu.mapapi.model.LatLng



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

object Utils {

    fun Any.log(message: String = "", tag: String = "log") {
        Log.e(tag, message)
    }

    /***
     * 保存定位信息
     */
    fun saveLocation(location: BDLocation) {
        if (location.locType == BDLocation.TypeServerError) return
//        if (!location.hasAddr()) return
//        if (TextUtils.isEmpty(location.addrStr)) return
        val zlt = Zlocation()
        zlt.imei = ""
        zlt.addString = location?.addrStr ?: ""
        zlt.altitude = location?.altitude ?: 0.0
        zlt.buildingId = location?.buildingID ?: ""
        zlt.buildingName = location?.buildingName ?: ""
        zlt.city = location?.city ?: ""
        zlt.cityCode = location?.cityCode ?: ""
        zlt.country = location?.country ?: ""
        zlt.countryCode = location?.countryCode ?: ""
        zlt.direction = location?.direction
        zlt.floor = location?.floor ?: ""
        zlt.lat = location?.latitude
        zlt.loactionid = location?.locationID ?: ""
        zlt.locType = location?.locType
        zlt.locationDescribe = location?.locationDescribe ?: ""
        zlt.lot = location?.longitude
        zlt.operators = location?.operators
        zlt.radius = location?.radius
        zlt.satelliteNumber = location?.satelliteNumber
        zlt.speed = location?.speed
        zlt.street = location?.street ?: ""
        zlt.streetNumber = location?.streetNumber ?: ""
        zlt.time = location?.time ?: ""
        zlt.date = getDate()
        OrmUtils.liteOrm?.save(zlt)
    }

    fun getDate(): String = SimpleDateFormat(Constant.DATEFORM).format(Date())

    fun getHour(): Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)


    /**
     * 计算两点之间距离
     * @param start
     * @param end
     * @return 米
     */
    fun getDistance(start: LatLng, end: LatLng): Double {
        val lat1 = Math.PI / 180 * start.latitude
        val lat2 = Math.PI / 180 * end.latitude
        val lon1 = Math.PI / 180 * start.longitude
        val lon2 = Math.PI / 180 * end.longitude
        //地球半径
        val R = 6371.0
        //两点间距离 km，如果想要米的话，结果*1000就可以了
        val d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R
        return d * 1000
    }
}