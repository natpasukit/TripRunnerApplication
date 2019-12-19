package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.shef.oak.com6510.Databases.GalleryRepository;
import uk.ac.shef.oak.com6510.Databases.MapRepository;
import uk.ac.shef.oak.com6510.R;

public class TripGalleryAdapter extends RecyclerView.Adapter<TripGalleryAdapter.ViewHolder> {
    private Application application;
    private Context context;
    private GalleryRepository galleryRepository;
    private MapRepository mapRepository;
    private Cursor tripCursorList;
    private int rowIdColumn;
    private CursorAdapter cursorAdapter;
    private int tripIdDispatcher;

    public TripGalleryAdapter(Application application, Context context) {
        this.application = application;
        this.context = context;
        this.galleryRepository = new GalleryRepository(application);
        this.mapRepository = new MapRepository(application);
        // Load trip data cursor
        this.tripCursorList = this.mapRepository.getAllTripName();
        this.cursorAdapter = new CursorAdapter(context, this.tripCursorList, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                // Inflate layout
                View view = inflater.inflate(R.layout.trip_gallery_card, parent, false);
                return view;

            }

            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                int tripId = cursor.getInt(cursor.getColumnIndex("_id"));
                tripIdDispatcher = tripId;
                String tripName = cursor.getString(cursor.getColumnIndex("tripName"));
                TextView textView = (TextView) view.findViewById(R.id.tripGalleryName);
                Button button = (Button) view.findViewById(R.id.tripGalleryButton);
                textView.setText(tripName);
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ShowImageActivity.class);
                                intent.putExtra("tripId", tripIdDispatcher);
                                context.startActivity(intent);
                            }
                        }
                );
            }
        };

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tripGalleryName;
        public Button tripGalleryButton;

        public ViewHolder(View view) {
            super(view);
            this.tripGalleryName = (TextView) view.findViewById(R.id.tripGalleryName);
            this.tripGalleryButton = (Button) view.findViewById(R.id.tripGalleryButton);
        }
    }

    // Create new view by layout manager
    @Override
    public TripGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.cursorAdapter.newView(this.context, this.cursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.cursorAdapter.getCursor().moveToPosition(position);
        this.cursorAdapter.bindView(holder.itemView, this.context, this.cursorAdapter.getCursor());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }
}
