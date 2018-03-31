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
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import cyecoders.clinicom.R;
import cyecoders.clinicom.activities.HospitalDetailActivity;
import cyecoders.clinicom.models.Hospital;

/**
 * Created by jay on 30/3/18.
 */

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {

    private List<Hospital> data;
    static Context mContext;


    public HospitalAdapter(List<Hospital> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView name, address, city, rating, call, direction;

        public MyViewHolder(View v) {
            super(v);

            mCardView = v.findViewById(R.id.hr_card);
            name = v.findViewById(R.id.hd_name);
            address = v.findViewById(R.id.hsr_address);
            city = v.findViewById(R.id.hsr_city);
            rating = v.findViewById(R.id.hsr_rateing);
            call = v.findViewById(R.id.hr_call);
            direction = v.findViewById(R.id.hr_direction);
        }
    }

    @Override
    public HospitalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_row_layout, parent, false);
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
        holder.address.setText(data.get(position).getAddress());
        holder.city.setText(data.get(position).getCity());
        holder.rating.setText(data.get(position).getStars() + "/5");

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent call_dev = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+data.get(position).getPhone()));

                if(Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 10);
                    }else {
                        mContext.startActivity(call_dev);
                    }
                }else {
                    mContext.startActivity(call_dev);
                }
            }
        });

        holder.direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = data.get(position).getLatitute();
                String longitude = data.get(position).getLongitute();
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(latitude), Float.valueOf(longitude));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mContext.startActivity(intent);
            }
        });

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), HospitalDetailActivity.class);
                intent.putExtra("name", data.get(position).getName());
                intent.putExtra("city", data.get(position).getCity());
                intent.putExtra("address", data.get(position).getAddress());
                intent.putExtra("rating", data.get(position).getStars());
                intent.putExtra("hid", data.get(position).getId());
                intent.putExtra("phone", data.get(position).getPhone());
                intent.putExtra("latitue", data.get(position).getLatitute());
                intent.putExtra("longitute", data.get(position).getLongitute());
                intent.putExtra("amneties", data.get(position).getAmneties());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
