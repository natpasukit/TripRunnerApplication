package uk.ac.shef.oak.com6510.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.TripGalleryAdapter;


/**
 * RowerActivity
 * Manage activity in row route path view for user
 */
public class RowerActivity extends AppCompatActivity {
    private static final int GRID_COLUMN_NUMBER = 3;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Recycle view creation
        recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);

        // Linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter for recycler view
        adapter = new TripGalleryAdapter(this.getApplication(), this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Create option menu with a recycler view
     *
     * @param menu Menu
     * @return boolean result of operation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Check option from menu selector
     *
     * @param item menu item
     * @return boolean result of operation
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            sortOptionsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * sortOptionDialog
     * Manage and handle listener of every click in option using dialog builder
     */
    private void sortOptionsDialog() {

        String options[] = {"By Ascend Date order", "By Descend Date order", "By Grid image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Show By:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(which);
                        switch (which) {
                            case 0:

                                break;
                            case 1:

                                break;
                            case 2:
                                Intent intent = new Intent(RowerActivity.this, GalleryActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                break;

                        }
                    }
                }).create().show();
    }
}