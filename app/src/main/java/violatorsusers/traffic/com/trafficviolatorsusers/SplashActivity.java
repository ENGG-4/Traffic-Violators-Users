package violatorsusers.traffic.com.trafficviolatorsusers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //switch from splash activity to main activity
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
        finish();
    }
}
