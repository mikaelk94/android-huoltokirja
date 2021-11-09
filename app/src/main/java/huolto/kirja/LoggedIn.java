package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoggedIn extends AppCompatActivity {
    TextView UsernameView;
    BottomNavigationView bottomNavigationView;
    // for creating variables for list view
    private ListView list;
    // new array list
    ArrayList<String> ArrayList;
    // database reference variable
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // initializing variables for listview
        list = findViewById(R.id.list);

        // initialazing new array list
        ArrayList = new ArrayList<String>();

        initializeListView();

        this.setTitle("Koti");

        UsernameView = findViewById(R.id.textViewUsername);
        String username = getIntent().getStringExtra("username");
        UsernameView.setText(username);

        bottomNavigationView = findViewById(R.id.bottomNavigationview);
        bottomNavigationView.setSelectedItemId(R.id.loggedIn);

        // Tähän pitää nyt sitten tehdä explicit intent tms että menee tiedot perille
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.addService:
                       /*startActivity(new Intent(getApplicationContext(),AddService.class));
                        overridePendingTransition(0,0);*/
                        // onnistuu myös näin expliciittisellä intentillä, mutta pitäisi tallentaa jotenkin koska aktiviteettia vaihdettaessa häviää username
                        Intent explicit = new Intent(LoggedIn.this, huolto.kirja.AddService.class);
                        explicit.putExtra("username",username);
                        startActivity(explicit);
                        return true;
                    case R.id.loggedIn:
                        return true;
                    case R.id.logOut:
                        startActivity(new Intent(getApplicationContext(),Logout.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });


    }

    public void initializeListView() {
        // creating new array adapter for listview
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, ArrayList);
        String username = getIntent().getStringExtra("username");
        String haku = "user/" + username;

        // getting the firebase reference
        reference = FirebaseDatabase.getInstance().getReference().child(haku);

        //calling method for add child event
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added to our database
                ArrayList.add(snapshot.getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                ArrayList.remove(snapshot.getValue(String.class));
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
    }


}