package hu.inf.koncertjegy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ConcertListActivity extends AppCompatActivity {
    private static final String LOG_TAG = ConcertListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<Concert> mConcertList;
    private ConcertAdapter mAdapter;

    private int gridNumber = 1;
    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_concert_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            Log.d(LOG_TAG, "User is logged in: " + user.getEmail());
        } else {
            Log.d(LOG_TAG, "User is not logged in!");
        }

        mRecyclerView = findViewById(R.id.concertListRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mConcertList = new ArrayList<>();

        mAdapter = new ConcertAdapter(this, mConcertList, user);
        mRecyclerView.setAdapter(mAdapter);

        initalizeData();

    }

    private void initalizeData() {
        String[] concertList = getResources().getStringArray(R.array.performers);
        String[] concertLocation = getResources().getStringArray(R.array.locations);
        String[] concertDate = getResources().getStringArray(R.array.dates);
        int[] concertQuantity = getResources().getIntArray(R.array.quantities);
        TypedArray concertRatedInfo = getResources().obtainTypedArray(R.array.ratings);
        TypedArray concertImage = getResources().obtainTypedArray(R.array.images);

        mConcertList.clear();

        for (int i = 0; i < concertList.length; i++) {
            mConcertList.add(new Concert(concertList[i], concertLocation[i], concertDate[i], concertQuantity[i], concertRatedInfo.getFloat(i, 0.0f), concertImage.getResourceId(i, 0)));
        }

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.concert_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_bar) {
            gridNumber = 1;
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
            return true;
        } else if (id == R.id.view_selector) {
            gridNumber = 2;
            if (viewRow) {
                changeSpanCount(item, R.drawable.view_grid, 1);
            } else {
                changeSpanCount(item, R.drawable.view_row, 2);
            }
            return true;
        } else if (id == R.id.profile_button) {
            gridNumber = 3;
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.logout_button) {
            mAuth.signOut();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeSpanCount(MenuItem item, int drawable, int spanCount) {
        viewRow = !viewRow;
        item.setIcon(drawable);
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}