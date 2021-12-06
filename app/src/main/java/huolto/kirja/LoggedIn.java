package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoggedIn extends AppCompatActivity {
    TextView tvUsername;
    BottomNavigationView bottomNavigationView;
    // for creating variables for list view
    private ListView list;
    // new array list
    ArrayList<String> ArrayList;
    // database reference variable
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
    List<String> NameList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // initializing variables for listview
        list = findViewById(R.id.list);

        // initializing new array list
        ArrayList = new ArrayList<String>();

        initializeListView();

        this.setTitle("Koti");

        tvUsername = findViewById(R.id.textViewUsername);
        String username = getIntent().getStringExtra("username");
        tvUsername.setText(username);

        // debuggia
        String search = "user/" + username;
        reference = FirebaseDatabase.getInstance().getReference().child(search);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.loggedIn);

        // Tähän rakennettu nyt explicit intent jotta username menee aina uuteen activityyn
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.addService:
                        Intent explicit = new Intent(LoggedIn.this, AddVehicle.class);
                        explicit.putExtra("username",username);
                        startActivity(explicit);
                        return true;
                    case R.id.loggedIn:
                        return true;
                    case R.id.logOut:
                        logoutAlert();
                        //return true;
                }
                return false;
            }
        });
    }

    // Function for displaying the AlertDialog when logout
    private void logoutAlert() {
        new AlertDialog.Builder(this)
                // Message to be displayed on the alert
                .setMessage("Haluatko varmasti poistua?")
                // Positive button onclick event
                .setPositiveButton("Kirjaudu Ulos", (dialogInterface, i) -> {
                    this.finishAffinity();
                })
                // Negative button onclick event
                .setNegativeButton("Peruuta", (dialogInterface, i) -> {

                })
                .show();
    }

    public void initializeListView() {
        // creating new array adapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoggedIn.this, android.R.layout.simple_dropdown_item_1line, ArrayList);
        String username = getIntent().getStringExtra("username");
        String search = "user/" + username + "/vehicles";

        // getting the firebase reference
        reference = FirebaseDatabase.getInstance().getReference().child(search);

        //calling method for add child event
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added to our database
                Iterable <DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                for(DataSnapshot d: dataSnapshotIterable) {
                    Iterable <DataSnapshot> debug = d.getChildren();

                }
                ArrayList.add(snapshot.getKey());
                String vehiclename = snapshot.getKey();
                NameList.add(vehiclename);
                /* String vehicle = snapshot.child("vehicles").child("-MoIYfnOGcuJASSDzrrS").getValue(String.class);
                System.out.println("***********************************");
                System.out.println(vehicle);
                System.out.println("***********************************");

                */
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren();

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // setting an adapter to our list view
        list.setAdapter(adapter);
        // onclickevent listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent explicit = new Intent(LoggedIn.this, VehicleData.class);
                System.out.println(NameList.get(position));
                explicit.putExtra("username",username);
                explicit.putExtra("vehicleName", NameList.get(position));
                startActivity(explicit);
            }
        });
    }
}