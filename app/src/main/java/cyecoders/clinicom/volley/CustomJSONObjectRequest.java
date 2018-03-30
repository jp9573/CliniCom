package cyecoders.clinicom.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


class CustomJSONObjectRequest<T> extends Request<T> {

    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    private JSONObject jsonObject;


    public CustomJSONObjectRequest(int method, String url, Map<String, String> headers,
                                   Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);

        this.headers = headers;
        this.listener = listener;
    }

    void setRequestBody(JSONObject object){
        this.jsonObject = object;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public byte[] getBody()
    {
        try {
            return jsonObject == null ? null : jsonObject.toString().getBytes();
            //return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
        }

        catch (Exception uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonObject, "utf-8");
            return null;
        }
    }


    @Override
    public String getBodyContentType() {
        return "application/json;charset=utf-8";
    }

    @Override
    protected void deliverResponse(T response)
    {
        listener.onResponse(response);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {

            String json = new String(response.data,HttpHeaderParser.parseCharset(response.headers));

            return Response.success(json,HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
