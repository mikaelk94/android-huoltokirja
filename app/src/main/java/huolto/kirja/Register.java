package huolto.kirja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                myRef.child(username).setValue(userinfo); // nyt voidaan luoda useita käyttäjiä uniikkien usernamejen kautta
            }

            Toast.makeText(Register.this,"Käyttäjä luotu",Toast.LENGTH_LONG).show();
            etUsername.setText("");
            etPassword.setText("");
            Intent explicit = new Intent(Register.this, MainActivity.class);
            startActivity(explicit);
        });
    }
}