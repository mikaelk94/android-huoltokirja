package huolto.kirja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Logout extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        this.setTitle("Kirjaudu Ulos");
        String username = getIntent().getStringExtra("username");

        bottomNavigationView = findViewById(R.id.bottomNavigationview);
        bottomNavigationView.setSelectedItemId(R.id.logOut);

        // Tähän rakennettu explicit intent jotta username menee aina uuteen activityyn
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.addService:
                        Intent explicit = new Intent(Logout.this, huolto.kirja.AddService.class);
                        explicit.putExtra("username",username);
                        startActivity(explicit);
                        return true;
                    case R.id.logOut:
                        return true;
                    case R.id.loggedIn:
                        Intent explicit2 = new Intent(Logout.this, huolto.kirja.LoggedIn.class);
                        explicit2.putExtra("username",username);
                        startActivity(explicit2);
                        return true;

                }
                return false;
            }
        });


    }
}