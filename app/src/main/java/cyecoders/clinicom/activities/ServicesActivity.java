package cyecoders.clinicom.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cyecoders.clinicom.Master;
import cyecoders.clinicom.R;
import cyecoders.clinicom.adapters.SingleServiceAdapter;
import cyecoders.clinicom.models.Services;
import cyecoders.clinicom.network.NetworkCommunicator;
import cyecoders.clinicom.network.NetworkException;
import cyecoders.clinicom.network.NetworkResponse;

public class ServicesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private NetworkCommunicator networkCommunicator;
    AutoCompleteTextView completeTextView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        context = this;
        recyclerView = findViewById(R.id.services_recycler_view);
        networkCommunicator = NetworkCommunicator.getInstance();

        completeTextView = findViewById(R.id.autocompleteView);

        completeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), HospitalListActivity.class);
                intent.putExtra("s_id", Master.servicesList.get(i).getId());
                intent.putExtra("s_name", Master.servicesList.get(i).getName());
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        Master.servicesList.clear();
        makeRequest();
    }

    void makeRequest() {
        Master.showProgressDialog(this, "Finding Services!");
        networkCommunicator.data(Master.getAllServicesAPI(),
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
                        List<String> arrayList = new ArrayList<>();
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            Services services = null;
                            try {
                                services = new Services(obj.getString("s_id"), obj.getString("name"));
                                arrayList.add(obj.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Master.servicesList.add(services);
                        }
                        if(array != null) {
                            // Toast.makeText(getApplicationContext(), obj + "", Toast.LENGTH_LONG).show();
                            SingleServiceAdapter adapter = new SingleServiceAdapter(Master.servicesList, getApplicationContext());

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayList);

                            completeTextView.setAdapter(arrayAdapter);
                            completeTextView.setThreshold(1);

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
