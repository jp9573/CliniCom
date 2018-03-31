package cyecoders.clinicom.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cyecoders.clinicom.Master;
import cyecoders.clinicom.R;
import cyecoders.clinicom.adapters.SingleServiceAdapter;
import cyecoders.clinicom.models.Services;
import cyecoders.clinicom.network.NetworkCommunicator;
import cyecoders.clinicom.network.NetworkException;
import cyecoders.clinicom.network.NetworkResponse;

public class FeedbackActivity extends AppCompatActivity {

    Button mSubmitButton;
    EditText mEditTextFeedback;
    RatingBar mRatingBar;

    int noOfStars=0;
    String feedbackText="";
    private NetworkCommunicator networkCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mSubmitButton = findViewById(R.id.submit_button);
        mEditTextFeedback = findViewById(R.id.editTextFeedback);
        mRatingBar = findViewById(R.id.ratingBar);
        networkCommunicator = NetworkCommunicator.getInstance();


        mRatingBar.setRating(3);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbackText = mEditTextFeedback.getText().toString();

                if (feedbackText.equals("")){
                    mEditTextFeedback.setError("Please give your feedback");
                }
                else{
                    noOfStars = (int)mRatingBar.getRating();
                    feedbackText = mEditTextFeedback.getText().toString();
                    //Toast.makeText(FeedbackActivity.this, ""+"Stars : "+noOfStars+"\nFeedback : "+feedbackText  , Toast.LENGTH_LONG).show();
                    makeRequest(feedbackText, String.valueOf(noOfStars));
                }
            }
        });
    }

    void makeRequest(String feedback, String stars) {
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Master.showProgressDialog(this, "Sending your feedback!");
        networkCommunicator.data(Master.getFeedbackAPI(user, feedback, stars),
                Request.Method.GET,
                null,
                false,new NetworkResponse.Listener() {

                    @Override
                    public void onResponse(Object result) {
                        Master.dismissProgressDialog();
                        String response = (String)result;
                        if(response.equals("Done")) {
                            Toast.makeText(getApplicationContext(), "Feedback successfully submitted!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Error in submitting feedback!", Toast.LENGTH_LONG).show();
                        }
                        onBackPressed();
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
