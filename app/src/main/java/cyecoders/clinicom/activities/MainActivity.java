package cyecoders.clinicom.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cyecoders.clinicom.Master;
import cyecoders.clinicom.R;
import cyecoders.clinicom.network.NetworkCommunicator;
import cyecoders.clinicom.network.NetworkException;
import cyecoders.clinicom.network.NetworkResponse;

public class MainActivity extends AppCompatActivity {

    ImageView dpImage;
    public static Context context;
    private NetworkCommunicator networkCommunicator;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dpImage = findViewById(R.id.img_dp);
        populateCityList();
        context = this;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = sharedPref.getString(getString(R.string.photoURL), "nan");
        networkCommunicator = NetworkCommunicator.getInstance();
        if(!url.equals("nan"))
            Picasso.get().load(url).placeholder(R.drawable.profile).into(dpImage);
    }

    public void getCities(View view) {
        showCityDialog();
    }

    void populateCityList() {
        Master.cityList.clear();
        try {
            readFromAssets(this, "cities1.txt");
        } catch (Exception e) {
            e.printStackTrace();
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

    void makeSearchRequest(final String city) {
        if(city.length() > 0) {
            Master.cityName = city;
            Master.hospitalList.clear();
            startActivity(new Intent(MainActivity.this, HospitalListActivity.class));
            dialog.cancel();
        }else {
            Toast.makeText(context, "No data found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(networkCommunicator==null) {
            networkCommunicator = NetworkCommunicator.getInstance();
        }
    }

    public String readFromAssets(Context context, String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine);
            Master.cityList.add(mLine);
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        System.exit(0);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure do you want to exit?").setPositiveButton("YES", dialogClickListener)
                .setNegativeButton("NO", dialogClickListener).show();
    }

    public void logout(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        FirebaseAuth.getInstance().signOut();

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove(getString(R.string.photoURL));
                        editor.commit();

                        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                        finishAffinity();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure do you want to logout?").setPositiveButton("YES", dialogClickListener)
                .setNegativeButton("NO", dialogClickListener).show();
    }

    public void showFeedback(View view) {
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    public void showAllServices(View view) {
        startActivity(new Intent(this, ServicesActivity.class));
    }
}
