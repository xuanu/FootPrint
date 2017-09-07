package apk.cn.zeffect.footprint.ui.left

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import apk.cn.zeffect.footprint.MainActivity
import apk.cn.zeffect.footprint.R
import apk.cn.zeffect.footprint.data.Zlocation
import apk.cn.zeffect.footprint.uitls.Constant
import apk.cn.zeffect.footprint.uitls.OrmUtils
import apk.cn.zeffect.footprint.uitls.PreferencesUtils
import com.baidu.mapapi.map.offline.MKOLUpdateElement
import com.baidu.mapapi.map.offline.MKOfflineMap
import com.baidu.mapapi.map.offline.MKOfflineMapListener
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import okhttp3.Response
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONObject
import java.lang.Exception
import java.util.*


/**
 * https://way.jd.com/jisuapi/weather?city=%E6%B7%B1%E5%9C%B3%E5%B8%82&cityid=111&citycode=101260301&appkey=98db16b11c40844cde7942c116edc194
 * 天气查询
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/09/05
 *      desc    ：
 *      version:：1.0
 * </pre>
 * @author zzx
 */
class LeftView(activity: MainActivity, view: View) : MKOfflineMapListener {
    override fun onGetOfflineMapState(type: Int, state: Int) {
        when (type) {
            MKOfflineMap.TYPE_DOWNLOAD_UPDATE -> {
                val update = mOffLine?.getUpdateInfo(state)
                // 处理下载进度更新提示
                val progress = update?.ratio ?: 0
                if (progress > 100) mOfflinMapProgress?.visibility = View.INVISIBLE
                mOfflinMapProgress?.progress = Math.ceil(progress.toDouble() ?: 0.0).toInt()
            }
            MKOfflineMap.TYPE_NEW_OFFLINE -> {
                val update = mOffLine?.getUpdateInfo(state)
                if (mZlocation?.cityCode?.toInt() == update?.cityID ?: 0)
                    mOffLineMapDown.text = "更新离线${mZlocation?.city}地图"
            }
            MKOfflineMap.TYPE_VER_UPDATE -> {
            }
            else -> {
            }
        }
    }

    private val mView by lazy { view }
    private val mActivity by lazy { activity }
    private var mRefreshLayout: SmartRefreshLayout? = null
    private var mOfflinMapProgress: ProgressBar? = null
    private var mOffLine: MKOfflineMap? = null
    private var mZlocation: Zlocation? = null
    private val mOffLineMapDown by lazy { mView.find<TextView>(R.id.offlin_map_down) }
    private val mCityName by lazy { mView.find<TextView>(R.id.cityName) }
    private val mWeatherStatus by lazy { mView.find<TextView>(R.id.weather_status) }
    private val mAirStatus by lazy { mView.find<TextView>(R.id.air_status) }
    private val mTemperature by lazy { mView.find<TextView>(R.id.temperature) }
    private val minMaxTemperature by lazy { mView.find<TextView>(R.id.minmaxtemperature) }

    init {
        initCity()
        initOffLine()
        initWeather()
        //
        mOffLineMapDown.onClick { mOfflinMapProgress?.visibility = View.VISIBLE;mOffLine?.start(mZlocation?.cityCode?.toInt() ?: 0); }
    }


    fun initView() {
        mRefreshLayout = mView.find<SmartRefreshLayout>(R.id.left)
        mOfflinMapProgress = mView.find(R.id.offlin_map_progress)
        //
        val header = MaterialHeader(mActivity)
        mRefreshLayout?.setRefreshHeader(header)
        //
        mRefreshLayout?.setOnRefreshListener { getWeather() }
        mView.find<TextView>(R.id.gotoLast).onClick { mActivity.initLocation();mActivity.mDraw.closeDrawer(Gravity.LEFT); }//定位到最后点
        mView.find<TextView>(R.id.drawLine).onClick { mActivity.drawLine(); mActivity.mDraw.closeDrawer(Gravity.LEFT); }//画线
    }

    private fun initCity() {
        val list = OrmUtils.liteOrm?.query(Zlocation::class.java)
        if (list?.isEmpty() != false) return
        mZlocation = list?.get(list.size - 1) ?: Zlocation()

    }

    private fun initOffLine() {
        if (mOffLine == null) mOffLine = MKOfflineMap()
        mOffLine?.init(this)
        val tempMap = mOffLine?.getUpdateInfo(mZlocation?.cityCode?.toInt() ?: 0)
        when {
            tempMap?.status == MKOLUpdateElement.FINISHED -> {
                mOffLineMapDown.isEnabled = false;mOffLineMapDown.text = "已离线${mZlocation?.city}地图"
            }
            tempMap?.status == MKOLUpdateElement.WAITING || tempMap?.status == MKOLUpdateElement.SUSPENDED -> {
                mOffLineMapDown.isEnabled = true;mOffLineMapDown.text = "已暂停离线${mZlocation?.city}地图"
            }
            else -> {
                mOffLineMapDown.isEnabled = true
                mOffLineMapDown.text = "离线${mZlocation?.city}地图"
            }
        }
    }

    private val SAVE_GET_WEATHER_TIME = "get_weather_time"

    /***
     * 一天只执行一次
     * 主动执行一次，请求天气数据
     * */
    private fun initWeather() {
        val lastTime = PreferencesUtils.getLong(mActivity, SAVE_GET_WEATHER_TIME, 0)
        if (System.currentTimeMillis() - lastTime > 24 * 60 * 60 * 1000) getWeather()
    }


    private fun getWeather() {
        if (TextUtils.isEmpty(mZlocation?.city)) {
            initCity();return
        }
        OkHttpUtils.get().url("https://way.jd.com/jisuapi/weather?city=${mZlocation?.city}&appkey=98db16b11c40844cde7942c116edc194").build().execute(object : StringCallback() {
            override fun onError(call: Call?, e: Exception?, id: Int) {
                Toast.makeText(mActivity, "更新天气失败", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(response: String?, id: Int) {
                PreferencesUtils.putLong(mActivity, SAVE_GET_WEATHER_TIME, System.currentTimeMillis())
                anyWeatherResponse(response ?: "")
            }

            override fun onAfter(id: Int) {
                mRefreshLayout?.finishRefresh()
            }
        })
    }

    private fun anyWeatherResponse(response: String) {
        if (TextUtils.isEmpty(response)) return
        val responseJson = JSONObject(response)
        val code = responseJson.getInt(Constant.CODE_KEY)
        if (code != 10000) return
        val resultJson = responseJson.getJSONObject(Constant.RESULT_KEY)
        val status = resultJson.getInt(Constant.STATUS_KEY)
        if (status != 0) return
        val tResultJson = resultJson.getJSONObject(Constant.RESULT_KEY)
        val temp = tResultJson.getString(Constant.TEMP_KEY)
        val weather = tResultJson.getString(Constant.WEATHER_KEY)
        val tempLow = tResultJson.getString(Constant.TEMP_LOW_KEY)
        val tempHight = tResultJson.getString(Constant.TEMP_HIGH_KEY)
        val cityName = tResultJson.getString(Constant.CITY_KEY)
        val winddirect = tResultJson.getString(Constant.WINDDIRECT_KEY)
        //
        mAirStatus.text = winddirect
        mCityName.text = cityName
        minMaxTemperature.text = "$tempLow°/$tempHight°"
        mWeatherStatus.text = weather
        mTemperature.text = ("$temp°")
    }


    fun onPause() {
        mOffLine?.pause(mZlocation?.cityCode?.toInt() ?: 0)
    }

    fun onDestory() {
        mOffLine?.destroy()
    }

}