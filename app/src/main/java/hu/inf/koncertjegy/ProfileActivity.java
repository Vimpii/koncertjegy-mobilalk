package hu.inf.koncertjegy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private EditText usernameEditText;
    private TextView emailTextView;
    private RecyclerView mTicketsRecyclerView;
    private ArrayList<Ticket> mTicketList;
    private TicketAdapter mTicketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        usernameEditText = findViewById(R.id.username_edit_text);
        emailTextView = findViewById(R.id.email_text_view);
        Button saveButton = findViewById(R.id.save_button);

        if (user != null) {
            db.collection("Users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                usernameEditText.setText(document.getString("username"));
                                emailTextView.setText(user.getEmail());
                            }
                        }
                    });
        }

        saveButton.setOnClickListener(v -> {
            if (user != null) {
                String newUsername = usernameEditText.getText().toString();
                db.collection("Users").document(user.getUid())
                        .update("username", newUsername)
                        .addOnSuccessListener(aVoid -> Toast.makeText(ProfileActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, "Error updating username", Toast.LENGTH_SHORT).show());
            }
        });

        Button backButton = findViewById(R.id.back_button);
        Button deleteAccountButton = findViewById(R.id.delete_account_button);

        backButton.setOnClickListener(v -> {
            finish();
        });

        deleteAccountButton.setOnClickListener(v -> {
            if (user != null) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // Delete user from Firestore
                            db.collection("Users").document(user.getUid())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Delete user from Firebase Authentication
                                        user.delete()
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                                        finish();
                                                    }
                                                });
                                    });
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        mTicketsRecyclerView = findViewById(R.id.tickets_recycler_view);
        mTicketsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTicketList = new ArrayList<>();
        mTicketAdapter = new TicketAdapter(this, mTicketList);
        mTicketsRecyclerView.setAdapter(mTicketAdapter);

        fetchTickets();

    }

    private void fetchTickets() {
        if (user != null) {
            db.collection("Tickets")
                    .whereEqualTo("userId", user.getEmail())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String performer = document.getString("performer");
                                String location = document.getString("location");
                                String date = document.getString("date");
                                int quantity = document.getLong("quantity").intValue();
                                String image = document.getString("image");

                                mTicketList.add(new Ticket(performer, location, date, quantity, image));
                            }
                            mTicketAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("ProfileActivity", "Error getting documents.", task.getException());
                        }
                    });
        }

    }

}