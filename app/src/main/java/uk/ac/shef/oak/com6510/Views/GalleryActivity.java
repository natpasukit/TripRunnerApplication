package uk.ac.shef.oak.com6510.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.GridGalleryAdapter;

public class GalleryActivity extends AppCompatActivity {

    private static final int GRID_COLUMN_NUMBER = 3;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private int sortOrder = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Recycle view creation
        recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);

        // Linear layout manager
        layoutManager = new GridLayoutManager(this.getApplication(), GRID_COLUMN_NUMBER);
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter for recycler view
        RecyclerView.Adapter adapter = new GridGalleryAdapter(getApplication(), this, sortOrder);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            System.out.println(id);
            sortOptionsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortOptionsDialog() {

        String options[] = {"By Ascend Date order", "By Descend Date order", "By Trip route"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Show By:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(which);
                        switch (which) {
                            case 0:
                                if (sortOrder != 1) {
                                    setSortOrder(1);
                                    setNewAdapter();
                                }
                                break;
                            case 1:
                                if (sortOrder != -1) {
                                    setSortOrder(-1);
                                    setNewAdapter();
                                }
                                break;
                            case 2:
                                Intent intent = new Intent(GalleryActivity.this, RowerActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                break;

                        }
                    }
                }).create().show();
    }

    /**
     * Set new adapter data to current sortingOrder
     */
    public void setNewAdapter() {
        this.adapter = new GridGalleryAdapter(this.getApplication(), this, sortOrder);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Set sortOrder to target integer
     *
     * @param sortOrder Integer , use 1 for ascending else descending
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}