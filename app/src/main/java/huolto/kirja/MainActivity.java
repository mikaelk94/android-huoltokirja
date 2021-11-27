package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button btnCreateAccount;
    Button btnLogin;
    EditText etUserName;
    EditText etPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"Firebase yhteys muodostettu",Toast.LENGTH_LONG).show();

        btnCreateAccount = findViewById(R.id.buttonCreateAcc);
        btnLogin = findViewById(R.id.buttonKirjaudu);
        etUserName = findViewById(R.id.editTextKayttaja);
        etPassWord = findViewById(R.id.editTextSalasana);

        btnLogin.setOnClickListener(view -> {

            String userGiven = etUserName.getText().toString();
            String passGiven = etPassWord.getText().toString();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
            Query checkUser = reference.orderByChild("username").equalTo(userGiven);
            checkUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String passwordFromDB = snapshot.child(userGiven).child("userpassword").getValue(String.class);

                        if(passwordFromDB.equals(passGiven)){
                            String usernameFromDB = snapshot.child(userGiven).child("username").getValue(String.class);
                            Intent explicit = new Intent(MainActivity.this, LoggedIn.class);
                            explicit.putExtra("username",usernameFromDB);
                            startActivity(explicit);
                        }
                        else {
                            etPassWord.setError("Väärä salasana");
                        }
                    }
                    else {
                        etUserName.setError("Käyttäjää ei ole");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });


        btnCreateAccount.setOnClickListener(view -> {

            Intent explicit = new Intent(MainActivity.this, Register.class);
            startActivity(explicit);

        });
    }
}