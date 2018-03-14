package violatorsusers.traffic.com.trafficviolatorsusers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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
        Query query = databaseRef.child("reports").orderByChild("vehicleNo").equalTo(searchField.getText().toString().toUpperCase());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String reportID = postSnapshot.getKey();
                    Report report = postSnapshot.getValue(Report.class);
                    String timeFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(report.getDatetime());
                    String dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.FRENCH).format(report.getDatetime());
                    ReportListItem item = new ReportListItem(reportID,
                            report.getVehicleNo(),
                            report.getReason(),
                            "₹ " + String.valueOf(report.getFine()),
                            report.isFinePaid(),
                            dateFormat,
                            timeFormat,
                            getItemBackground(report.getReason()));
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

    // On Pressing app the app will be closed completely
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_alertdialog).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportView = (RecyclerView) findViewById(R.id.RecycleView);
        reportViewAdapter = new RecyclerViewAdapter(MainActivity.this,reportList);
        reportView.setHasFixedSize(true);
        reportView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        reportView.setItemAnimator(new DefaultItemAnimator());
        searchField = (EditText) findViewById(R.id.search_field);
        Logout = (Button) findViewById(R.id.sign_out_button);
        Search = (Button) findViewById(R.id.search_button);
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
