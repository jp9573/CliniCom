package cyecoders.clinicom;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import cyecoders.clinicom.models.Hospital;
import cyecoders.clinicom.models.Services;

/**
 * Created by jay on 30/3/18.
 */

public class Master {

    public static String serverURL = "http://jaypatel.co.in/cc";

    public static String cityName;
    public static String cityId;


    public static String getHospitalsAPI(String id) {
        return serverURL + "/hospitals.php?c="+id;
    }

    public static String getCityIdAPI(String id) {
        return serverURL + "/getid.php?c="+id;
    }

    public static String getHospitalDetailAPI(String id) {
        return serverURL + "/getserv.php?c="+id;
    }

    public static String getAllServicesAPI() {
        return serverURL + "/getservices.php";
    }

    public static List<Hospital> hospitalList = new ArrayList<>();

    public static List<Services> servicesList = new ArrayList<>();

    public static List<String> cityList = new ArrayList<>();

    private static ProgressDialog pDialog;

    public static void showProgressDialog(Context context, String message) {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }
}
