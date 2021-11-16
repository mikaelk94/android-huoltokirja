package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    Button buttonCreate;
    Button buttonLogin;
    EditText accountName;
    EditText passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this,"Firebase yhteys muodostettu",Toast.LENGTH_LONG).show();

        buttonCreate = findViewById(R.id.buttonCreateAcc);
        buttonLogin = findViewById(R.id.buttonKirjaudu);
        accountName = findViewById(R.id.editTextKayttaja);
        passWord = findViewById(R.id.editTextSalasana);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userGiven = accountName.getText().toString();
                String passGiven = passWord.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
                Query checkUser = reference.orderByChild("username").equalTo(userGiven);
                checkUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String passwordFromDB = snapshot.child(userGiven).child("userpassword").getValue(String.class);

                            if(passwordFromDB.equals(passGiven)){
                                String usernameFromDB = snapshot.child(userGiven).child("username").getValue(String.class);
                                Intent explicit = new Intent(MainActivity.this, huolto.kirja.LoggedIn.class);
                                explicit.putExtra("username",usernameFromDB);
                                startActivity(explicit);
                            }
                            else {
                                passWord.setError("Väärä salasana");
                            }
                        }
                        else {
                            accountName.setError("Käyttäjää ei ole");
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent explicit = new Intent(MainActivity.this, huolto.kirja.Register.class);
                startActivity(explicit);

            }
        });
    }
}