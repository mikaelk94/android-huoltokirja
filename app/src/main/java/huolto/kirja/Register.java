package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    Button btnCreate;
    EditText etUsername;
    EditText etPassword;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnCreate = findViewById(R.id.buttonLuoTunnus);
        etUsername = findViewById(R.id.editTextLuoTunnus);
        etPassword = findViewById(R.id.editTextLuoSalasana);

        this.setTitle("Luo Käyttäjä");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnCreate.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            UserInfo userinfo = new UserInfo(username, password);

            if(TextUtils.isEmpty(username)) {                        //tehdään errorit jos ei oo kentät täynnä
                etUsername.setError("Tunnus pitää olla");
                return;
            }

            if(TextUtils.isEmpty(password)) {
                etPassword.setError("Salasana pitää olla");
                return;
            }

            else
            {
                Query checkUser = myRef.orderByChild("username").equalTo(username);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String UserFromDB = snapshot.child(username).child("username").getValue(String.class);

                            if(UserFromDB.equals(username)){
                                Toast.makeText(Register.this,"Käyttäjänimi on jo olemassa",Toast.LENGTH_LONG).show();
                                etUsername.setText("");
                            }
                        }
                        else {
                            myRef.child(username).setValue(userinfo); // nyt voidaan luoda useita käyttäjiä uniikkien usernamejen kautta
                            Toast.makeText(Register.this,"Käyttäjä luotu",Toast.LENGTH_LONG).show();
                            etUsername.setText("");
                            etPassword.setText("");
                            Intent explicit = new Intent(Register.this, MainActivity.class);
                            startActivity(explicit);
                            finish();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
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