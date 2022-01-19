package com.mx.academy_check.frag;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.academy_check.Api.RetrofitApi;
import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;
import com.mx.academy_check.act.MainAct;
import com.mx.academy_check.act.UserAddAct;
import com.mx.academy_check.adapter.UserListAdapter;
import com.mx.academy_check.util.DateFormatUtil;
import com.mx.academy_check.util.PreferenceManager;
import com.mx.academy_check.vo.UserVO;
import com.ramotion.foldingcell.FoldingCell;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFrag extends BaseFrag {

    public static final String TAG = MainFrag.class.getSimpleName();

    private Context mContext;
    private View mView;
    private MainAct parentAct;
    private ArrayList<UserVO> userList;
    private UserVO selectedUserVo;

    private TextView currentDateTxt;
    private ListView listview;
    public UserListAdapter userListAdapter;

    public static MainFrag newInstance() {
        return new MainFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView = getView();
        mContext = getmContext();
        parentAct = ((MainAct) getActivity());
        userList = parentAct.getUserList();
        selectedUserVo = parentAct.getSelectedUserVo();

        setToolbar(View.INVISIBLE, "등원 확인");

        currentDateTxt = mView.findViewById(R.id.current_date_txt);

        currentDateTxt.setText(DateFormatUtil.getCurrentDate(DateFormatUtil.DATE_FORMAT_8));
        createUserRecyclerView();
    }

    @Override
    public void onPause() {
        super.onPause();
        parentAct.setSelectedUserVo(this.selectedUserVo);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*StudentListAct.java 에서 학생 선택시 MainFrag.java list toggle 효과*/
        /*실행 후 해당 view는 notifyDataSetChanged()가 되지 않음*/
//        if(parentAct.togglePosition >= 0) {
//            onToggleByPosition(parentAct.togglePosition);
//        }
    }

    public void onToggleByPosition(final int position) {
        listview.setSelection(position);
        final FoldingCell view = (FoldingCell)getViewByPosition(position, listview);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e("onResume listview H", view.getHeight()+" / " + parentAct.togglePosition);
                if(!view.isUnfolded()) {
                    view.unfold(true);
                    userListAdapter.registerToggle(position);

                    removeOnGlobalLayoutListener(view.getViewTreeObserver(), this);
                }
            }
        });

        parentAct.togglePosition = -1;
    }

    private static void removeOnGlobalLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (observer == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeGlobalOnLayoutListener(listener);
        } else {
            observer.removeOnGlobalLayoutListener(listener);
        }
    }

    private View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void retrofitApiAction(int clickId) {
        String login_code = PreferenceManager.getString(mContext, "login_code");
        String user_code, start_time, start_day, end_time, end_day;

        final RetrofitApi retrofitApi = getRetrofitApi();
        switch (clickId) {
            case R.id.start_check_btn:
                user_code = selectedUserVo.getUser_code();
                start_time = selectedUserVo.getStart_time();
                start_day = selectedUserVo.getStart_day();

                retrofitApi.checkStartTime(login_code, user_code, start_time, start_day).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body().trim();

                        Log.e(TAG + " reault", result);
                        if (result.equals("success")) {
                            Toast.makeText(mContext, "학생이 등원하였습니다.", Toast.LENGTH_SHORT).show();
                            userListAdapter.updateItem(selectedUserVo.getItemPosition(), selectedUserVo);

                            // send FCM
                            retrofitApi.fcm(Constants.FCM_TYPE_TIME_CHECK_START, MainFrag.this.selectedUserVo.getUser_code()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body().trim();

                                    Log.e(TAG + " send FCM result", result);
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.e(TAG, t.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
                break;
            case R.id.end_check_btn:
                user_code = selectedUserVo.getUser_code();
                end_time = selectedUserVo.getEnd_time();
                end_day = selectedUserVo.getEnd_day();

                /* end 성공은 하는데 fail 출력됨*/
                Log.e(TAG + " request end data", user_code + " / " + end_time + " / " + end_day);
                retrofitApi.checkEndTime(login_code, user_code, end_time, end_day).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String result = response.body().trim();

                        Log.e(TAG + " reault", result);
                        if (result.equals("success")) {
                            Toast.makeText(mContext, "학생이 하원하였습니다.", Toast.LENGTH_SHORT).show();
                            userListAdapter.updateItem(selectedUserVo.getItemPosition(), selectedUserVo);

                            // send FCM
                            retrofitApi.fcm(Constants.FCM_TYPE_TIME_CHECK_END, MainFrag.this.selectedUserVo.getUser_code()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body().trim();

                                    Log.e(TAG + " send FCM result", result);
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.e(TAG, t.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });
                break;
            default:
        }
    }

    private void createUserRecyclerView() {
        listview = mView.findViewById(R.id.list_view);
        userListAdapter = new UserListAdapter(mContext, userList) ;
        userListAdapter.setOnBtnClickListener(new UserListAdapter.OnBtnClickListener() {
            @Override
            public void onPopUpBtnClicked(View v, int position) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        selectedUserVo.setUser_class(Constants.USER_CLASS_STUDENT);

                        String userName = selectedUserVo.getUser_name();

                        if (userName == null || userName.equals("")) {
                            Toast.makeText(mContext, "학생을 선택해주세요.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG + "popup 1", selectedUserVo.toJson());
                        } else {
                            if (menuItem.getItemId() == R.id.popup_check_list) {
                                Log.e(TAG + "popup 2", selectedUserVo.toJson());
                                movePage(CheckListFrag.newInstance(), CheckListFrag.TAG);
                            } else if (menuItem.getItemId() == R.id.popup_edit_info) {
                                Log.e(TAG + "popup 3", selectedUserVo.toJson());
                                Intent intent = new Intent(mContext, UserAddAct.class);
                                intent.putExtra("uservo", (Serializable) selectedUserVo);
                                startActivity(intent);
                            } else {
                                Log.e(TAG, "no pop up menu");

                            }
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }

            @Override
            public void onStartBtnClicked(View v, int position) {
                if (userList.get(position).getStart_time() != null) {
                    Toast.makeText(mContext, "이미 등원 하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    selectedUserVo.setStart_time(DateFormatUtil.getCurrentDate(DateFormatUtil.DATE_FORMAT_3)
                            + " " + DateFormatUtil.getCurrentTime(DateFormatUtil.DATE_FORMAT_2));

                    retrofitApiAction(R.id.start_check_btn);
                }
            }

            @Override
            public void onEndBtnClicked(View v, int position) {
                if (userList.get(position).getStart_time() == null) {
                    Toast.makeText(mContext, "아직 등원하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    /*push 알림 테스트를 위해 주석 처리*/
                } else if (userList.get(position).getEnd_time() != null) {
                    Toast.makeText(mContext, "이미 하원 하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    selectedUserVo.setEnd_time(DateFormatUtil.getCurrentDate(DateFormatUtil.DATE_FORMAT_3)
                            + " " + DateFormatUtil.getCurrentTime(DateFormatUtil.DATE_FORMAT_2));

                    Log.e(TAG + " end selectedDate", selectedUserVo.getEnd_time());
                    retrofitApiAction(R.id.end_check_btn);
                }
            }
        });
        listview.setAdapter(userListAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedUserVo = userList.get(position);
                selectedUserVo.setCheck_date(DateFormatUtil.getCurrentDate(DateFormatUtil.DATE_FORMAT_5));
                selectedUserVo.setItemPosition(position);

                ((FoldingCell) view).toggle(false);
                userListAdapter.registerToggle(position);
            }
        });
    }

}
