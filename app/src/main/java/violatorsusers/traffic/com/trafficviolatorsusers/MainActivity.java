package violatorsusers.traffic.com.trafficviolatorsusers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

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
    Button bookmarks;
    ImageButton Search;
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

       final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Searching...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = databaseRef.child("reports").orderByChild("vehicleNo").equalTo(searchField.getText().toString().toUpperCase());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String reportID = postSnapshot.getKey();
                    Report report = postSnapshot.getValue(Report.class);
                    String timeFormat  = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(report.getDatetime());
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
                if(reportList.size() == 0) {
                    Toast.makeText(MainActivity.this, "No Records Found!", Toast.LENGTH_SHORT).show();
                }
                reportView.setAdapter(reportViewAdapter);
                progressDialog.dismiss();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //TODO - menu_main.xml
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
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
        bookmarks = (Button) findViewById(R.id.bookmarks);
        Search = (ImageButton) findViewById(R.id.search_button);
        reportView.setHasFixedSize(true);
        reportView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();

        FloatingActionButton addBookmark = (FloatingActionButton) findViewById(R.id.add_bookmarks);
        addBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this ,NewBookmark.class));
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this,GoogleSignInActivity.class));
                }
            }
        };

        /*Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });*/
//Firebase Sign-Out Logout Code End

        bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Bookmarks.class));
            }
        });

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReportList();
            }
        });
    }
}
