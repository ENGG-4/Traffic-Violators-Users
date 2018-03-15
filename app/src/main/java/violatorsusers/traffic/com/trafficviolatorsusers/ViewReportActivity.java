package violatorsusers.traffic.com.trafficviolatorsusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewReportActivity extends AppCompatActivity {

    private TextView vehicleNo,licenseNo,reason,fine,description,date,time,status;
    private ImageView photo,statusIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        initialize();
        getReport();
    }

    public void initialize() {
        vehicleNo = (TextView) findViewById(R.id.txt_vehicleNoValue);
        licenseNo = (TextView) findViewById(R.id.txt_licenseNoValue);
        reason = (TextView) findViewById(R.id.txt_reasonValue);
        fine = (TextView) findViewById(R.id.txt_fineValue);
        description = (TextView) findViewById(R.id.txt_descriptionValue);
        date = (TextView) findViewById(R.id.txt_dateValue);
        time = (TextView) findViewById(R.id.txt_timeValue);
        photo = (ImageView) findViewById(R.id.img_photo);
        statusIndicator = (ImageView) findViewById(R.id.status);
        status = (TextView) findViewById(R.id.txt_statusValue);
    }

    public void getReport() {
        Bundle bundle = getIntent().getExtras();
        String reportID = bundle.getString("reportID");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting report");
        progressDialog.setMessage("Retrieving details...");
        progressDialog.show();

        final StorageReference pathReference =   FirebaseStorage.getInstance().getReference().child("images/reports/" + reportID);

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = databaseRef.child("reports").child(reportID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Report report = dataSnapshot.getValue(Report.class);
                vehicleNo.setText(report.getVehicleNo());
                licenseNo.setText(report.getLicenseNo());
                reason.setText(report.getReason());
                fine.setText(String.valueOf(report.getFine()));
                description.setText(report.getDescription());
                time.setText(SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(report.getDatetime()));
                date.setText(SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.FRENCH).format(report.getDatetime()));

                if(report.isFinePaid()) {
                    status.setText("Paid");
                    statusIndicator.setBackgroundResource(R.drawable.circle_green);
                }
                else {
                    status.setText("Pending");
                    statusIndicator.setBackgroundResource(R.drawable.circle_yellow);
                }

                Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(pathReference).into(photo);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
