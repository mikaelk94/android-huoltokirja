package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class VehicleData extends AppCompatActivity {
    EditText etAddService;
    EditText etAddKilometers;
    Button btnAddService;
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
        lv = findViewById(R.id.listViewVehicleDataList);

        // initializing array list
        lvArrayList = new ArrayList<>();

        initializeListView();

        String username = getIntent().getStringExtra("username");
        String vehicleName = getIntent().getStringExtra("vehicleName");

        etAddService = findViewById(R.id.editTextAddService);
        etAddKilometers = findViewById(R.id.editTextAddKilometers);
        btnAddService = findViewById(R.id.buttonAddService);


        btnAddService.setOnClickListener(v -> {
            String kilometers = etAddKilometers.getText().toString();
            String service = etAddService.getText().toString();
            ServiceInfo serviceInfo = new ServiceInfo(service,kilometers);
            reference.child(username).child("vehicles").child(vehicleName).child("services").push().setValue(serviceInfo);
            etAddKilometers.setText("");
            etAddService.setText("");
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
                String vehicleName = getIntent().getStringExtra("vehicleName");
                reference.child(username).child("vehicles").child(vehicleName).removeValue();
            case R.id.PrintThisVehicle:
                writeToFile("test");
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeToFile(String data) {
        Context context = this.getApplicationContext();
        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path,"data.txt");
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes(StandardCharsets.UTF_8));
            stream.close();
            Toast.makeText(this,"Saved to: " + getExternalFilesDir(null), Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(this, "File write failed: " + e.toString(), Toast.LENGTH_LONG).show();
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void initializeListView(){
        // new array adapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, lvArrayList);
        String username = getIntent().getStringExtra("username");
        String vehicleName = getIntent().getStringExtra("vehicleName");
        String search = "user/" + username + "/" + "vehicles/" + vehicleName;

        // getting database reference
        reference2 = FirebaseDatabase.getInstance().getReference().child(search);

        // adding child event listener
        reference2.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String kilometers = postSnapshot.child("kilometers").getValue(String.class);
                    String service = postSnapshot.child("service").getValue(String.class);
                    lvArrayList.add(service + " " + kilometers + " km");
                    // for debugging
                    // log.TAG", "Values: " + time);
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