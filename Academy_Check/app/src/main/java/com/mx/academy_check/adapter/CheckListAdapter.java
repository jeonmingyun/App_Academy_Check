package com.mx.academy_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.academy_check.R;
import com.mx.academy_check.vo.CheckListVO;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

    private Context mContext;
    private List<CheckListVO> checkList;

    private OnItemClickListener mListener = null;

    public interface OnItemClickListener {
        void onItemClicked(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public CheckListAdapter(Context context, List<CheckListVO> checkList) {
        this.mContext = context;
        this.checkList = checkList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.checkDate.setText(checkList.get(position).getCheckListDate());
        holder.checkDay.setText(checkList.get(position).getCheckListDay());
        holder.startedTime.setText(checkList.get(position).getStart_time());
        holder.endedTime.setText(checkList.get(position).getEnd_time());
    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }

    public void addItem(List<CheckListVO> itemList) {
        checkList.addAll(itemList);
        this.notifyDataSetChanged();
    }

    public void updateItem(ArrayList<CheckListVO> itemList) {
        checkList = itemList;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView checkDate, checkDay, startedTime, endedTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkDate = itemView.findViewById(R.id.check_date);
            checkDay = itemView.findViewById(R.id.check_day);
            startedTime = itemView.findViewById(R.id.started_time);
            endedTime = itemView.findViewById(R.id.ended_time);

            /*click event*/
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if (pos != RecyclerView.NO_POSITION) {
//                        if (mListener != null) {
//                            mListener.onItemClicked(v, pos);
//                        }
//                    }
//                }
//            });
        }
    }

}
