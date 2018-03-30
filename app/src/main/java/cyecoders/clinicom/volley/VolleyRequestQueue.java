package cyecoders.clinicom.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cyecoders.clinicom.activities.MainActivity;

public class VolleyRequestQueue{

    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;

    private VolleyRequestQueue(){
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance() {
        if (mInstance == null) {

            mInstance = new VolleyRequestQueue();
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(MainActivity.context);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}