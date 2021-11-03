package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddService extends AppCompatActivity {
   // TextView usernameView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        String username = getIntent().getStringExtra("username");

        this.setTitle("Lisää Huolto");
     //   usernameView = findViewById(R.id.textViewUser);
        bottomNavigationView = findViewById(R.id.bottomNavigationview);
        bottomNavigationView.setSelectedItemId(R.id.addService);
     //   usernameView.setText(username);

        // Tähän pitää nyt sitten tehdä explicit intent tms että menee tiedot perille
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.loggedIn:
                        startActivity(new Intent(getApplicationContext(),LoggedIn.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.addService:
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