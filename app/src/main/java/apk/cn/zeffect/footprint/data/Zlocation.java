package apk.cn.zeffect.footprint.data;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.Table;

/**
 * <pre>
 *      author  ：zzx
 *      e-mail  ：zhengzhixuan18@gmail.com
 *      time    ：2017/09/05
 *      desc    ：
 *      version:：1.0
 * </pre>
 *
 * @author zzx
 *         // TODO 用@see描述一下当前类的方法及简单解释
 */
@Table("table_location")
public class Zlocation {
    public static final String COL_DATE_KEY = "date";

    @Column(COL_DATE_KEY)
    String date = "";//日期，按天数:2017-08-20
    String imei = "";//机器的IMEI号
    int id = 0;
    String time = "";//获取定位时间
    String loactionid = ""; //获取定位唯一ID，v7.2版本新增，用于排查定位问题
    Double lat = 0.0;//获取纬度信息
    Double lot = 0.0;//获取经度信息
    int locType = 0; //获取定位类型
    float radius = 0f;//获取定位精准度
    String addString = "";//获取地址信息
    String country = ""; //获取国家信息
    String countryCode = "";//获取国家码
    String city = "";//获取城市信息
    @Default("0")
    String cityCode = "";//获取城市码
    String district = "";//获取区县信息
    String street = "";//获取街道信息
    String streetNumber = "";  //获取街道码
    String locationDescribe = "";//获取当前位置描述信息
    String buildingId = "";//室内精准定位下，获取楼宇ID
    String buildingName = "";//室内精准定位下，获取楼宇名称
    String floor = "";//室内精准定位下，获取当前位置所处的楼层信息
    float speed = 0f;//获取当前速度，单位：公里每小时
    int satelliteNumber = 0;//获取当前卫星数
    Double altitude = 0.0;//获取海拔高度信息，单位米
    float direction = 0f;//获取方向信息，单位度
    int operators = 0;//获取运营商信息

    public String getDate() {
        return date;
    }

    public Zlocation setDate(String pDate) {
        date = pDate;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public Zlocation setImei(String pImei) {
        imei = pImei;
        return this;
    }

    public int getId() {
        return id;
    }

    public Zlocation setId(int pId) {
        id = pId;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Zlocation setTime(String pTime) {
        time = pTime;
        return this;
    }

    public String getLoactionid() {
        return loactionid;
    }

    public Zlocation setLoactionid(String pLoactionid) {
        loactionid = pLoactionid;
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public Zlocation setLat(Double pLat) {
        lat = pLat;
        return this;
    }

    public Double getLot() {
        return lot;
    }

    public Zlocation setLot(Double pLot) {
        lot = pLot;
        return this;
    }

    public int getLocType() {
        return locType;
    }

    public Zlocation setLocType(int pLocType) {
        locType = pLocType;
        return this;
    }

    public float getRadius() {
        return radius;
    }

    public Zlocation setRadius(float pRadius) {
        radius = pRadius;
        return this;
    }

    public String getAddString() {
        return addString;
    }

    public Zlocation setAddString(String pAddString) {
        addString = pAddString;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Zlocation setCountry(String pCountry) {
        country = pCountry;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Zlocation setCountryCode(String pCountryCode) {
        countryCode = pCountryCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Zlocation setCity(String pCity) {
        city = pCity;
        return this;
    }

    public String getCityCode() {
        return cityCode;
    }

    public Zlocation setCityCode(String pCityCode) {
        cityCode = pCityCode;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public Zlocation setDistrict(String pDistrict) {
        district = pDistrict;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public Zlocation setStreet(String pStreet) {
        street = pStreet;
        return this;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public Zlocation setStreetNumber(String pStreetNumber) {
        streetNumber = pStreetNumber;
        return this;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public Zlocation setLocationDescribe(String pLocationDescribe) {
        locationDescribe = pLocationDescribe;
        return this;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public Zlocation setBuildingId(String pBuildingId) {
        buildingId = pBuildingId;
        return this;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public Zlocation setBuildingName(String pBuildingName) {
        buildingName = pBuildingName;
        return this;
    }

    public String getFloor() {
        return floor;
    }

    public Zlocation setFloor(String pFloor) {
        floor = pFloor;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public Zlocation setSpeed(float pSpeed) {
        speed = pSpeed;
        return this;
    }

    public int getSatelliteNumber() {
        return satelliteNumber;
    }

    public Zlocation setSatelliteNumber(int pSatelliteNumber) {
        satelliteNumber = pSatelliteNumber;
        return this;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Zlocation setAltitude(Double pAltitude) {
        altitude = pAltitude;
        return this;
    }

    public float getDirection() {
        return direction;
    }

    public Zlocation setDirection(float pDirection) {
        direction = pDirection;
        return this;
    }

    public int getOperators() {
        return operators;
    }

    public Zlocation setOperators(int pOperators) {
        operators = pOperators;
        return this;
    }
}
