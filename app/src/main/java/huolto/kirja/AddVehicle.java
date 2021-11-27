package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVehicle extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");

    // Määritellään widgetit
    BottomNavigationView bottomNavigationView;
    EditText etMakeModel;
    Button btnAddVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        String username = getIntent().getStringExtra("username");
        this.setTitle("Lisää Ajoneuvo");

        etMakeModel = findViewById(R.id.editTextVehicleName);
        btnAddVehicle = findViewById(R.id.buttonAddVehicle);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.addService);

        btnAddVehicle.setOnClickListener(v -> {
            String makModel = etMakeModel.getText().toString();
            VehicleInfo vehicleInfo = new VehicleInfo(makModel);
            String search = "user/" + username + "/vehicles";
            reference = FirebaseDatabase.getInstance().getReference().child(search);
            reference.child(vehicleInfo.getVehicleInfo()).child("services").setValue("");
            Toast.makeText(AddVehicle.this,"Ajoneuvo Lisätty",Toast.LENGTH_LONG).show();
            etMakeModel.setText("");
        });

        // Tähän rakennettu nyt explicit intent jotta username menee aina uuteen activityyn
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.loggedIn:
                        Intent explicit = new Intent(AddVehicle.this, huolto.kirja.LoggedIn.class);
                        explicit.putExtra("username",username);
                        startActivity(explicit);
                        return true;
                    case R.id.addService:
                        return true;
                    case R.id.logOut:
                        Intent explicit2 = new Intent(AddVehicle.this, huolto.kirja.Logout.class);
                        explicit2.putExtra("username",username);
                        startActivity(explicit2);
                        return true;
                }
                return false;
            }
        });
    }
}