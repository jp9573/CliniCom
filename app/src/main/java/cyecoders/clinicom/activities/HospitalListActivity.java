package cyecoders.clinicom.activities;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
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
    TextView currentCity, serviceName;
    Context context;
    String s_id;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        context = this;
        recyclerView = findViewById(R.id.hospital_recycler_view);
        networkCommunicator = NetworkCommunicator.getInstance();
        currentCity = findViewById(R.id.currentCity);
        serviceName = findViewById(R.id.tv_service_name);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        if(!Master.cityName.equals("")) {
            currentCity.setText(Master.cityName);
        }

        currentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCityDialog();
            }
        });

        s_id = getIntent().getStringExtra("s_id");
        if(s_id != null) {
            serviceName.setVisibility(View.VISIBLE);
            String name = getIntent().getStringExtra("s_name");
            serviceName.setText(name);
            if(!Master.cityName.equals(""))
                makeServiceWiseHospitalRequest(s_id, Master.cityName);
            else
                makeServiceWiseHospitalRequest(s_id, "ALL");
        }else {
            serviceName.setVisibility(View.GONE);
            makeRequest();
        }

    }

    private void showCityDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.get_city,null);
        builder.setView(dialogView);

        final AutoCompleteTextView completeTextView = dialogView.findViewById(R.id.autocompleteView);
        Button search = dialogView.findViewById(R.id.btn_search);

        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String city = completeTextView.getText().toString();
                makeSearchRequest(city);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Master.cityList);
        completeTextView.setAdapter(adapter);
        completeTextView.setThreshold(1);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = completeTextView.getText().toString();
                makeSearchRequest(city);
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    void makeSearchRequest(String city) {
        if(city.length() > 0) {
            Master.cityName = city;
            currentCity.setText(city);
            dialog.cancel();
            if(s_id != null) {
                makeServiceWiseHospitalRequest(s_id, Master.cityName);
            }else {
                makeRequest();
            }
        }else {
            Toast.makeText(context, "No data found!", Toast.LENGTH_LONG).show();
        }
    }

    void makeRequest() {
        Master.hospitalList.clear();
        Master.showProgressDialog(this, "Finding Hospitals!");
        networkCommunicator.data(Master.getHospitalsAPI(Master.cityName),
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

    void makeServiceWiseHospitalRequest(String sID, String city) {
        Master.hospitalList.clear();
        Master.showProgressDialog(this, "Finding Hospitals!");
        networkCommunicator.data(Master.getHospitalDetailByServiceAPI(sID, city),
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
                                hospital = new Hospital(obj.getString("h_id"), obj.getString("name"), obj.getString("address"), obj.getString("city"), obj.getString("stars"), obj.getString("lati"), obj.getString("longi"), obj.getString("contact"));
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
