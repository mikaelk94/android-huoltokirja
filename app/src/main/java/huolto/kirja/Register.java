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

    Button luo;
    EditText nimi;
    EditText salasana;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("user");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        luo = findViewById(R.id.buttonLuoTunnus);
        nimi = findViewById(R.id.editTextLuoTunnus);
        salasana = findViewById(R.id.editTextLuoSalasana);

        luo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = nimi.getText().toString();
                String userpassword = salasana.getText().toString();

                UserInfo userinfo = new UserInfo(username, userpassword);

                if(TextUtils.isEmpty(username)) {                        //tehdään errorit jos ei oo kentät täynnä
                    nimi.setError("Tunnus pitää olla");
                    return;
                }

                if(TextUtils.isEmpty(userpassword)) {
                    salasana.setError("Salasana pitää olla");
                    return;
                }

                else
                {
                    myRef.child(username).setValue(userinfo); // nyt voidaan luoda useita käyttäjiä uniikkien usernamejen kautta
                }

                Toast.makeText(Register.this,"Käyttäjä luotu",Toast.LENGTH_LONG).show();
                nimi.setText("");
                salasana.setText("");
                Intent explicit = new Intent(Register.this, huolto.kirja.MainActivity.class);
                startActivity(explicit);
            }
        });
    }
}