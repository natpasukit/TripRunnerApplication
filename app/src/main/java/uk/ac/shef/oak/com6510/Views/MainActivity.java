package uk.ac.shef.oak.com6510.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;

import uk.ac.shef.oak.com6510.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Set timeText in layout every seconds into current datetime.
         */
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView = (TextView) findViewById(R.id.timeText);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nhh:mm:ss a");
                                String dateString = sdf.format(date);
                                textView.setText(dateString);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    // @Override exception into interrupted
                }
            }
        };
        thread.start();

        /**
         * Set button listener to get trip text from tripName
         * If its not empty redirect to MapActivity
         */
        final Button button = (Button) findViewById(R.id.startTripButtonMain);
        final EditText tripNameInput = (EditText) findViewById(R.id.tripName);
        final TextView errorMessage = (TextView) findViewById(R.id.errorText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tripName = tripNameInput.getText().toString();
                if (tripName.length() > 0) {
                    errorMessage.setText("");
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    intent.putExtra("tripName", tripName);
                    startActivity(intent);
                    finish();
                } else {
                    errorMessage.setText(R.string.errorEmptyTripNameMain);
                }
            }
        });
        /**
         * Set button lister to go to imageActivity
         * @// TODO: Direct to gallery instead
         */
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.accessPhotoGallery);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To test to show latest image
                 Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                 startActivity(intent);
            }
        });
    }
}
