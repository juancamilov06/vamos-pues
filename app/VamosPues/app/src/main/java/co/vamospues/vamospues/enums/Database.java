package co.vamospues.vamospues.enums;

/**
 * Created by Manuela Duque M on 06/02/2017.
 */

public class Database {

    public static final String DB_NAME = "vamospues_db";
    public static final int DB_VERSION = 1;

    public static final String KEY_MUSIC_ID = "music_id";
    public static final String KEY_ZONE_ID = "zone_id";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_IS_DISCO = "is_disco";
    public static final String KEY_MIN_BUDGET = "min_budget";
    public static final String KEY_LIMIT_HOUR = "limit_hour";
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_OPEN = "open";
    public static final String KEY_CLOSE = "close";
    public static final String KEY_CREATED = "created";
    public static final String KEY_UPDATED = "updated";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_MAIL = "mail";
    public static final String KEY_IS_ACTIVE = "is_active";
    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_TOKEN = "auth_token";
    public static final String KEY_EXPIRE_TIME = "expire_time";
    public static final String KEY_PLACE_ITEM_ID = "place_item_id";
    public static final String KEY_ITEM_ORDER_ID = "item_order_id";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_TOTAL = "total";
    public static final String KEY_IS_REDEEMED = "is_redeemed";

    public static final String TABLE_PLACE = "place";
    public static final String TABLE_MUSIC = "music";
    public static final String TABLE_ZONE = "zone";
    public static final String TABLE_ITEM_TYPES = "item_types";
    public static final String TABLE_URL = "url";
    public static final String TABLE_SESSION = "session";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_ITEM_ORDER = "item_order";
    public static final String TABLE_ITEM_ORDER_ITEM = "item_order_item";

    public static final String CREATE_ITEM_ORDER = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_ORDER + "("
            + KEY_ID + " TEXT NOT NULL, " + KEY_USER_ID + " INTEGER NOT NULL, " + KEY_DATE + " TEXT NOT NULL, "
            + KEY_TOTAL + " DOUBLE NOT NULL, " + KEY_IS_REDEEMED + " BOOLEAN NOT NULL, PRIMARY KEY("
            + KEY_ID + ", " + KEY_USER_ID + "))";

    public static final String CREATE_ITEM_ORDER_ITEM = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_ORDER_ITEM + "("
            + KEY_PLACE_ITEM_ID + " INTEGER NOT NULL, " + KEY_ITEM_ORDER_ID + " TEXT NOT NULL, "
            + KEY_QUANTITY + " INTEGER NOT NULL, PRIMARY KEY (" + KEY_PLACE_ITEM_ID + ", " + KEY_ITEM_ORDER_ID + "))";

    public static final String CREATE_TOKEN = "CREATE TABLE IF NOT EXISTS " + TABLE_SESSION + "("
             + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_TOKEN + " TEXT NOT NULL, "
             + KEY_EXPIRE_TIME + " TEXT NOT NULL, " + KEY_MAIL + " TEXT NOT NULL, "
             + KEY_USER_ID + " INTEGER NOT NULL)";

    public static final String CREATE_URL = "CREATE TABLE IF NOT EXISTS " + TABLE_URL + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_SERVER_URL + " TEXT NOT NULL)";

    public static final String CREATE_ITEM_TYPES = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_TYPES + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_NAME + " TEXT NOT NULL)";

    public static final String CREATE_FAVORITES = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_NAME + " TEXT NOT NULL, "
            + KEY_DESCRIPTION + " TEXT NOT NULL, " + KEY_LATITUDE + " TEXT NOT NULL, "
            + KEY_LONGITUDE + " TEXT NOT NULL, " + KEY_ADDRESS + " TEXT NOT NULL, "
            + KEY_IS_DISCO + " BOOLEAN NOT NULL, " + KEY_MIN_BUDGET + " DOUBLE NOT NULL, "
            + KEY_LIMIT_HOUR + " TEXT NOT NULL, " + KEY_ZONE_ID + " INTEGER NOT NULL, "
            + KEY_MUSIC_ID + " INTEGER NOT NULL, " + KEY_IMAGE_URL + " TEXT, "
            + KEY_IS_ACTIVE + " BOOLEAN NOT NULL, " + KEY_OPEN + " TEXT NOT NULL, "
            + KEY_CLOSE + " TEXT NOT NULL, " + KEY_MAIL + " TEXT NOT NULL)";

    public static final String CREATE_PLACE = "CREATE TABLE IF NOT EXISTS " + TABLE_PLACE + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_NAME + " TEXT NOT NULL, "
            + KEY_DESCRIPTION + " TEXT NOT NULL, " + KEY_LATITUDE + " TEXT NOT NULL, "
            + KEY_LONGITUDE + " TEXT NOT NULL, " + KEY_ADDRESS + " TEXT NOT NULL, "
            + KEY_IS_DISCO + " BOOLEAN NOT NULL, " + KEY_MIN_BUDGET + " DOUBLE NOT NULL, "
            + KEY_LIMIT_HOUR + " TEXT NOT NULL, " + KEY_ZONE_ID + " INTEGER NOT NULL, "
            + KEY_MUSIC_ID + " INTEGER NOT NULL, " + KEY_IMAGE_URL + " TEXT, "
            + KEY_IS_ACTIVE + " BOOLEAN NOT NULL, " + KEY_OPEN + " TEXT NOT NULL, "
            + KEY_CLOSE + " TEXT NOT NULL)";

    public static final String CREATE_MUSIC = "CREATE TABLE IF NOT EXISTS " + TABLE_MUSIC + "(" +
            KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_NAME + " TEXT NOT NULL)";

    public static final String CREATE_ZONE = "CREATE TABLE IF NOT EXISTS " + TABLE_ZONE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY NOT NULL, " + KEY_NAME + " TEXT NOT NULL)";


}
