package cyecoders.clinicom.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import cyecoders.clinicom.R;

public class FeedbackActivity extends AppCompatActivity {

    Button mSubmitButton;
    EditText mEditTextFeedback;
    RatingBar mRatingBar;

    int noOfStars=0;
    String feedbackText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mSubmitButton = findViewById(R.id.submit_button);
        mEditTextFeedback = findViewById(R.id.editTextFeedback);
        mRatingBar = findViewById(R.id.ratingBar);


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
                    Toast.makeText(FeedbackActivity.this, ""+"Stars : "+noOfStars+"\nFeedback : "+feedbackText  , Toast.LENGTH_LONG).show();

                    onBackPressed();
                }
            }
        });
    }
}
