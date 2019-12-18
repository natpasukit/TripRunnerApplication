package uk.ac.shef.oak.com6510.Views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import uk.ac.shef.oak.com6510.R;

public class TripGalleryAdapter extends RecyclerView.Adapter<TripGalleryAdapter.ViewHolder> {
    private String[] tripNameList;
    private Context context;
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

    public TripGalleryAdapter(String[] tripNameList) {
        this.tripNameList = tripNameList;
    }

    // Create new view by layout manager
    @Override
    public TripGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate layout
        View view = inflater.inflate(R.layout.trip_gallery_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tripGalleryName.setText(tripNameList[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, ShowImageActivity.class);
//                intent.putExtra("Tr", tripNameList);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tripNameList.length;
    }


}
