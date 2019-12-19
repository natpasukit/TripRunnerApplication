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

import uk.ac.shef.oak.com6510.Utils.TimestampConverter;
import uk.ac.shef.oak.com6510.Views.ImageActivity;

/**
 * TripGalleryAdapter
 * Adapter to add customization to RecyclerView of row gallery with data from database using CursorAdapter
 * *Noted that there is no getView() to help on recyclerView because the base cursorAdapter already check the instance of recyclerView.
 */
public class TripGalleryAdapter extends RecyclerView.Adapter<TripGalleryAdapter.ViewHolder> {
    private Application application;
    private Context context;
    private GalleryRepository galleryRepository;
    private MapRepository mapRepository;
    private Cursor tripCursorList;
    private int rowIdColumn;
    private CursorAdapter cursorAdapter;
    private LayoutInflater inflater;

    /**
     * Create adapter add mutate view with recycler with layout manager
     *
     * @param application current application context
     * @param context     current context
     */
    public TripGalleryAdapter(Application application, Context context) {
        this.application = application;
        this.context = context;
        this.galleryRepository = new GalleryRepository(application);
        this.mapRepository = new MapRepository(application);
        // Load trip data cursor
        this.tripCursorList = this.mapRepository.getAllTripName();
        this.inflater = LayoutInflater.from(context);
        this.cursorAdapter = new CursorAdapter(context, this.tripCursorList, 0) {

            /**
             * newView will be call every time the recycler try to create view and initiate view.
             * @param context context of activity
             * @param cursor cursor of database from galleryRepository of this position.
             * @param parent parent viewGroup of this view
             * @return recycle view that already inflate
             */
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate layout
                View view = inflater.inflate(R.layout.trip_gallery_card, parent, false);
                return view;
            }

            /**
             * bindView will bind action and set variable of each view, this also bind intent onClick to each view.
             * @param view view to bind to.
             * @param context context of this activity.
             * @param cursor current cursor position of database.
             */
            @Override
            public void bindView(View view, final Context context, Cursor cursor) {
                // Get data from cursor
                final int tripId = cursor.getInt(cursor.getColumnIndex("_id"));
                String tripName = cursor.getString(cursor.getColumnIndex("tripName"));
                String tripDate = cursor.getString(cursor.getColumnIndex("tripEnd"));
                // Find view to settler
                TextView textView = (TextView) view.findViewById(R.id.tripGalleryName);
                TextView dateTextView = (TextView) view.findViewById(R.id.galleryTripDate);
                Button button = (Button) view.findViewById(R.id.tripGalleryButton);
                // Set view information
                textView.setText(tripName);
                dateTextView.setText(TimestampConverter.timeStampToDate(tripDate).toString());
                // Add listener intent to button
                button.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ImageActivity.class);
                                intent.putExtra("tripId", tripId);
                                context.startActivity(intent);
                            }
                        }
                );
            }
        };

    }


    /**
     * viewHolder to generate recyclerView model
     * Provide a reference to the views for each data item in viewHolder
     */
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

    /**
     * Get cursor for each ViewHolder to access database to create new view
     *
     * @param parent   ViewGroup, parent View group of this view
     * @param viewType Integer, type of view
     * @return view for view holder
     */
    @Override
    public TripGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.cursorAdapter.newView(this.context, this.cursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }

    /**
     * Replace the content of a view , this will be invoked by layout manager
     *
     * @param holder   ViewHolder of this view
     * @param position position for the data location in cursor
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.cursorAdapter.getCursor().moveToPosition(position);
        this.cursorAdapter.bindView(holder.itemView, this.context, this.cursorAdapter.getCursor());
    }

    /**
     * Return the size of the data set, this will be invoked by layout manager
     *
     * @return size of cursorAdapter
     */
    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }
}
