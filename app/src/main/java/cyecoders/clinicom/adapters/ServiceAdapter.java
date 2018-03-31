package cyecoders.clinicom.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import cyecoders.clinicom.R;
import cyecoders.clinicom.activities.HospitalDetailActivity;
import cyecoders.clinicom.activities.SchemeActivity;
import cyecoders.clinicom.models.Hospital;
import cyecoders.clinicom.models.Services;

/**
 * Created by jay on 30/3/18.
 */

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private List<Services> data;
    static Context mContext;


    public ServiceAdapter(List<Services> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView name, detail, price, stars, numberOfVoters;
        public ImageButton infoButton;

        public MyViewHolder(View v) {
            super(v);

            mCardView = v.findViewById(R.id.hr_card);
            name = v.findViewById(R.id.hd_name);
            detail   = v.findViewById(R.id.hsr_address);
            price = v.findViewById(R.id.hsr_city);
            stars = v.findViewById(R.id.hsr_rateing);
            numberOfVoters = v.findViewById(R.id.hsr_number_of_voter);
            infoButton = v.findViewById(R.id.infoButton);
        }
    }

    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_services_row_layout, parent, false);
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
        holder.detail.setText(data.get(position).getDetail());
        holder.price.setText(data.get(position).getPrice());
        holder.stars.setText(data.get(position).getStars() + "/5");
        holder.numberOfVoters.setText(data.get(position).getNumberOfVoters());

        try{
            if(data.get(position).getPrice().toLowerCase().contains("rs."))
            holder.infoButton.setVisibility(View.VISIBLE);
        }catch (Exception e){
            holder.infoButton.setVisibility(View.GONE);
        }

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, SchemeActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
