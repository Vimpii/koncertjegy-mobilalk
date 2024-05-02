package hu.inf.koncertjegy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getName();
    private static final String PREFS_KEY = MainActivity.class.getPackage().toString();
    EditText userNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secretKey != 99) {
            finish();
        }

        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.userEmailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordAgainEditText = findViewById(R.id.passwordAgainEditText);

        preferences = getSharedPreferences(PREFS_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userNameEditText.setText(userName);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);

    }

    public void register(View view) {
        String userName = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
            Log.e(TAG, "All fields are required!");
            return;
        }

        if (!password.equals(passwordAgain)) {
            Log.e(TAG, "Passwords do not match!");
            return;
        }

        Log.i(TAG, "User name: " + userName + ", Email: " + email + ", Password: " + password + ", Password again: " + passwordAgain);

    }

    public void back(View view) {
        finish();
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userName = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userNameEditText.setText(userName);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}