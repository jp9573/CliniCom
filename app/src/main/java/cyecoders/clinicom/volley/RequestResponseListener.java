package cyecoders.clinicom.volley;


import cyecoders.clinicom.network.NetworkException;

public interface RequestResponseListener {

    interface Listener{
        <T> void onResponse(T response);
    }

    interface ErrorListener{
        void onError(NetworkException error);
    }

}
