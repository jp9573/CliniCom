package cyecoders.clinicom.volley;

import android.content.Context;

import org.json.JSONObject;

public class VolleyRequest {
    public int method;
    public String url;
    public String contentType;
    public Context context;
    public boolean authentication;
    public String tag;
    public JSONObject jsonObject;
}
