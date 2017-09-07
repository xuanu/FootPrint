package apk.cn.zeffect.footprint

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import apk.cn.zeffect.footprint.data.Zlocation
import apk.cn.zeffect.footprint.service.LocationService
import apk.cn.zeffect.footprint.ui.left.LeftView
import apk.cn.zeffect.footprint.uitls.Constant
import apk.cn.zeffect.footprint.uitls.OrmUtils
import apk.cn.zeffect.footprint.uitls.Utils
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.map.PolylineOptions
import com.baidu.mapapi.model.LatLng
import com.litesuits.orm.db.assit.QueryBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startService
import java.util.*
import android.support.v7.app.AppCompatDelegate


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val mLeftView: LeftView by lazy { LeftView(this, find(R.id.left)) }
    val mDraw by lazy { find<DrawerLayout>(R.id.drawerLayout) }
    override fun onClick(p0: View?) {
        when (p0?.id) {
        }
    }

    private val mContext: Context by lazy { this }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initApp()
        setContentView(R.layout.activity_main)
        initView()
        //
        startService<LocationService>()
    }

    private fun initApp() {
        MyApp.instance.init(mContext)
    }

    private fun initView() {
        mLeftView.initView()
        mapView.map.setMaxAndMinZoomLevel(19f, 12f)
        initLocation()
        //
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(Constant.ACTION_LOCATION_CHANGE))
    }

    /**初始化最后的位置**/
    fun initLocation() {
        val listLocation = OrmUtils.liteOrm?.query(Zlocation::class.java)
        if (listLocation?.isNotEmpty() ?: false) {
            val tempZlocation = listLocation?.get(listLocation.size - 1)
            val bdLocation = BDLocation()
            bdLocation.latitude = tempZlocation?.lat ?: 0.0
            bdLocation.longitude = tempZlocation?.lot ?: 0.0
            bdLocation.radius = tempZlocation?.radius ?: 0f
            updateLocation(bdLocation, mapView.mapLevel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        mLeftView.onDestory()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onPause() {
        super.onPause()
        mLeftView.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    /***
     * 位置变化广播
     */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                Constant.ACTION_LOCATION_CHANGE -> {
                    val bdLocation = p1.getParcelableExtra<BDLocation>(Constant.BDLOCATION_KEY)
                    updateLocation(bdLocation, mapView.mapLevel)
                    drawLine()
                }
            }
        }
    }


    private fun updateLocation(bdLocation: BDLocation, scale: Int) {
        mapView.map.isMyLocationEnabled = true
        val locData = MyLocationData.Builder()
                .accuracy(bdLocation.radius)
                .direction(bdLocation.direction).latitude(bdLocation.latitude)
                .longitude(bdLocation.longitude).build()
        mapView.map.setMyLocationData(locData)
        mapView.map.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(LatLng(bdLocation.latitude, bdLocation.longitude), scale.toFloat()))
    }

    fun drawLine() {
        mapView.map.clear()
        val localList = OrmUtils.liteOrm?.query(QueryBuilder(Zlocation::class.java).whereEquals(Zlocation.COL_DATE_KEY, Utils.getDate())) ?: Collections.emptyList<Zlocation>()
        var points = arrayListOf<LatLng>()
        localList.forEach {
            //            if (!TextUtils.isEmpty(it.addString))
            points.add(LatLng(it.lat, it.lot))
        }
        if (points.size < 2) return
        mapView.map.addOverlay(PolylineOptions().color(Color.RED).points(points))
    }

}
