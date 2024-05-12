package hu.inf.koncertjegy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String PREFS_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    EditText userName;
    EditText password;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = findViewById(R.id.editTextUserName);
        password = findViewById(R.id.editTextPassword);

        preferences = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

    }

    public void login(View view) {
        String userNameStr = userName.getText().toString();
        String passwordStr = password.getText().toString();

        if (userNameStr.isEmpty() || passwordStr.isEmpty()) {
            Log.e(TAG, "All fields are required!");
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userNameStr).matches()) {
            Log.e(TAG, "Invalid email format!");
            Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, "User name: " + userNameStr + ", Password: " + passwordStr);

        mAuth.signInWithEmailAndPassword(userNameStr, passwordStr)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Login successful");
                        // Start ConcertListActivity
                        Intent intent = new Intent(MainActivity.this, ConcertListActivity.class);
                        startActivity(intent);
                        finish(); // Optional: if you want MainActivity to be removed from the back stack
                    } else {
                        Log.e(TAG, "Login failed", task.getException());
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);

        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userName.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}