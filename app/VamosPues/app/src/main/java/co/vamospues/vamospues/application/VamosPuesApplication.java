package co.vamospues.vamospues.application;

import android.app.Application;
import android.content.Intent;

import co.vamospues.vamospues.helpers.ConnectivityReceiver;
import co.vamospues.vamospues.services.PromosService;

/**
 * Created by Manuela Duque M on 08/02/2017.
 */

public class VamosPuesApplication extends Application {

    public static VamosPuesApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static synchronized VamosPuesApplication getInstance() {
        return app;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
