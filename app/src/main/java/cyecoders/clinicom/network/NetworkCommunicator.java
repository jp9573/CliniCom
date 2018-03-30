package cyecoders.clinicom.network;

import android.content.Context;

import org.json.JSONObject;

import cyecoders.clinicom.volley.RequestResponseListener;
import cyecoders.clinicom.volley.VolleyRequest;
import cyecoders.clinicom.volley.VolleyRequestDispatcher;

public class NetworkCommunicator
{

    private static NetworkCommunicator networkCommunicator;

    public static NetworkCommunicator getInstance() {

        if (networkCommunicator == null) {
            networkCommunicator = new NetworkCommunicator();
        }
        return networkCommunicator;
    }

    private NetworkCommunicator() {}

    public void data
            (String url,
             int method,
             JSONObject jsonObject,
             boolean auth,
             final NetworkResponse.Listener listener,
             final NetworkResponse.ErrorListener errorListener,
             String tag,
             Context context)
    {
        try {

            final VolleyRequest volleyRequest = new VolleyRequest();
            volleyRequest.url = url;
            volleyRequest.method = method;
            volleyRequest.context = context.getApplicationContext();
            volleyRequest.tag = tag;
            volleyRequest.jsonObject=jsonObject;
            volleyRequest.authentication=auth;
            volleyRequest.contentType = "application/json;charset=utf-8";


            VolleyRequestDispatcher.doNetworkOperation(volleyRequest, new RequestResponseListener.Listener() {
                @Override
                public <T> void onResponse(T response) {
                    try {

                        listener.onResponse(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new RequestResponseListener.ErrorListener() {

                @Override
                public void onError(NetworkException error) {
                    try
                    {
                        errorListener.onError(error);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
