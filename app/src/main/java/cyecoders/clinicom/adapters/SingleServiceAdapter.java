package cyecoders.clinicom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cyecoders.clinicom.R;
import cyecoders.clinicom.models.Services;

/**
 * Created by jay on 31/3/18.
 */

public class SingleServiceAdapter extends RecyclerView.Adapter<SingleServiceAdapter.MyViewHolder> {

    private List<Services> data;
    static Context mContext;


    public SingleServiceAdapter(List<Services> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView name;

        public MyViewHolder(View v) {
            super(v);

            mCardView = v.findViewById(R.id.srl_card);
            name = v.findViewById(R.id.srl_name);
        }
    }

    @Override
    public SingleServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_services_row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if(position % 4 == 0) {
            holder.mCardView.setCardBackgroundColor(Color.rgb(45,86,107));
        }else if (position % 4 ==1) {
            holder.mCardView.setCardBackgroundColor(Color.rgb(134,30,106));
        }else if (position % 4 == 2) {
            holder.mCardView.setCardBackgroundColor(Color.rgb(34,117,133));
        }else if (position % 4 == 3) {
            holder.mCardView.setCardBackgroundColor(Color.rgb(170,22,86));
        }

        holder.name.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
