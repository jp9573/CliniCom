package cyecoders.clinicom.volley;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import cyecoders.clinicom.Master;
import cyecoders.clinicom.network.NetworkException;


@SuppressWarnings("ALL")
public class VolleyRequestDispatcher {

    public static <T> void doNetworkOperation(VolleyRequest request, final RequestResponseListener.Listener listener,
                                              final RequestResponseListener.ErrorListener errorListener) throws AuthFailureError {
        try {
            Map<String, String> headers = new HashMap<>();
            if (request.contentType != null) {
                headers.put("Content-Type", request.contentType);

                if (request.authentication) {

                    String creds = String.format("%s:%s", "", "");
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    headers.put("Authorization", auth);
                }

            }
            CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest
                    (
                            request.method,
                            request.url,
                            headers,
                            new Response.Listener<T>() {

                                @Override
                                public void onResponse(T response) {
                                    try {
                                        listener.onResponse(response);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                errorListener.onError(new NetworkException(error.getMessage()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            if (request.method == Request.Method.POST) {

                customJSONObjectRequest.setRequestBody(request.jsonObject);
                if (request.jsonObject != null) {
                }
            }
            if (request.tag != null) {
                customJSONObjectRequest.setTag(request.tag);
            }
            customJSONObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4, DefaultRetryPolicy.DEFAULT_MAX_RETRIES * 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyRequestQueue.getInstance().addToRequestQueue(customJSONObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
