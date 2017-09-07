package apk.cn.zeffect.footprint.ui.splash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import apk.cn.zeffect.footprint.MainActivity
import apk.cn.zeffect.footprint.R
import com.anthonycr.grant.PermissionsManager
import com.anthonycr.grant.PermissionsResultAction

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
class SplashActivity : Activity() {
    private val mContext by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkPermission()
    }

    private fun checkPermission() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION), object : PermissionsResultAction() {
            override fun onDenied(permission: String?) {

            }

            override fun onGranted() {
                startActivity(Intent(mContext, MainActivity::class.java))
                mContext.finish()
            }
        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }


}