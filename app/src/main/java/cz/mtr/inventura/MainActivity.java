package cz.mtr.inventura;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import cz.mtr.inventura.Preferences.Prefs;
import cz.mtr.inventura.listView.Item;
import cz.mtr.inventura.listView.ItemAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private ArrayList<Item> items = new ArrayList<>();
    private EditText inputEanEditText, inputLocationEditText;
    private boolean containsEan = false;
    private Prefs prefs;
    private int itemPosition;

    private Item selectedItem = new Item("default", 0, "default");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbarAndDrawer();
        setupRecyclerView();
        setupEditTexts();
        setInputListener();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        prefs = new Prefs(this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                new RecyclerViewSwipeDecorator.Builder(MainActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.baseline_delete_white_24dp).addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.RED))
                        .addSwipeLeftLabel("Smazat").setSwipeLeftLabelColor(ContextCompat.getColor(MainActivity.this, R.color.WHITE)).addSwipeRightActionIcon(R.drawable.baseline_edit_white_24dp).
                        addSwipeRightLabel("Editovat").addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.BLUE)).setSwipeRightLabelColor(ContextCompat.getColor(MainActivity.this, R.color.WHITE))
                        .create().decorate();


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                itemPosition = viewHolder.getAdapterPosition();

                Item item = items.get(itemPosition);
                selectedItem = item;
                if (direction == ItemTouchHelper.LEFT) {
                    items.remove(item);
                    mAdapter.notifyItemRemoved(itemPosition);
                    prefs.setItems(items);
                } else {
                    FragmentChangeDialog dialog = new FragmentChangeDialog();
                    dialog.show(getSupportFragmentManager(), "FragmentChangeDialog");
                    prefs.setItems(items);
                }

            }
        }).attachToRecyclerView(mRecyclerView);

        getWindow().setSoftInputMode(//
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }


    public Item getSelectedItem() {
        return selectedItem;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public ItemAdapter getAdapter() {
        return mAdapter;
    }


    private void setupToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ItemAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItems(items);
    }

    private void setupEditTexts() {
        inputEanEditText = findViewById(R.id.inputEan);
//        inputEanEditText.requestFocus();
        inputLocationEditText = findViewById(R.id.inputLocation);
        inputLocationEditText.requestFocus();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view Item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_delete) {
            handleDeleteButton();
        } else if (id == R.id.nav_export) {
            handleExportButton();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleDeleteButton() {
        if (!items.isEmpty()) {
            FragmentDeleteAllDialog dialog = new FragmentDeleteAllDialog();
            dialog.show(getSupportFragmentManager(), "FragmentDeleteAllDialog");
        } else {
            Toast.makeText(this, "Seznam je prázdný.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleExportButton() {
        new SaveOfflineFilesTask(this, items).execute();
    }


    // when enter key is pressed, value in inputEanTextview is added to listView and amount info is refreshed
    private void setInputListener() {
        inputEanEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyevent) {
                return handleEnterKey(keyCode, keyevent);
            }
        });
    }

    private boolean handleEnterKey(int keyCode, KeyEvent keyevent) {
        if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (inputLocationEditText.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Není zadaná lokace", Toast.LENGTH_SHORT).show();
                inputEanEditText.getText().clear();
            } else {
                addEan();
                hideKeyboard(inputEanEditText);
                mRecyclerView.smoothScrollToPosition(0);
            }

            return true;
        }
        return false;
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    private void addEan() {

        String ean = inputEanEditText.getText().toString();
        if (!ean.isEmpty()) {
            handleAddingEan(ean);
        }
    }


    private void handleAddingEan(String ean) {
        if (items.isEmpty()) {
            items.add(0, new Item(ean, 1, getLocation()));
        } else {
            iterateListForMatch(ean);
        }
        mAdapter.setItems(items);
        prefs.setItems(items);
        inputEanEditText.setText("");
    }

    private String getLocation() {
        String location = "";
        if (location == "") {
            location = inputLocationEditText.getText().toString();
        }
        return location;
    }


    private void iterateListForMatch(String ean) {
        searchListForItemByEan(ean);
        addNewItemIfNotFound(ean);
    }

    private void searchListForItemByEan(String ean) {
        for (int i = 0; i < items.size(); i++) {
            Item a = items.get(i);
            containsEan = false;
            if (ean.equalsIgnoreCase(a.getEan())) {
                increaseAmountIfFound(a);
                break;
            }
        }
    }

    private void increaseAmountIfFound(Item a) {
        incrementAmount(a);
        containsEan = true;
    }

    private void incrementAmount(Item a) {
        int amount = a.getAmount() + 1;
        String currentEan = a.getEan();
        items.remove(a);
        items.add(0, new Item(currentEan, amount, getLocation()));
    }

    private void addNewItemIfNotFound(String ean) {
        if (!containsEan) {
            items.add(0, new Item(ean, 1, getLocation()));
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        prefs.setItems(items);
    }


    @Override
    public void onStart() {
        super.onStart();
        askForPermission();
        items.clear();
        items.addAll(prefs.getItems());
        mAdapter.setItems(items);
    }

    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // you already have a permission
//                checkForDatabaseUpdate();
            } else {
                // asks for permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else { //you dont need to worry about these stuff below api level 23

        }
    }

    public void deleteItems() {
        items.clear();
        mAdapter.setItems(items);
        prefs.setItems(items);

    }

}
