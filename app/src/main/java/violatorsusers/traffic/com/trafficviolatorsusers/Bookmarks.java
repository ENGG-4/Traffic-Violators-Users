package violatorsusers.traffic.com.trafficviolatorsusers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

public class Bookmarks extends AppCompatActivity {
    List<ReportListItem> reportList;
    List<String> vehicleList;
    RecyclerView listview;
    RecyclerViewAdapter reportAdapter;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        emptyText = (TextView) findViewById(R.id.empty_view);
        listview = (RecyclerView) findViewById(R.id.rv_reports);
        reportAdapter = new RecyclerViewAdapter(this,reportList);
        listview.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        listview.setLayoutManager(manager);
        listview.setItemAnimator(new DefaultItemAnimator());

        vehicleList = new ArrayList<>();
        getVehicleList();
        getReportList(vehicleList.get(0));
    }

    public void getVehicleList() {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = databaseRef.child("bookmarks").equalTo(FirebaseAuth.getInstance().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                   vehicleList.add(postSnapshot.getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getReportList(String VehicleNo){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        Query query = databaseRef.child("reports").orderByChild("vehicleNo").equalTo(VehicleNo);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    ;
                    reportList.add(item);
                }
                if(reportList.isEmpty()) {
                    listview.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    listview.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                    listview.setAdapter(reportAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int getItemBackground(String reason) {
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
}
