package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddService extends AppCompatActivity {
   // TextView usernameView;
    BottomNavigationView bottomNavigationView;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");

    //alapuolella editTextit ja buttonit
    EditText makeModel;
    EditText service;
    EditText kiloMeters;
    Button saveVehicle;
    Button saveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        String username = getIntent().getStringExtra("username");

        this.setTitle("Lisää Ajoneuvo");

        bottomNavigationView = findViewById(R.id.bottomNavigationview);
        bottomNavigationView.setSelectedItemId(R.id.addService);

        makeModel = findViewById(R.id.editTextVehicleName);
        saveVehicle = findViewById(R.id.buttonAddVehicle);



        // buttonien listenerit
        saveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String makModel = makeModel.getText().toString();
                VehicleInfo vehicleInfo = new VehicleInfo(makModel);
                String search = "user/" + username + "/vehicles";
                //reference.child(search).setValue(vehicleInfo);
                reference = FirebaseDatabase.getInstance().getReference().child(search);
                reference.child(vehicleInfo.getVehicleInfo()).child("huollot").setValue("");
                Toast.makeText(AddService.this,"Ajoneuvo Lisätty",Toast.LENGTH_LONG).show();
                makeModel.setText("");

            }
        });



        // Tähän rakennettu nyt explicit intent jotta username menee aina uuteen activityyn
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.loggedIn:
                        Intent explicit = new Intent(AddService.this, huolto.kirja.LoggedIn.class);
                        explicit.putExtra("username",username);
                        startActivity(explicit);
                        return true;
                    case R.id.addService:
                        return true;
                    case R.id.logOut:
                        Intent explicit2 = new Intent(AddService.this, huolto.kirja.Logout.class);
                        explicit2.putExtra("username",username);
                        startActivity(explicit2);
                        return true;

                }
                return false;
            }
        });


    }
}