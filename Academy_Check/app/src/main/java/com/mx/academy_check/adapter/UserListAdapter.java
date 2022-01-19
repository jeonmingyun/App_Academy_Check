package com.mx.academy_check.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;
import com.mx.academy_check.util.DateFormatUtil;
import com.mx.academy_check.util.PreferenceManager;
import com.mx.academy_check.vo.UserVO;
import com.ramotion.foldingcell.FoldingCell;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

import androidx.annotation.NonNull;

public class UserListAdapter extends ArrayAdapter<UserVO> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private Context mContext;
    private ArrayList<UserVO> userList;
    private OnBtnClickListener mListener;

    public interface OnBtnClickListener {
        void onPopUpBtnClicked(View v, int position);
        void onStartBtnClicked(View v, int position);
        void onEndBtnClicked(View v, int position);
    }

    public void setOnBtnClickListener(OnBtnClickListener mListener) {
        this.mListener = mListener;
    }

    public UserListAdapter(Context context, ArrayList<UserVO> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.userList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        UserVO item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder holder;
        if (cell == null) {

            holder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            holder.title_name = cell.findViewById(R.id.name);
            holder.title_gender = cell.findViewById(R.id.gender);
            holder.title_code = cell.findViewById(R.id.code);
            holder.title_phone_num = cell.findViewById(R.id.phone_num);
            holder.title_start_time = cell.findViewById(R.id.today_start_time);
            holder.title_end_time = cell.findViewById(R.id.today_end_time);

            holder.content_name = cell.findViewById(R.id.selected_name);
            holder.content_gender = cell.findViewById(R.id.selected_gender);
            holder.content_code = cell.findViewById(R.id.selected_code);
            holder.content_phone_num = cell.findViewById(R.id.selected_phone_num);
            holder.content_start_time = cell.findViewById(R.id.start_time);
            holder.content_end_time = cell.findViewById(R.id.end_time);
            holder.content_popup_btn = cell.findViewById(R.id.selected_popup_btn);
            holder.content_start_check_btn = cell.findViewById(R.id.start_check_btn);
            holder.content_end_check_btn = cell.findViewById(R.id.end_check_btn);
            holder.check_btn_container = cell.findViewById(R.id.check_btn_container);
            cell.setTag(holder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            holder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        String TitleFormattedStartTime = "";
        String TitleFormattedEndTime = "";
        String login_class = PreferenceManager.getString(mContext, PreferenceManager.KEY_LOGIN_CLASS);

        try {
            if (userList.get(position).getStart_time() != null) {
                TitleFormattedStartTime = DateFormatUtil.formatChangeDateString(
                        userList.get(position).getStart_time(),
                        DateFormatUtil.DATE_FORMAT_6,
                        DateFormatUtil.DATE_FORMAT_2);
            }

            if (userList.get(position).getEnd_time() != null) {
                TitleFormattedEndTime = DateFormatUtil.formatChangeDateString(
                        userList.get(position).getEnd_time(),
                        DateFormatUtil.DATE_FORMAT_6,
                        DateFormatUtil.DATE_FORMAT_2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

//         bind data from selected element to view through view holder
        holder.title_name.setText(userList.get(position).getUser_name());
        holder.title_gender.setText(userList.get(position).getUser_gender());
        holder.title_code.setText(userList.get(position).getUser_code());
        holder.title_phone_num.setText(userList.get(position).getUser_phone_num());
        holder.title_start_time.setText(TitleFormattedStartTime);
        holder.title_end_time.setText(TitleFormattedEndTime);

        holder.content_name.setText(userList.get(position).getUser_name());
        holder.content_gender.setText(userList.get(position).getUser_gender());
        holder.content_code.setText(userList.get(position).getUser_code());
        holder.content_phone_num.setText(userList.get(position).getUser_phone_num());
        holder.content_start_time.setText(userList.get(position).getStart_time());
        holder.content_end_time.setText(userList.get(position).getEnd_time());

        Log.e("ddd", login_class);
        if (login_class.equals(Constants.USER_CLASS_PARENT)) {
            holder.check_btn_container.setVisibility(View.GONE);
        }

        holder.content_popup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onPopUpBtnClicked(v, position);
                }
            }
        });
        holder.content_start_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onStartBtnClicked(v, position);
                }
            }
        });
        holder.content_end_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onEndBtnClicked(v, position);
                }
            }
        });

        return cell;
    }

    public void updateItem(int position, UserVO item) {
        this.userList.set(position, item);
        this.notifyDataSetChanged();
    }

    public void updateAllItem(ArrayList<UserVO> itemList) {
        this.userList = itemList;
        this.notifyDataSetChanged();
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView title_name, title_gender, title_code, title_phone_num, title_start_time, title_end_time;
        TextView content_name, content_gender, content_code, content_phone_num, content_start_time, content_end_time;
        ImageView content_popup_btn;
        TextView content_start_check_btn, content_end_check_btn;
        View check_btn_container;
    }
}
