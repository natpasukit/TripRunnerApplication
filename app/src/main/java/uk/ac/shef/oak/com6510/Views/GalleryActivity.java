package uk.ac.shef.oak.com6510.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com6510.Models.PhotoModel;
import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.ViewModels.GridGalleryAdapter;
import uk.ac.shef.oak.com6510.ViewModels.TripGalleryAdapter;

public class GalleryActivity extends AppCompatActivity {

    private static final int GRID_COLUMN_NUMBER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

//        // Recycle view creation
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);
//
//        // Linear layout manager
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        // Set adapter for recycler view
//        RecyclerView.Adapter adapter = new TripGalleryAdapter(this.getApplication(),this);
//        recyclerView.setAdapter(adapter);


        //   Recycle view creation
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tripGalleryInformationRecycler);

        // Linear layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, GRID_COLUMN_NUMBER);
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter for recycler view
        RecyclerView.Adapter adapter = new GridGalleryAdapter(this.getApplication(), this);
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

            sortOptionsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private  void sortOptionsDialog(){

        String options[] = {"By Ascend","By Path","By List"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0)
                        {

                        }
                        else if (which ==1){

                        }else if (which ==2){

                        }
                    }
                }).create().show();
    }
}