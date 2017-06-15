package co.vamospues.vamospues.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import co.vamospues.vamospues.enums.Database;
import co.vamospues.vamospues.models.ItemOrder;
import co.vamospues.vamospues.models.ItemOrderItem;
import co.vamospues.vamospues.models.ItemType;
import co.vamospues.vamospues.models.Music;
import co.vamospues.vamospues.models.Place;
import co.vamospues.vamospues.models.Prefs;
import co.vamospues.vamospues.models.Promo;
import co.vamospues.vamospues.models.SelectablePlaceItem;
import co.vamospues.vamospues.models.User;
import co.vamospues.vamospues.models.Zone;

/**
 * Created by Manuela Duque M on 06/02/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context){
        super(context, Database.DB_NAME, null, Database.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Database.CREATE_MUSIC);
        database.execSQL(Database.CREATE_PLACE);
        database.execSQL(Database.CREATE_ZONE);
        database.execSQL(Database.CREATE_URL);
        database.execSQL(Database.CREATE_ITEM_TYPES);
        database.execSQL(Database.CREATE_SESSION);
        database.execSQL(Database.CREATE_FAVORITES);
        database.execSQL(Database.CREATE_ITEM_ORDER_ITEM);
        database.execSQL(Database.CREATE_ITEM_ORDER);
        database.execSQL(Database.CREATE_PROMO);
        database.execSQL(Database.CREATE_PREFS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    
    public void truncateDatabase(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + Database.TABLE_MUSIC);
        database.execSQL("DELETE FROM " + Database.TABLE_PLACE);
        database.execSQL("DELETE FROM " + Database.TABLE_ZONE);
        database.execSQL("DELETE FROM " + Database.TABLE_URL);
        database.execSQL("DELETE FROM " + Database.TABLE_ITEM_TYPES);
        database.execSQL("DELETE FROM " + Database.TABLE_SESSION);
        database.execSQL("DELETE FROM " + Database.TABLE_FAVORITES);
        database.execSQL("DELETE FROM " + Database.TABLE_ITEM_ORDER_ITEM);
        database.execSQL("DELETE FROM " + Database.TABLE_ITEM_ORDER);
        database.execSQL("DELETE FROM " + Database.TABLE_PREFS);
    }

    //--Table prefs methods begining--

    public boolean insertPrefs(Prefs prefs){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Database.KEY_ZONE_ID, prefs.getZone().getId());
            values.put(Database.KEY_MUSIC_ID, prefs.getMusic().getId());
            values.put(Database.KEY_USER_ID, getCurrentUser().getId());
            values.put(Database.KEY_NOTIFY, prefs.isNotify());
            database.replace(Database.TABLE_PREFS, null, values);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Prefs getPrefs(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_PREFS
                + " WHERE " + Database.KEY_USER_ID + " = " + getCurrentUser().getId(), null);
        Prefs prefs = null;
        if (cursor.moveToFirst()){
            prefs = new Prefs();
            prefs.setZone(getZone(cursor.getInt(1)));
            prefs.setMusic(getMusic(cursor.getInt(2)));
            prefs.setNotify(cursor.getInt(3) > 0);
        }
        cursor.close();
        return prefs;
    }

    //--Table prefs methods ending--

    //--Table promo methods begining--

    public boolean insertPromo(Promo promo){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Database.KEY_ID, promo.getId());
            database.insert(Database.TABLE_PROMO, null, values);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsPromo(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_PROMO + " WHERE "
                + Database.KEY_ID + " = " + id, null);
        if (cursor.moveToFirst()){
            return true;
        }
        cursor.close();
        return false;
    }

    //--Table promo methods ending--

    //--Table token methods begining--
    public void deleteToken(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + Database.TABLE_SESSION);
    }

    public String getToken(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_SESSION, null);
        if (cursor.moveToFirst()){
            String token = cursor.getString(1);
            cursor.close();
            return token;
        }
        cursor.close();
        return null;
    }

    public String getExpirationDate(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_SESSION, null);
        if (cursor.moveToFirst()){
            String time = cursor.getString(2);
            cursor.close();
            return time;
        }
        cursor.close();
        return null;
    }

    public String getCurrentMail(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_SESSION, null);
        if (cursor.moveToFirst()){
            String mail = cursor.getString(3);
            cursor.close();
            return mail;
        }
        cursor.close();
        return null;
    }

    public int getCurrentId(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_SESSION, null);
        if (cursor.moveToFirst()){
            int id = cursor.getInt(4);
            cursor.close();
            return id;
        }
        cursor.close();
        return 0;
    }

    private User getCurrentUser(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_SESSION, null);
        User user = null;
        if (cursor.moveToFirst()){
            user = new User();
            user.setMail(cursor.getString(3));
            user.setId(cursor.getInt(4));
            cursor.close();
        }
        cursor.close();
        return user;
    }

    public void setAuthToken(String token, String time, String mail, int id){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Database.KEY_TOKEN, token);
        values.put(Database.KEY_EXPIRE_TIME, time);
        values.put(Database.KEY_MAIL, mail);
        values.put(Database.KEY_USER_ID, id);
        values.put(Database.KEY_ID, 1);

        database.replace(Database.TABLE_SESSION, null, values);
    }
    //--Table token methods ending--

    //--Table zone methods begining--

    public boolean insertZones(List<Zone> zones){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (Zone zone : zones){
                ContentValues values = new ContentValues();
                values.put(Database.KEY_ID, zone.getId());
                values.put(Database.KEY_NAME, zone.getName());
                database.insert(Database.TABLE_ZONE, null, values);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Zone> getZones(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_ZONE, null);
        List<Zone> zones = new ArrayList<>();
        try {
            if (cursor.moveToFirst()){
                do {
                    Zone zone = new Zone();
                    zone.setId(cursor.getInt(0));
                    zone.setName(cursor.getString(1));
                    zones.add(zone);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return zones;
        } catch (Exception e){
            cursor.close();
            return null;
        }
    }

    public Zone getZone(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_ZONE
                + " WHERE " + Database.KEY_ID + " = " + id, null);
        Zone zone = null;
        try {
            if (cursor.moveToFirst()){
                zone = new Zone();
                zone.setId(cursor.getInt(0));
                zone.setName(cursor.getString(1));
            }
            cursor.close();
            return zone;
        } catch (Exception e){
            cursor.close();
            return null;
        }
    }

    //-- Table zone methods ending--

    //-- Table item_types methods beginning

    public boolean insertItemTypes(List<ItemType> itemTypes){

        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (ItemType itemType : itemTypes){
                ContentValues values = new ContentValues();
                values.put(Database.KEY_ID, itemType.getId());
                values.put(Database.KEY_NAME, itemType.getName());
                database.insert(Database.TABLE_ITEM_TYPES, null, values);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public ItemType getItemType(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_ITEM_TYPES
                + " WHERE " + Database.KEY_ID + " = " + id, null);
        ItemType itemType = null;
        try {
            if (cursor.moveToFirst()){
                itemType = new ItemType();
                itemType.setId(cursor.getInt(0));
                itemType.setName(cursor.getString(1));
            }
            cursor.close();
            return itemType;
        } catch (Exception e){
            cursor.close();
            return null;
        }
    }

    //-- Table item_types methods ending

    //-- Table music method beginning--

    public boolean insertMusicTypes(List<Music> musicTypes){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (Music music : musicTypes){
                ContentValues values = new ContentValues();
                values.put(Database.KEY_ID, music.getId());
                values.put(Database.KEY_NAME, music.getName());
                database.insert(Database.TABLE_MUSIC, null, values);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Music> getMusicTypes(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_MUSIC, null);
        List<Music> musics = new ArrayList<>();
        try {
            if (cursor.moveToFirst()){
                do {
                    Music music = new Music();
                    music.setId(cursor.getInt(0));
                    music.setName(cursor.getString(1));
                    musics.add(music);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return musics;
        } catch (Exception e){
            cursor.close();
            return null;
        }
    }

    public Music getMusic(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_MUSIC
                + " WHERE " + Database.KEY_ID + " = " + id, null);
        Music music = null;
        try {
            if (cursor.moveToFirst()){
                music = new Music();
                music.setId(cursor.getInt(0));
                music.setName(cursor.getString(1));
            }
            cursor.close();
            return music;
        } catch (Exception e){
            cursor.close();
            return null;
        }
    }

    //-- Table music methods ending--

    //--Table place methods begining--

    public boolean deleteFavorite(int id){
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL("DELETE FROM " + Database.TABLE_FAVORITES + " WHERE " + Database.KEY_ID + " = " + id
                    + " AND " + Database.KEY_MAIL + " = '" + getCurrentMail() + "'");
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean isFavorite(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_FAVORITES
                + " WHERE " + Database.KEY_ID + " = " + id
                + " AND " + Database.KEY_MAIL + " = '" + getCurrentMail() + "'", null);

        if (cursor.moveToFirst()){
            cursor.close();
            return true;
        }

        cursor.close();
        return false;
    }

    public List<Place> getFavorites(){
        SQLiteDatabase database = this.getWritableDatabase();
        List<Place> places = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_FAVORITES + " WHERE "
                + Database.KEY_MAIL + " = '" + getCurrentMail() + "'", null);
        if (cursor.moveToFirst()){
            do {
                Place place = new Place();
                place.setId(cursor.getInt(0));
                place.setName(cursor.getString(1));
                place.setDescription(cursor.getString(2));
                place.setLatitude(cursor.getDouble(3));
                place.setLongitude(cursor.getDouble(4));
                place.setAddress(cursor.getString(5));
                place.setDisco(cursor.getInt(6) > 0);
                place.setBudget(cursor.getDouble(7));
                place.setLimitHour(cursor.getString(8));
                place.setZone(getZone(cursor.getInt(9)));
                place.setMusic(getMusic(cursor.getInt(10)));
                place.setImageUrl(cursor.getString(11));
                place.setActive(cursor.getInt(12) > 0);
                place.setOpen(cursor.getString(13));
                place.setClose(cursor.getString(14));
                places.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return places;
    }

    public boolean insertFavorite(Place place){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(Database.KEY_NAME, place.getName());
            values.put(Database.KEY_LONGITUDE, place.getLongitude());
            values.put(Database.KEY_LATITUDE, place.getLatitude());
            values.put(Database.KEY_ZONE_ID, place.getZone().getId());
            values.put(Database.KEY_MUSIC_ID, place.getMusic().getId());
            values.put(Database.KEY_DESCRIPTION, place.getDescription());
            values.put(Database.KEY_LIMIT_HOUR, place.getLimitHour());
            values.put(Database.KEY_IS_ACTIVE, place.isActive());
            values.put(Database.KEY_IS_DISCO, place.isDisco());
            values.put(Database.KEY_MIN_BUDGET, place.getBudget());
            values.put(Database.KEY_CLOSE, place.getClose());
            values.put(Database.KEY_OPEN, place.getOpen());
            values.put(Database.KEY_ADDRESS, place.getAddress());
            values.put(Database.KEY_IMAGE_URL, place.getImageUrl());
            values.put(Database.KEY_MAIL, getCurrentMail());
            database.insert(Database.TABLE_FAVORITES, null, values);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertPlaces(List<Place> places){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            for (Place place : places){
                ContentValues values = new ContentValues();
                values.put(Database.KEY_NAME, place.getName());
                values.put(Database.KEY_LONGITUDE, place.getLongitude());
                values.put(Database.KEY_LATITUDE, place.getLatitude());
                values.put(Database.KEY_ZONE_ID, place.getZone().getId());
                values.put(Database.KEY_MUSIC_ID, place.getMusic().getId());
                values.put(Database.KEY_DESCRIPTION, place.getDescription());
                values.put(Database.KEY_LIMIT_HOUR, place.getLimitHour());
                values.put(Database.KEY_IS_ACTIVE, place.isActive());
                values.put(Database.KEY_IS_DISCO, place.isDisco());
                values.put(Database.KEY_MIN_BUDGET, place.getBudget());
                values.put(Database.KEY_CLOSE, place.getClose());
                values.put(Database.KEY_OPEN, place.getOpen());
                values.put(Database.KEY_ADDRESS, place.getAddress());
                values.put(Database.KEY_IMAGE_URL, place.getImageUrl());
                database.insert(Database.TABLE_PLACE, null, values);
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Place getPlace(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Place place = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_PLACE + " WHERE "
                + Database.KEY_ID + " = " + id, null);
        if (cursor.moveToFirst()){
            place = new Place();
            place.setId(cursor.getInt(0));
            place.setName(cursor.getString(1));
            place.setDescription(cursor.getString(2));
            place.setLatitude(cursor.getDouble(3));
            place.setLongitude(cursor.getDouble(4));
            place.setAddress(cursor.getString(5));
            place.setDisco(cursor.getInt(6) > 0);
            place.setBudget(cursor.getDouble(7));
            place.setLimitHour(cursor.getString(8));
            place.setZone(getZone(cursor.getInt(9)));
            place.setMusic(getMusic(cursor.getInt(10)));
            place.setImageUrl(cursor.getString(11));
            place.setActive(cursor.getInt(12) > 0);
            place.setOpen(cursor.getString(13));
            place.setClose(cursor.getString(14));
        }
        cursor.close();
        return place;
    }

    public List<Place> getPlacesByBudget(double budget){

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_PLACE + " WHERE "
                + Database.KEY_MIN_BUDGET + " <= " + budget, null);

        List<Place> places = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                Place place = new Place();
                place.setId(cursor.getInt(0));
                place.setName(cursor.getString(1));
                place.setDescription(cursor.getString(2));
                place.setLatitude(cursor.getDouble(3));
                place.setLongitude(cursor.getDouble(4));
                place.setAddress(cursor.getString(5));
                place.setDisco(cursor.getInt(6) > 0);
                place.setBudget(cursor.getDouble(7));
                place.setLimitHour(cursor.getString(8));
                place.setZone(getZone(cursor.getInt(9)));
                place.setMusic(getMusic(cursor.getInt(10)));
                place.setImageUrl(cursor.getString(11));
                place.setActive(cursor.getInt(12) > 0);
                place.setOpen(cursor.getString(13));
                place.setClose(cursor.getString(14));
                places.add(place);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return places;
    }

    public List<Place> getPlacesByMusic(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        List<Place> places = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_PLACE + " WHERE "
                + Database.KEY_MUSIC_ID + " = " + id, null);
        if (cursor.moveToFirst()){
            do {
                Place place = new Place();
                place.setId(cursor.getInt(0));
                place.setName(cursor.getString(1));
                place.setDescription(cursor.getString(2));
                place.setLatitude(cursor.getDouble(3));
                place.setLongitude(cursor.getDouble(4));
                place.setAddress(cursor.getString(5));
                place.setDisco(cursor.getInt(6) > 0);
                place.setBudget(cursor.getDouble(7));
                place.setLimitHour(cursor.getString(8));
                place.setZone(getZone(cursor.getInt(9)));
                place.setMusic(getMusic(cursor.getInt(10)));
                place.setImageUrl(cursor.getString(11));
                place.setActive(cursor.getInt(12) > 0);
                place.setOpen(cursor.getString(13));
                place.setClose(cursor.getString(14));
                places.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return places;
    }

    public List<Place> getPlaces(){
        SQLiteDatabase database = this.getWritableDatabase();
        List<Place> places = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_PLACE, null);
        if (cursor.moveToFirst()){
            do {
                Place place = new Place();
                place.setId(cursor.getInt(0));
                place.setName(cursor.getString(1));
                place.setDescription(cursor.getString(2));
                place.setLatitude(cursor.getDouble(3));
                place.setLongitude(cursor.getDouble(4));
                place.setAddress(cursor.getString(5));
                place.setDisco(cursor.getInt(6) > 0);
                place.setBudget(cursor.getDouble(7));
                place.setLimitHour(cursor.getString(8));
                place.setZone(getZone(cursor.getInt(9)));
                place.setMusic(getMusic(cursor.getInt(10)));
                place.setImageUrl(cursor.getString(11));
                place.setActive(cursor.getInt(12) > 0);
                place.setOpen(cursor.getString(13));
                place.setClose(cursor.getString(14));
                places.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return places;
    }

    public List<Place> getPlacesByZone(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        List<Place> places = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_PLACE + " WHERE "
                + Database.KEY_ZONE_ID + " = " + id, null);
        if (cursor.moveToFirst()){
            do {
                Place place = new Place();
                place.setId(cursor.getInt(0));
                place.setName(cursor.getString(1));
                place.setDescription(cursor.getString(2));
                place.setLatitude(cursor.getDouble(3));
                place.setLongitude(cursor.getDouble(4));
                place.setAddress(cursor.getString(5));
                place.setDisco(cursor.getInt(6) > 0);
                place.setBudget(cursor.getDouble(7));
                place.setLimitHour(cursor.getString(8));
                place.setZone(getZone(cursor.getInt(9)));
                place.setMusic(getMusic(cursor.getInt(10)));
                place.setImageUrl(cursor.getString(11));
                place.setActive(cursor.getInt(12) > 0);
                place.setOpen(cursor.getString(13));
                place.setClose(cursor.getString(14));
                places.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return places;
    }

    //--Table place methods ending--

    //--Table item order methods begining
    public boolean createNewItemOrder(ItemOrder order){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try{
            values.put(Database.KEY_ID, order.getId());
            values.put(Database.KEY_USER_ID, order.getUser());
            values.put(Database.KEY_IS_REDEEMED, order.isRedeemed());
            values.put(Database.KEY_TOTAL, order.getTotal());
            values.put(Database.KEY_DATE, order.getDate());
            database.insert(Database.TABLE_ITEM_ORDER, null, values);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrder(ItemOrder order){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.execSQL("DELETE FROM " + Database.TABLE_ITEM_ORDER + " WHERE " + Database.KEY_ID + " = '" + order.getId() + "'");
            database.execSQL("DELETE FROM " + Database.TABLE_ITEM_ORDER_ITEM + " WHERE " + Database.KEY_ITEM_ORDER_ID + " = '" + order.getId() + "'");
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //--Table item order methods ending

    //--Table item order items methods begining

    public boolean createItemOrderItem(ItemOrderItem orderItem){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(Database.KEY_PLACE_ITEM_ID, orderItem.getSelectablePlaceItem().getId());
            values.put(Database.KEY_ITEM_ORDER_ID, orderItem.getItemOrder().getId());
            values.put(Database.KEY_QUANTITY, orderItem.getQuantity());
            database.replace(Database.TABLE_ITEM_ORDER_ITEM, null, values);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItemFromOrder(ItemOrder order, SelectablePlaceItem item){
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.execSQL("DELETE FROM " + Database.TABLE_ITEM_ORDER_ITEM
                    + " WHERE " + Database.KEY_ITEM_ORDER_ID + " = '" + order.getId() + "'"
                    + " AND " + Database.KEY_PLACE_ITEM_ID + " = " + item.getId());
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public ItemOrderItem getItemOrderItem(ItemOrder order, SelectablePlaceItem item){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_ITEM_ORDER_ITEM
                + " WHERE " + Database.KEY_ITEM_ORDER_ID + " = '" + order.getId() + "'"
                + " AND " + Database.KEY_PLACE_ITEM_ID + " = " + item.getId(), null);

        if (cursor.moveToFirst()){
            ItemOrderItem itemOrderItem = new ItemOrderItem();
            itemOrderItem.setSelectablePlaceItem(item);
            itemOrderItem.setItemOrder(order);
            itemOrderItem.setQuantity(cursor.getInt(2));
            cursor.close();
            return itemOrderItem;
        }
        cursor.close();
        return null;
    }

    //--Table item order items methods ending

    public String getBaseUrl(){
        SQLiteDatabase database = this.getWritableDatabase();
        String url = null;
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM " + Database.TABLE_URL, null);
            if (cursor.moveToFirst()){
                url = cursor.getString(1);
            }
            cursor.close();
            return url;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
