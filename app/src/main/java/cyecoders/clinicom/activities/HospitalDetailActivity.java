package cyecoders.clinicom.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
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

import java.util.Locale;

import cyecoders.clinicom.Master;
import cyecoders.clinicom.R;
import cyecoders.clinicom.adapters.HospitalAdapter;
import cyecoders.clinicom.adapters.ServiceAdapter;
import cyecoders.clinicom.models.Hospital;
import cyecoders.clinicom.models.Services;
import cyecoders.clinicom.network.NetworkCommunicator;
import cyecoders.clinicom.network.NetworkException;
import cyecoders.clinicom.network.NetworkResponse;

public class HospitalDetailActivity extends AppCompatActivity {

    TextView name, city, address, rating;
    RecyclerView recyclerView;
    private NetworkCommunicator networkCommunicator;
    String hospitalId, phone, latitute, longitute, amneties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);

        name = findViewById(R.id.hd_name);
        city = findViewById(R.id.hsr_city);
        address = findViewById(R.id.hsr_address);
        rating = findViewById(R.id.hsr_rateing);

        name.setText(getIntent().getStringExtra("name"));
        city.setText(getIntent().getStringExtra("city"));
        address.setText(getIntent().getStringExtra("address"));
        System.out.println(" ---------- " + getIntent().getStringExtra("address"));
        rating.setText(getIntent().getStringExtra("rating") + "/5");

        hospitalId = getIntent().getStringExtra("hid");
        phone = getIntent().getStringExtra("phone");
        latitute = getIntent().getStringExtra("latitue");
        longitute = getIntent().getStringExtra("longitute");
        amneties = getIntent().getStringExtra("amneties");

        recyclerView = findViewById(R.id.hd_recycler_view);
        networkCommunicator = NetworkCommunicator.getInstance();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        Master.servicesList.clear();
        makeRequest();
    }

    void makeRequest() {
        Master.showProgressDialog(this, "Getting Services!");
        networkCommunicator.data(Master.getHospitalDetailAPI(hospitalId),
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
                            Services services = null;
                            try {
                                services = new Services(obj.getString("name"),
                                        obj.getString("details"),
                                        obj.getString("price"),
                                        obj.getString("stars"),
                                        obj.getString("no_of_stars"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Master.servicesList.add(services);
                        }
                        if(array != null) {
                            // Toast.makeText(getApplicationContext(), obj + "", Toast.LENGTH_LONG).show();
                            ServiceAdapter adapter = new ServiceAdapter(Master.servicesList, getApplicationContext());

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

    public void call(View view) {
        Intent call_dev = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));

        if(Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.CALL_PHONE}, 10);
            }else {
                startActivity(call_dev);
            }
        }else {
            startActivity(call_dev);
        }
    }

    public void direction(View view) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(latitute), Float.valueOf(longitute));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    public void showFacility(View view) {
        showFacilityDialog();
    }

    private void showFacilityDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.facility_layout,null);
        builder.setView(dialogView);

        TextView textView = dialogView.findViewById(R.id.tv_facility);

        String[] data = amneties.split(",");
        StringBuilder sb = new StringBuilder();

        for(String line: data) {
            sb.append("\u25BA " + line.trim() + "\n");
        }

        textView.setText(sb.toString());

        dialog = builder.create();
        dialog.show();
    }
}
