package violatorsusers.traffic.com.trafficviolatorsusers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewBookmark extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bookmark);

        final EditText vehicleNumber = (EditText) findViewById(R.id.veh_number);
        Button addBookmark = (Button) findViewById(R.id.add_bookmark_button);

        addBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(vehicleNumber.getText().toString());
                vehicleNumber.setText("");
            }
        });
    }

    public void saveData(String vehicleNo) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("bookmarks");
        String bookmarkID = dbRef.push().getKey();
        dbRef.child(FirebaseAuth.getInstance().getUid()).child(bookmarkID).setValue(vehicleNo.toLowerCase());
        Toast.makeText(this,"Bookmark added successfully",Toast.LENGTH_LONG).show();
    }
}
