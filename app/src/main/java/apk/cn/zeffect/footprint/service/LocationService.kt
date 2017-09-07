package apk.cn.zeffect.footprint.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import apk.cn.zeffect.footprint.uitls.Constant
import apk.cn.zeffect.footprint.uitls.Utils
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.model.LatLng
import org.jetbrains.anko.doAsync


/**
 * 定位类
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/08/26
 *      desc    ：
 *      version:：1.0
 * </pre>
 * @author zzx
 */
class LocationService : Service() {
    private val INTERVAL_TIME: Int = 60 * 1000
    private var mLastLat: Double = 0.0
    private var mLastLot: Double = 0.0
    private var mStartTime: Long = 0
    private var mLocationClient: LocationClient? = null
    override fun onCreate() {
        super.onCreate()
        init()
    }


    fun init() {
        mLocationClient = LocationClient(this.applicationContext)
        mLocationClient?.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(pLocation: BDLocation) {//处理定位结果
                tactics(pLocation)
            }
        })
        mLocationClient?.locOption = buildOptions(LocationClientOption.LocationMode.Battery_Saving)
        mLocationClient?.start()
    }

    /**
     * 选择策略,重新执行。
     * **/
    private fun tactics(pLocation: BDLocation) {
        if (pLocation.locType == BDLocation.TypeServerDecryptError) return
        if (pLocation.locType == BDLocation.TypeServerError) return
        val nowTime: Long = System.currentTimeMillis()
        if (nowTime - mStartTime < INTERVAL_TIME) return
        mStartTime = nowTime
        if (mLastLat == pLocation.latitude && mLastLot == pLocation.longitude) return//如果最后位置相同，不记录。
        val distance = Utils.getDistance(LatLng(mLastLat, mLastLot), LatLng(pLocation.latitude, pLocation.longitude))
        mLastLat = pLocation.latitude
        mLastLot = pLocation.longitude
        LocalBroadcastManager.getInstance(this@LocationService).sendBroadcast(Intent(Constant.ACTION_LOCATION_CHANGE).putExtra(Constant.BDLOCATION_KEY, pLocation))
        doAsync {
            Utils.saveLocation(pLocation)
            //策略更换
//            when {
//                Utils.getHour() >= 22 -> {//如果大于晚上22点，启动仅设备定位
//                    restartLocation(LocationClientOption.LocationMode.Device_Sensors)
//                }
//                distance > 5 * 1000 -> {//一分钟这内，行走了5000米，这应该是不是人行走的速度了
//                    restartLocation(LocationClientOption.LocationMode.Battery_Saving)
//                }
//                else -> restartLocation(LocationClientOption.LocationMode.Battery_Saving)
//            }
        }
    }

    /**
     * 重启定位
     * **/
    private fun restartLocation(pModel: LocationClientOption.LocationMode) {
        if (mLocationClient?.locOption?.locationMode == pModel) return
        mLocationClient?.stop();mLocationClient?.locOption = buildOptions(pModel);mLocationClient?.start(); }


    /**设置选项
     * @param pModel 定位精度
     * **/
    private fun buildOptions(pModel: LocationClientOption.LocationMode): LocationClientOption {
        val option = LocationClientOption()
        option.locationMode = pModel//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll")//可选，默认gcj02，设置返回的定位结果坐标系1
        option.setScanSpan(INTERVAL_TIME)//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true)//可选，设置是否需要地址信息，默认不需要
        option.isOpenGps = true//可选，默认false,设置是否使用gps
        option.isLocationNotify = false//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true)//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false)//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false)//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false)//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        return option
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}