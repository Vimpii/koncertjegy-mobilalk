package hu.inf.koncertjegy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private ArrayList<Ticket> mTicketList;
    private Context mContext;

    public TicketAdapter(Context context, ArrayList<Ticket> ticketList) {
        this.mContext = context;
        this.mTicketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ticket_item, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket currentTicket = mTicketList.get(position);
        holder.bindTo(currentTicket);

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return mTicketList.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView mPerformerTextView;
        private TextView mLocationTextView;
        private TextView mDateTextView;
        private TextView mQuantityTextView;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);

            mPerformerTextView = itemView.findViewById(R.id.performer_text_view);
            mLocationTextView = itemView.findViewById(R.id.location_text_view);
            mDateTextView = itemView.findViewById(R.id.date_text_view);
            mQuantityTextView = itemView.findViewById(R.id.quantity_text_view);
        }

        public void bindTo(Ticket ticket) {
            mPerformerTextView.setText(ticket.getPerformer());
            mLocationTextView.setText(ticket.getLocation());
            mDateTextView.setText(ticket.getDate());
            mQuantityTextView.setText(String.valueOf(ticket.getQuantity()));
        }
    }
}
