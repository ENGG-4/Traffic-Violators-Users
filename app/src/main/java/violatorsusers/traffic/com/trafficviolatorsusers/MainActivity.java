package violatorsusers.traffic.com.trafficviolatorsusers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
//Firebase Sign-Out Logout Code Start
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Button Logout,Search;
    RecyclerView reportView;
    EditText searchField;
    private List<ReportListItem> reportList = new ArrayList<>();
    private RecyclerViewAdapter reportViewAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void setReportList() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = databaseRef.child("reports").orderByChild("vehicleNo").equalTo(searchField.getText().toString());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    String timeFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(report.getDatetime());
                    String dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.FRENCH).format(report.getDatetime());
                    ReportListItem item = new ReportListItem(report.getVehicleNo(),report.getReason(),"₹ " + String.valueOf(report.getFine()),dateFormat,timeFormat,getItemBackground(report.getReason()));
                    reportList.add(item);
                }
                reportView.setAdapter(reportViewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int getItemBackground(String reason)  {
        if(reason.toLowerCase().equals("speed limit"))
            return R.drawable.ic_speed;
        else if(reason.toLowerCase().equals("drinking and driving"))
            return R.drawable.ic_drink;
        else if(reason.toLowerCase().equals("parking violations"))
            return R.drawable.ic_parking;
        else if(reason.toLowerCase().equals("jumping signal"))
            return R.drawable.ic_signal;
        else
            return R.drawable.ic_default;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportViewAdapter = new RecyclerViewAdapter(MainActivity.this,reportList);
        reportView.setHasFixedSize(true);
        reportView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        reportView.setItemAnimator(new DefaultItemAnimator());
        searchField = findViewById(R.id.search_field);
        Logout = findViewById(R.id.sign_out_button);
        Search = findViewById(R.id.search_button);
        reportView = findViewById(R.id.RecycleView);
        reportView.setHasFixedSize(true);
        reportView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this,GoogleSignInActivity.class));
                }
            }
        };

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });
//Firebase Sign-Out Logout Code End

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReportList();
            }
        });
    }
}
