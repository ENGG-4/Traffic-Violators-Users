package violatorsusers.traffic.com.trafficviolatorsusers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewBookmark extends AppCompatActivity {

    List<String> vehicleList,toDelete;
    ArrayAdapter<String> adapter;
    ListView listview;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bookmark);

        final EditText vehicleNumber = (EditText) findViewById(R.id.veh_number);
        ImageButton addBookmark = (ImageButton) findViewById(R.id.add_bookmark_button);

        addBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(vehicleNumber.getText().toString());
                vehicleNumber.setText("");
            }
        });

        emptyText = (TextView) findViewById(R.id.empty_view);
        listview = (ListView) findViewById(R.id.list_bookmarks);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
                if (checked) {
                    toDelete.add(adapter.getItem(position));
                } else {
                    toDelete.remove(adapter.getItem(position));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.menu_bookmark, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete:
                        for (String bookmark : toDelete) {
                            removeBookmark(bookmark);
                        }
                        toDelete.clear();
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                toDelete.clear();
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

        });

        vehicleList = new ArrayList<>();
        toDelete = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, vehicleList);
        listview.setAdapter(adapter);
        getBookmarks();
    }

    public void removeBookmark(final String bookmark) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookmarks").child(FirebaseAuth.getInstance().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(postSnapshot.getValue().toString().toUpperCase().equals(bookmark)) {
                        postSnapshot.getRef().setValue(null);
                        vehicleList.remove(bookmark);
                        Toast.makeText(NewBookmark.this,"Bookmark removed",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter.notifyDataSetChanged();
    }

    public void saveData(String vehicleNo) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("bookmarks");
        String bookmarkID = dbRef.push().getKey();
        dbRef.child(FirebaseAuth.getInstance().getUid()).child(bookmarkID).setValue(vehicleNo.toUpperCase());
        Toast.makeText(this,"Bookmark added",Toast.LENGTH_LONG).show();
    }

    public void getBookmarks() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bookmarks").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vehicleList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    vehicleList.add(postSnapshot.getValue().toString().toUpperCase());
                }
                if(vehicleList.isEmpty()) {
                    listview.setVisibility(View.GONE);
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    listview.setVisibility(View.VISIBLE);
                    emptyText.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
