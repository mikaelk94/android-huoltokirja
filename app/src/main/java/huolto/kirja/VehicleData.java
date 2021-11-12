package huolto.kirja;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VehicleData extends AppCompatActivity {
    EditText addService;
    EditText addKilometers;
    Button saveService;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_data);
        this.setTitle("VehicleData");

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
                reference.child(username).child("vehicles").child(vehiclename).child("huollot").push().setValue(serviceInfo);
                addKilometers.setText("");
                addService.setText("");
            }
        });

    }
}