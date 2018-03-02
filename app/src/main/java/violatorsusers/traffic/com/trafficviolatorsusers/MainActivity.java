package violatorsusers.traffic.com.trafficviolatorsusers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // Manually configure Firebase Options
    FirebaseOptions options = new FirebaseOptions.Builder()
            .setApplicationId("1:523255386290:android:8e5eb46ead6e471f") // Required for Analytics.
            .setApiKey("AIzaSyAXrZ2EoBWRh24BnQQUJUDpkvIZDENWCyk") // Required for Auth.
            .setDatabaseUrl("https://trafficviolators-f0e4c.firebaseio.com/") // Required for RTDB.
            .build();
}
