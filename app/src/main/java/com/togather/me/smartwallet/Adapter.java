package com.togather.me.smartwallet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    public Adapter(Context context, List<CashFlow> items) {
        this.mContext = context;
        mItems = new ArrayList<CashFlow>();
        mItems.addAll(items);
        this.mInflater = LayoutInflater.from(context);
        notifyDataSetChanged();
        System.out.println("Adapter");

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cashflowcard, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
         CashFlow temp = mItems.get(position);
        holder.time.setText(String.valueOf(temp.time_hours) + ": " + String.valueOf(temp.time_minutes));
        holder.desp.setText(temp.desp);
        holder.amt.setText("600");
        System.out.println("dfdgdfg");
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
        }

    }

}
