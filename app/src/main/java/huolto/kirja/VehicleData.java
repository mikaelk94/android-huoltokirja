package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class VehicleData extends AppCompatActivity {
    EditText addService;
    EditText addKilometers;
    Button saveService;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("user");

    // creating variables for listview
    private ListView lv;

    // new array list
    ArrayList<String> lvArrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_data);
        this.setTitle("VehicleData");

        // initializing variables for listview
        lv = findViewById(R.id.VehicleDataList);

        // initializing array list
        lvArrayList = new ArrayList<String>();

        initializeListView();

        String username = getIntent().getStringExtra("username");
        String vehiclename = getIntent().getStringExtra("vehicleName");
      //  System.out.println("päästiin lopultakin tänne gg ez 4h");

        addService = findViewById(R.id.editTextAddVService);
        addKilometers = findViewById(R.id.editTextAddVKilometers);
        saveService = findViewById(R.id.buttonAddVService);


        saveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kilometers = addKilometers.getText().toString();
                String service = addService.getText().toString();
                ServiceInfo serviceInfo = new ServiceInfo(service,kilometers);
                reference.child(username).child("vehicles").child(vehiclename).child("services").push().setValue(serviceInfo);
                addKilometers.setText("");
                addService.setText("");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu3, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteThisVehicle:
                String username = getIntent().getStringExtra("username");
                String vehiclename = getIntent().getStringExtra("vehicleName");
                reference.child(username).child("vehicles").child(vehiclename).removeValue();
            case R.id.PrintThisVehicle:
                

        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeListView(){
        // new array adapter for listview
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lvArrayList);
        String username = getIntent().getStringExtra("username");
        String vehiclename = getIntent().getStringExtra("vehicleName");
        String search = "user/" + username + "/" + "vehicles/" + vehiclename;

        // getting database reference
        reference2 = FirebaseDatabase.getInstance().getReference().child(search);
        // adding child event listener
        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Iterable <DataSnapshot> dataSnapshotIterable = snapshot.getChildren();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String kilometers = postSnapshot.child("kilometers").getValue(String.class);
                    String service = postSnapshot.child("service").getValue(String.class);
                    lvArrayList.add(service + " " + kilometers + " km");
                    //lvArrayList.add(service);

                    //konsoliin tulostus debugaamista varten
                    //Log.d("TAG", "Values: " + time);
                }


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lv.setAdapter(adapter);
    }
}