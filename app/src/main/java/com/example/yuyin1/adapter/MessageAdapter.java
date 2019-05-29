package com.example.yuyin1.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuyin1.R;
import com.example.yuyin1.entity.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mList;

    public MessageAdapter(List<Message> mList){
        this.mList =mList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View messageView;
        TextView date;
        TextView name;
        TextView flightId;
        TextView goods;
        TextView amount;
        TextView boxer;
        TextView starter;
        TextView remark;
        ImageView way1;
        ImageView way2;
        ImageView way3;
        ImageView way4;
        ImageView way5;
        ImageView way6;
        ImageView way7;
        public ViewHolder(View view){
            super(view);
            messageView = view;
            date = (TextView)view.findViewById((R.id.date));
            name = (TextView)view.findViewById((R.id.name));
            flightId = (TextView)view.findViewById((R.id.flightId));
            goods = (TextView)view.findViewById((R.id.goods));
            amount = (TextView)view.findViewById((R.id.amount));
            boxer = (TextView)view.findViewById((R.id.boxer));
            starter = (TextView)view.findViewById((R.id.starter));
            remark = (TextView)view.findViewById((R.id.remark));
            way1 = (ImageView)view.findViewById(R.id.way1);
            way2 = (ImageView)view.findViewById(R.id.way2);
            way3 = (ImageView)view.findViewById(R.id.way3);
            way4 = (ImageView)view.findViewById(R.id.way4);
            way5 = (ImageView)view.findViewById(R.id.way5);
            way6 = (ImageView)view.findViewById(R.id.way6);
            way7 = (ImageView)view.findViewById(R.id.way7);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.messageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("提示");
                dialog.setMessage("确定要删除此项吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = holder.getAdapterPosition();
                        System.out.println(mList.size());
                        Message m = mList.get(position);
                        m.delete();
                        mList.remove(position);
                        notifyItemRemoved(position);


                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message m = mList.get(i);
        viewHolder.date.setText(m.getDate());
        viewHolder.name.setText(m.getName());
        viewHolder.flightId.setText(m.getFlightId());
        viewHolder.amount.setText(String.valueOf(m.getAmount()));
        viewHolder.boxer.setText(m.getBoxer());
        viewHolder.goods.setText(m.getGoods());
        viewHolder.starter.setText(m.getStarter());
        viewHolder.remark.setText(m.getRemark());


            if(m.getWay().equals("zancun")){
                viewHolder.way1.setImageResource(R.drawable.a2);
                viewHolder.way2.setImageResource(R.drawable.a1);
                viewHolder.way3.setImageResource(R.drawable.a1);
                viewHolder.way4.setImageResource(R.drawable.a1);
                viewHolder.way5.setImageResource(R.drawable.a1);
                viewHolder.way6.setImageResource(R.drawable.a1);
                viewHolder.way7.setImageResource(R.drawable.a1);
            }else if(m.getWay().equals("fangqi")){
                viewHolder.way1.setImageResource(R.drawable.a1);
                viewHolder.way2.setImageResource(R.drawable.a2);
                viewHolder.way3.setImageResource(R.drawable.a1);
                viewHolder.way4.setImageResource(R.drawable.a1);
                viewHolder.way5.setImageResource(R.drawable.a1);
                viewHolder.way6.setImageResource(R.drawable.a1);
                viewHolder.way7.setImageResource(R.drawable.a1);
            }else if(m.getWay().equals("tuihui")) {
                viewHolder.way1.setImageResource(R.drawable.a1);
                viewHolder.way2.setImageResource(R.drawable.a1);
                viewHolder.way3.setImageResource(R.drawable.a2);
                viewHolder.way4.setImageResource(R.drawable.a1);
                viewHolder.way5.setImageResource(R.drawable.a1);
                viewHolder.way6.setImageResource(R.drawable.a1);
                viewHolder.way7.setImageResource(R.drawable.a1);
            }else if(m.getWay().equals("tuoyun")){
                viewHolder.way1.setImageResource(R.drawable.a1);
                viewHolder.way2.setImageResource(R.drawable.a1);
                viewHolder.way3.setImageResource(R.drawable.a1);
                viewHolder.way4.setImageResource(R.drawable.a2);
                viewHolder.way5.setImageResource(R.drawable.a1);
                viewHolder.way6.setImageResource(R.drawable.a1);
                viewHolder.way7.setImageResource(R.drawable.a1);
            }else if(m.getWay().equals("yijiao")){
                viewHolder.way1.setImageResource(R.drawable.a1);
                viewHolder.way2.setImageResource(R.drawable.a1);
                viewHolder.way3.setImageResource(R.drawable.a1);
                viewHolder.way4.setImageResource(R.drawable.a1);
                viewHolder.way5.setImageResource(R.drawable.a2);
                viewHolder.way6.setImageResource(R.drawable.a1);
                viewHolder.way7.setImageResource(R.drawable.a1);
            }else if(m.getWay().equals("xiedai")){
                viewHolder.way1.setImageResource(R.drawable.a1);
                viewHolder.way2.setImageResource(R.drawable.a1);
                viewHolder.way3.setImageResource(R.drawable.a1);
                viewHolder.way4.setImageResource(R.drawable.a1);
                viewHolder.way5.setImageResource(R.drawable.a1);
                viewHolder.way6.setImageResource(R.drawable.a2);
                viewHolder.way7.setImageResource(R.drawable.a1);
            }else if(m.getWay().equals("xianliang")){
                viewHolder.way1.setImageResource(R.drawable.a1);
                viewHolder.way2.setImageResource(R.drawable.a1);
                viewHolder.way3.setImageResource(R.drawable.a1);
                viewHolder.way4.setImageResource(R.drawable.a1);
                viewHolder.way5.setImageResource(R.drawable.a1);
                viewHolder.way6.setImageResource(R.drawable.a1);
                viewHolder.way7.setImageResource(R.drawable.a2);
            }


    }

    @Override
    public int getItemCount() {
        return mList.size();

    }
}
