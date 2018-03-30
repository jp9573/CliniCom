package cyecoders.clinicom.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cyecoders.clinicom.Master;
import cyecoders.clinicom.R;
import cyecoders.clinicom.adapters.HospitalAdapter;
import cyecoders.clinicom.models.Hospital;
import cyecoders.clinicom.network.NetworkCommunicator;
import cyecoders.clinicom.network.NetworkException;
import cyecoders.clinicom.network.NetworkResponse;

public class HospitalListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private NetworkCommunicator networkCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        recyclerView = findViewById(R.id.hospital_recycler_view);
        networkCommunicator = NetworkCommunicator.getInstance();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        makeRequest();

    }

    void makeRequest() {
        Master.showProgressDialog(this, "Finding Hospitals!");
        networkCommunicator.data(Master.getHospitalsAPI(Master.cityId),
                Request.Method.GET,
                null,
                false,new NetworkResponse.Listener() {

                    @Override
                    public void onResponse(Object result) {
                        Master.dismissProgressDialog();
                        String response = (String)result;
                        JSONArray array = null;
                        try {
                            array = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            Hospital hospital = null;
                            try {
                                hospital = new Hospital(obj.getString("h_id"), obj.getString("name"), obj.getString("address"), Master.cityName, obj.getString("stars"), obj.getString("lati"), obj.getString("longi"), obj.getString("contact"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Master.hospitalList.add(hospital);
                        }
                        if(array != null) {
                            // Toast.makeText(getApplicationContext(), obj + "", Toast.LENGTH_LONG).show();
                            HospitalAdapter adapter = new HospitalAdapter(Master.hospitalList, getApplicationContext());

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }, new NetworkResponse.ErrorListener()
                {
                    @Override
                    public void onError(NetworkException error) {
                        Master.dismissProgressDialog();
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),getString(R.string.toast_technical_issue),Toast.LENGTH_LONG).show();
                    }
                },"main", getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(networkCommunicator==null) {
            networkCommunicator = NetworkCommunicator.getInstance();
        }
    }

}
