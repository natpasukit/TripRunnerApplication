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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.shef.oak.com6510.Databases.GalleryRepository;
import uk.ac.shef.oak.com6510.Databases.MapRepository;
import uk.ac.shef.oak.com6510.R;
import uk.ac.shef.oak.com6510.Views.ImageActivity;

public class GridGalleryAdapter extends RecyclerView.Adapter<GridGalleryAdapter.ViewHolder> {
    private Application application;
    private Context context;
    private GalleryRepository galleryRepository;
    private LayoutInflater inflater;
    private CursorAdapter cursorAdapter;
    private Cursor imageCursorList;

    public GridGalleryAdapter(final Application application, Context context) {
        this.application = application;
        this.context = context;
        this.galleryRepository = new GalleryRepository(application);
        // Load all picture information
        this.imageCursorList = this.galleryRepository.getAllPhotoInformation();
        this.inflater = LayoutInflater.from(this.context);
        this.cursorAdapter = new CursorAdapter(context, this.imageCursorList, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate layout
                View view = inflater.inflate(R.layout.grid_image_gallery_card, parent, false);
                return view;
            }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView photoDate;
        public ImageView photoImage;

        public ViewHolder(View view) {
            super(view);
            this.photoDate = (TextView) view.findViewById(R.id.recyclerGridTextId);
            this.photoImage = (ImageView) view.findViewById(R.id.recyclerGridImage);
        }
    }

    @Override
    public GridGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.cursorAdapter.newView(this.context, this.cursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.cursorAdapter.getCursor().moveToPosition(position);
        this.cursorAdapter.bindView(holder.itemView, this.context, this.cursorAdapter.getCursor());
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }
}


