package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoggedIn extends AppCompatActivity {
    TextView UsernameView;
    BottomNavigationView bottomNavigationView;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("vehicles");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        this.setTitle("Koti");

        UsernameView = findViewById(R.id.textViewUsername);
        String username = getIntent().getStringExtra("username");
        UsernameView.setText(username);

        bottomNavigationView = findViewById(R.id.bottomNavigationview);
        bottomNavigationView.setSelectedItemId(R.id.loggedIn);

        // Tähän pitää nyt sitten tehdä explicit intent tms että menee tiedot perille
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.addService:
                       /*startActivity(new Intent(getApplicationContext(),AddService.class));
                        overridePendingTransition(0,0);*/
                        // onnistuu myös näin expliciittisellä intentillä, mutta pitäisi tallentaa jotenkin koska aktiviteettia vaihdettaessa häviää username
                        Intent explicit = new Intent(LoggedIn.this, huolto.kirja.AddService.class);
                        explicit.putExtra("username",username);
                        startActivity(explicit);
                        return true;
                    case R.id.loggedIn:
                        return true;
                    case R.id.logOut:
                        startActivity(new Intent(getApplicationContext(),Logout.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });


    }


}