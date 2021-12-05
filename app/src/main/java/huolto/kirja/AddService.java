package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddService extends AppCompatActivity {

    String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("user");

        String username = getIntent().getStringExtra("username");
        String vehicleName = getIntent().getStringExtra("vehicleName");

        this.setTitle("Uusi huolto");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditText etAddService;
        EditText etAddKilometers;
        EditText etAddDate;
        EditText etAddInformation;
        Button btnAddService;

        etAddService = findViewById(R.id.editTextAddService);
        etAddKilometers = findViewById(R.id.editTextAddKilometers);
        etAddInformation = findViewById(R.id.editTextAddInformation);
        etAddDate = findViewById(R.id.editTextAddDate);
        btnAddService = findViewById(R.id.buttonAddService);
        etAddDate.setText(date);

        btnAddService.setOnClickListener(v -> {
            String service = etAddService.getText().toString();
            String kilometers = etAddKilometers.getText().toString();
            String information = etAddInformation.getText().toString();
            String date = etAddDate.getText().toString();
            ServiceInfo serviceInfo = new ServiceInfo(service,kilometers,information,date);
            reference.child(username).child("vehicles").child(vehicleName).child("services").push().setValue(serviceInfo);
            etAddKilometers.setText("");
            etAddService.setText("");
            etAddInformation.setText("");
            etAddDate.setText("");

            Toast.makeText(this, "Lisätty huolto: " + service, Toast.LENGTH_LONG).show();

            Intent explicit = new Intent(AddService.this, LoggedIn.class);
            explicit.putExtra("username",username);
            startActivity(explicit);
        });
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}