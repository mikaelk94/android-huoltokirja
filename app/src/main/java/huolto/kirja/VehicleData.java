package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class VehicleData extends AppCompatActivity {
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
        this.setTitle("Huoltohistoria");

        String vehicleName = getIntent().getStringExtra("vehicleName");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initializing variables for listview
        lv = findViewById(R.id.listViewVehicleDataList);

        // initializing array list
        lvArrayList = new ArrayList<>();

        initializeListView();

        TextView tvVehicleName;
        tvVehicleName = findViewById(R.id.textViewVehicleName);
        tvVehicleName.setText(vehicleName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String username = getIntent().getStringExtra("username");
        String vehicleName = getIntent().getStringExtra("vehicleName");

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.AddNewService:
                Intent explicit = new Intent(VehicleData.this, AddService.class);
                explicit.putExtra("username", username);
                explicit.putExtra("vehicleName", vehicleName);
                startActivity(explicit);
                break;
            case R.id.deleteThisVehicle:
                removeVehicleAlert(username, vehicleName);
                break;
            case R.id.PrintThisVehicle:
                String vehicleToFile = getIntent().getStringExtra("vehicleName");
                String serviceToFile = "";
                serviceToFile = vehicleToFile + "\n";
                for (String index : lvArrayList) {
                    String serviceToprint = index;
                    serviceToFile = serviceToFile + "\n" + serviceToprint;
                }
                writeToFile(serviceToFile);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeToFile(String data) {
        Context context = this.getApplicationContext();
        String vehicleName = getIntent().getStringExtra("vehicleName");
        try {
            File path = context.getExternalFilesDir(null);
            File file = new File(path,vehicleName + "ServiceHistory.txt");
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(data.getBytes(StandardCharsets.UTF_8));
            stream.close();
            Toast.makeText(this,"Tallennettu: " + getExternalFilesDir(null), Toast.LENGTH_LONG).show();
        }
        catch (IOException e) {
            Toast.makeText(this, "Tiedoston luonti onnistui: " + e.toString(), Toast.LENGTH_LONG).show();
            Log.e("Exception", "Tiedoston luonti epäonnistui: " + e.toString());
        }
    }

    // Function for displaying the AlertDialog when removing a vehicle
    private void removeVehicleAlert(String username, String vehicleName) {
        new AlertDialog.Builder(this)
                // Message to be displayed on the alert
                .setMessage("Poista " + vehicleName + "?")
                // Positive button onclick event
                .setPositiveButton("Poista", (dialogInterface, i) -> {
                    reference.child(username).child("vehicles").child(vehicleName).removeValue();
                    Toast.makeText(this, vehicleName + " poistettu", Toast.LENGTH_LONG).show();
                    finish();
                })
                // Negative button onclick event
                .setNegativeButton("Peruuta", (dialogInterface, i) -> {

                })
                .show();
    }

    private void initializeListView(){
        // new array adapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lvArrayList);
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
                    String date = postSnapshot.child("date").getValue(String.class);
                    String information = postSnapshot.child("information").getValue(String.class);
                    String eol = System.getProperty("line.separator");
                    lvArrayList.add(service + " " + kilometers + " km" + "\n" +"Päivämäärä: " + date
                            + "\n" + "Lisätiedot: " + information + "\n");
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