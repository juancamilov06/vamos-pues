package co.vamospues.vamospues.models;

/**
 * Created by Manuela Duque M on 28/05/2017.
 */

public class Prefs {

    private Zone zone;
    private Music music;
    private boolean notify;

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
