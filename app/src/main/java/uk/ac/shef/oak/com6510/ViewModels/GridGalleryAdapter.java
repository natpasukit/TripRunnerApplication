package uk.ac.shef.oak.com6510.ViewModels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.shef.oak.com6510.Databases.GalleryRepository;
import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.Views.ImageActivity;

/**
 * GridGalleryAdapter
 * Adapter to add customization to RecyclerView of grid gallery with data from database using CursorAdapter
 * *Noted that there is no getView() to help on recyclerView because the base cursorAdapter already check the instance of recyclerView.
 */
public class GridGalleryAdapter extends RecyclerView.Adapter<GridGalleryAdapter.ViewHolder> {
    private Application application;
    private Context context;
    private GalleryRepository galleryRepository;
    private LayoutInflater inflater;
    private CursorAdapter cursorAdapter;
    private Cursor imageCursorList;

    /**
     * Create adapter add mutate view with recycler with layout manager
     *
     * @param application current application context
     * @param context     current context
     */
    public GridGalleryAdapter(final Application application, Context context) {
        this.application = application;
        this.context = context;
        this.galleryRepository = new GalleryRepository(application);
        // Load all picture information
        this.imageCursorList = this.galleryRepository.getAllPhotoInformation();
        this.inflater = LayoutInflater.from(this.context);
        this.cursorAdapter = new CursorAdapter(context, this.imageCursorList, 0) {

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
                View view = inflater.inflate(R.layout.grid_image_gallery_card, parent, false);
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
                final String imagePath = cursor.getString(cursor.getColumnIndex("photoFileDirectory"));
                String imageDate = cursor.getString(cursor.getColumnIndex("photoDate"));
                final int photoStopDispatcher = cursor.getInt(cursor.getColumnIndex("tripStopId"));
                final int photoTripIdDispatcher = cursor.getInt(cursor.getColumnIndex("tripId"));

                // Find view to settler
                TextView textView = (TextView) view.findViewById(R.id.recyclerGridTextId);
                ImageView imageView = (ImageView) view.findViewById(R.id.recyclerGridImage);
                // Decode scale image
                imageView.setImageBitmap(PhotoViewModel.getDecodedScaleImage(application, imageView, imagePath));
                imageView.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ImageActivity.class);
                        intent.putExtra("imagePath", imagePath);
                        intent.putExtra("tripStopId", photoStopDispatcher);
                        intent.putExtra("tripId", photoTripIdDispatcher);
                        context.startActivity(intent);
                    }
                });
            }
        };
    }

    /**
     * viewHolder to generate recyclerView model
     * Provide a reference to the views for each data item in viewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView photoDate;
        public ImageView photoImage;

        public ViewHolder(View view) {
            super(view);
            this.photoDate = (TextView) view.findViewById(R.id.recyclerGridTextId);
            this.photoImage = (ImageView) view.findViewById(R.id.recyclerGridImage);
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
    @NonNull
    public GridGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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


