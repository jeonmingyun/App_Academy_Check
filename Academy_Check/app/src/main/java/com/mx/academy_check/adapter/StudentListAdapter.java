package com.mx.academy_check.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mx.academy_check.R;
import com.mx.academy_check.vo.UserVO;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<UserVO> userList;

    private OnItemClickListener mListener = null;

    public interface OnItemClickListener{
        void onItemClicked(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public StudentListAdapter(Context context, ArrayList<UserVO> userList) {
        this.mContext = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_list_2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(userList.get(position).getUser_name());
        holder.gender.setText(userList.get(position).getUser_gender());
        holder.code.setText(userList.get(position).getUser_code());
        holder.phone_num.setText(userList.get(position).getUser_phone_num());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateItem(ArrayList<UserVO> itemList) {
        this.userList = itemList;
        this.notifyDataSetChanged();
    }

    public void addItem(UserVO item) {
        this.userList.add(item);
        this.notifyDataSetChanged();
    }

    public void deleteItem(String userCode) {
        int idx = -1;

        for(int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getUser_code().equals(userCode)) {
                idx = i;
                break;
            }
        }
        if( idx != -1) {
            this.userList.remove(idx);
            this.notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, gender, code, phone_num;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            gender = itemView.findViewById(R.id.gender);
            code = itemView.findViewById(R.id.code);
            phone_num = itemView.findViewById(R.id.phone_num);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClicked(v, pos);
                        }
                    }
                }
            });
        }
    }

}
