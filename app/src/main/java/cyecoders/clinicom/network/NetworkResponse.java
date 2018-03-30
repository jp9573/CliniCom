package cyecoders.clinicom.network;


public interface NetworkResponse {

    interface Listener{
        void onResponse(Object result);
    }
    interface ErrorListener{
        void onError(NetworkException error);
    }
}
