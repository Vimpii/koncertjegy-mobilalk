package hu.inf.koncertjegy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ConcertAdapter extends RecyclerView.Adapter<ConcertAdapter.ViewHolder> implements Filterable {
    private ArrayList<Concert> mConcertsData;
    private ArrayList<Concert> mConcertsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    public ConcertAdapter(Context context, ArrayList<Concert> concertsData) {
        this.mConcertsData = concertsData;
        this.mContext = context;
        mConcertsDataAll = concertsData;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ConcertAdapter.ViewHolder holder, int position) {
        Concert currentConcert = mConcertsData.get(position);

        holder.bindTo(currentConcert);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return mConcertsData.size();
    }

    @Override
    public Filter getFilter() {
        return concertFilter;
    }

    private Filter concertFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Concert> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.count = mConcertsDataAll.size();
                results.values = mConcertsDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Concert item : mConcertsDataAll) {
                    if (item.getPerformer().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mConcertsData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mPerformer;
        private TextView mLocation;
        private TextView mDate;
        private NumberPicker mQuantity;
        private RatingBar mRatedInfo;
        private ImageView mConcertImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mPerformer = itemView.findViewById(R.id.performer);
            mLocation = itemView.findViewById(R.id.location);
            mDate = itemView.findViewById(R.id.date);
            mQuantity = itemView.findViewById(R.id.quantity);
            mRatedInfo = itemView.findViewById(R.id.ratedInfo);
            mConcertImage = itemView.findViewById(R.id.concertImage);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ConcertAdapter", "Add to cart clicked");
                }
            });

        }

        public void bindTo(Concert currentConcert) {

            mPerformer.setText("Performer: " + currentConcert.getPerformer());
            mLocation.setText("Location: " + currentConcert.getLocation());
            mDate.setText("Date: " + currentConcert.getDate());
            mQuantity.setMinValue(1);
            mQuantity.setMaxValue(10);
            mQuantity.setValue(currentConcert.getQuantity());
            mRatedInfo.setRating(currentConcert.getRatedInfo());
            mConcertImage.setImageResource(currentConcert.getImageResource());

            Glide.with(mContext).load(currentConcert.getImageResource()).into(mConcertImage);
        }
    }

}
