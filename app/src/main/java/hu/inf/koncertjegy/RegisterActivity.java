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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getName();
    private static final String PREFS_KEY = MainActivity.class.getPackage().toString();
    EditText userNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

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

        emailEditText.setText(userName);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);

        mAuth = FirebaseAuth.getInstance();

    }

    public void register(View view) {
        String userName = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
            Log.e(TAG, "All fields are required!");
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.e(TAG, "Invalid email format!");
            Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Log.e(TAG, "Password is too short!");
            Toast.makeText(this, "Password is too short!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordAgain)) {
            Log.e(TAG, "Passwords do not match!");
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "User registered successfully!");

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Create a new user with email and username
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("username", userName);

                        // Add a new document with the user's UID
                        db.collection("Users")
                                .document(mAuth.getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                    // Update the Firebase user's profile with usernamew
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName)
                                            .build();

                                    mAuth.getCurrentUser().updateProfile(profileUpdates)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");

                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    intent.putExtra("email", email);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Log.e(TAG, "Email is already in use!");
                            Toast.makeText(this, "Email is already in use!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "User registration failed!");
                        }
                    }
                });
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

        emailEditText.setText(userName);
        passwordEditText.setText(password);
        passwordAgainEditText.setText(password);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}