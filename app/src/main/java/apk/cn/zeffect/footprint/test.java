package apk.cn.zeffect.footprint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.litesuits.orm.db.assit.QueryBuilder;

import apk.cn.zeffect.footprint.data.Zlocation;
import apk.cn.zeffect.footprint.uitls.OrmUtils;

/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/08/28
 *      desc    ：
 *      version:：1.0
 * </pre>
 *
 * @author zzx
 *         // TODO 用@see描述一下当前类的方法及简单解释
 */

public class test {
    public  static void init(Context pContext) {
        OrmUtils.Companion.getLiteOrm().query(new QueryBuilder<>(Zlocation.class));
    }
}
