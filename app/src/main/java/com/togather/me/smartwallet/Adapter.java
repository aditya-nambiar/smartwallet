package com.togather.me.smartwallet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditya on 3/11/16.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ItemHolder> {
    Context mContext;
    List<CashFlow> mItems;
    LayoutInflater mInflater;
    private Edit edit_dialog;
//    private Edit2 edit_dialog2;
    private InterfaceUtils listener;
    ImageButton button;

    public Adapter(Context context, List<CashFlow> items ) {
        this.mContext = context;
        mItems = new ArrayList<CashFlow>();
        mItems.addAll(items);
        this.mInflater = LayoutInflater.from(context);
        notifyDataSetChanged();
        System.out.println("Adapter");


    }

//    public void setItems(List<CashFlow> items) {
//        mItems.clear();
//        mItems.addAll(items);
//
//        this.notifyDataSetChanged();
//    }



    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cashflowcard, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
         CashFlow temp = mItems.get(position);
        holder.time.setText(String.valueOf(temp.time_hours) + ": " + String.valueOf(temp.time_minutes) + temp.ampm);
        holder.desp.setText(temp.desp);
        holder.amt.setText(String.valueOf(temp.amt));
        ImageButton moneda = (ImageButton)  holder.itemView.findViewById(R.id.locButton);
        moneda.setTag(position); //For passing the list item index
        final int pos = position;
        moneda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CashFlow temp = mItems.get(pos);
                Intent intent = new Intent(mContext, GoogleMapActivity.class);
                intent.putExtra("longitude", temp.longitude);
                intent.putExtra("latitude", temp.latitude);
                mContext.startActivity(intent);
            }
        });
    }

    public CashFlow getItem(int position) {
        return mItems.get(position);
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time;
        TextView desp;
        TextView amt;



        public ItemHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.timing);
            desp = (TextView) itemView.findViewById(R.id.desp);
            amt = (TextView) itemView.findViewById(R.id.amt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            CashFlow item = getItem(getAdapterPosition());
            edit_dialog = new Edit(mContext,getAdapterPosition() );
            edit_dialog.setCancelable(false);
            edit_dialog.setCanceledOnTouchOutside(false);
            edit_dialog.setCashFlow(item, Adapter.this);
            edit_dialog.show();
            Window window = edit_dialog.getWindow();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getMetrics(displaymetrics);
            int height = (int) (displaymetrics.heightPixels * 0.8);

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);

        }

    }

}
