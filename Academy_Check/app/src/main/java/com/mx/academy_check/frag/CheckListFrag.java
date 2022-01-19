package com.mx.academy_check.frag;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.academy_check.R;
import com.mx.academy_check.act.MainAct;
import com.mx.academy_check.adapter.CheckListAdapter;
import com.mx.academy_check.util.DateFormatUtil;
import com.mx.academy_check.vo.CheckListVO;
import com.mx.academy_check.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckListFrag extends BaseFrag implements View.OnClickListener {

    public static final String TAG = CheckListFrag.class.getSimpleName();

    private Context mContext;
    private View mView;
    private MainAct parentAct;
    private ArrayList<CheckListVO> checkList = new ArrayList<>();
    private UserVO selectedUserVo = new UserVO();
    private TextView name, gender, code, phoneNum, startTime, endTime;
    private TextView checkListDate;
    private RecyclerView checkListRecycler;
    private CheckListAdapter checkListAdapter;

    private Calendar calendar;

    public static CheckListFrag newInstance() {
        return new CheckListFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_check_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView = getView();
        mContext = getmContext();
        parentAct = ((MainAct)getActivity());
        selectedUserVo = parentAct.getSelectedUserVo();
        calendar = Calendar.getInstance();
        Log.e(TAG, selectedUserVo.toJson());

        setToolbar(View.VISIBLE, "등원 확인");
        retrofitApiAction(0);

        name = mView.findViewById(R.id.name);
        gender = mView.findViewById(R.id.gender);
        code = mView.findViewById(R.id.code);
        phoneNum = mView.findViewById(R.id.phone_num);
        startTime = mView.findViewById(R.id.today_start_time);
        endTime = mView.findViewById(R.id.today_end_time);
        checkListDate = mView.findViewById(R.id.check_list_date);

        checkListDate.setOnClickListener(this);

        setSelectedUserInfo();

        String dateStrType = DateFormatUtil.getCurrentDate(DateFormatUtil.DATE_FORMAT_4);
        checkListDate.setText(dateStrType);

        checkListRecycler = mView.findViewById(R.id.check_list_recycler);
        checkListRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        checkListAdapter = new CheckListAdapter(mContext, checkList);
        checkListRecycler.setAdapter(checkListAdapter);
        checkListRecycler.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
    }

    private void setSelectedUserInfo() {
        name.setText(selectedUserVo.getUser_name());
        gender.setText(selectedUserVo.getUser_gender());
        code.setText(selectedUserVo.getUser_code());
        phoneNum.setText(selectedUserVo.getUser_phone_num());

        String startTimeStr = "";
        String endTimeStr = "";
        try {
            if(selectedUserVo.getStart_time() != null) {
                startTimeStr = DateFormatUtil.formatChangeDateString(
                        selectedUserVo.getStart_time(),
                        DateFormatUtil.DATE_FORMAT_6,
                        DateFormatUtil.DATE_FORMAT_2);
            }

            if(selectedUserVo.getEnd_time() != null) {
                endTimeStr = DateFormatUtil.formatChangeDateString(
                        selectedUserVo.getEnd_time(),
                        DateFormatUtil.DATE_FORMAT_6,
                        DateFormatUtil.DATE_FORMAT_2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startTime.setText(startTimeStr);
        endTime.setText(endTimeStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_list_date:
                openDatePickerDialog();
                break;
            default:
                Log.e(TAG, "no click event");
        }
    }

    public void openDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE) ;

        DatePickerDialog dialog = new DatePickerDialog(mContext, listener, year, month, date);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setCalendarDate(year, monthOfYear, dayOfMonth);
            Date dateTime = calendar.getTime();
            String dateStrType = DateFormatUtil.convertDateToString(DateFormatUtil.DATE_FORMAT_4, dateTime);

            checkListDate.setText(dateStrType);
            selectedUserVo.setCheck_date(DateFormatUtil.convertDateToString(DateFormatUtil.DATE_FORMAT_5, dateTime));

            retrofitApiAction(dayOfMonth);
        }
    };

    /**
     * @param dayOfMonth default 0, only use RecyclerView scroll
     * */
    private void scrollPosition(ArrayList<CheckListVO> checkList, int dayOfMonth) {
        int position = 0;
        boolean isAbsent = false;

        for(int i = 0; i < checkList.size(); i++) {
            if(checkList.get(i).getCheckListDate().equals(dayOfMonth+"일")) {
                position = i;
                isAbsent = false;
                break;
            } else if(i == checkList.size()) {
                isAbsent = true;
            }
        }
        if(isAbsent) {
            Toast.makeText(mContext, "출석 기록이 없습니다.", Toast.LENGTH_SHORT).show();
        }

        LinearLayoutManager calendarLayoutManager = (LinearLayoutManager) checkListRecycler.getLayoutManager();
        if(calendarLayoutManager != null && dayOfMonth != 0)
            calendarLayoutManager.scrollToPositionWithOffset(position, 0);
    }

    private void setCalendarDate(int year, int month, int date) {
        calendar.set(year, month, date);
    }

    public void retrofitApiAction(final int dayOfMonth) {
        String user_code = selectedUserVo.getUser_code();
        String check_date = selectedUserVo.getCheck_date();

        getRetrofitApi().getCheckList(user_code, check_date).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                checkList.clear();

                try {
                    JSONObject jobj = new JSONObject(result);
                    JSONArray jarr = jobj.getJSONArray("lists");
                    CheckListVO vo;

                    for (int i = 0; i < jarr.length(); i++) {
                        String str = jarr.getString(i);

                        vo = (CheckListVO)new CheckListVO().fromJson(str);

                        Date date = DateFormatUtil.convertStringToDate(vo.getStart_time(),DateFormatUtil.DATE_FORMAT_6);
                        calendar.setTime(date);

                        vo.setCheckListDate(calendar.get(Calendar.DAY_OF_MONTH)+"");
                        vo.setCheckListDay(DateFormatUtil.formatChangeDateString(vo.getStart_time(), DateFormatUtil.DATE_FORMAT_6,  DateFormatUtil.DATE_FORMAT_7));
                        checkList.add(vo);
                    }

                    checkListAdapter.updateItem(checkList);

                    scrollPosition(checkList, dayOfMonth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

}
