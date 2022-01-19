package com.mx.academy_check.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.mx.academy_check.Api.RetrofitApi;
import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;
import com.mx.academy_check.act.LoginAct;
import com.mx.academy_check.act.ParentListAct;
import com.mx.academy_check.act.TabletAct;
import com.mx.academy_check.act.TeacherListAct;
import com.mx.academy_check.util.PreferenceManager;
import com.mx.academy_check.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.mx.academy_check.Constants.Constants.BASE_URL;

public class SideMenu implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "SideMenu";
    private Context mContext;
    private Activity mActivity;
    private static final int DRAWER_END = GravityCompat.END;
    private DrawerLayout mDrawer;
    private NavigationView nav;

    public SideMenu(Context context) {
        this.mContext = context;
        this.mActivity = ((Activity)mContext);
    }

    public void setDrawer() {
        mDrawer = mActivity.findViewById(R.id.drawer_layout);
        nav = mActivity.findViewById(R.id.nav_view);

        mActivity.findViewById(R.id.side_menu_btn).setOnClickListener(this);
        nav.setNavigationItemSelectedListener(this);

        String loginUserClass = PreferenceManager.getString(mContext, PreferenceManager.KEY_LOGIN_CLASS);
        Menu nav_menu = nav.getMenu();
        if (loginUserClass.equals(Constants.USER_CLASS_TEACHER)) {
            nav_menu.findItem(R.id.teacher_list).setVisible(false);
        } else if (loginUserClass.equals(Constants.USER_CLASS_PARENT)) {
            nav_menu.findItem(R.id.parent_list).setVisible(false);
            nav_menu.findItem(R.id.teacher_list).setVisible(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.side_menu_btn:
                mDrawer.openDrawer(DRAWER_END);
                break;
            default:
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mActivity.startActivity(new Intent(mContext, LoginAct.class));
                PreferenceManager.setBoolean(mContext, PreferenceManager.KEY_AUTO_LOGIN_IS_CHECKED, false);
                mActivity.finish();
                break;
            case R.id.parent_list:
                retrofitApiAction("getParentList");
                break;
            case R.id.teacher_list:
                retrofitApiAction("getTeacherList");
                break;
            case R.id.tablet:
                mActivity.startActivity(new Intent(mActivity, TabletAct.class));
                break;
            default:
                Log.e(TAG, "onNavigationItemSelected default");
        }

        mDrawer.closeDrawer(DRAWER_END);

        return false;
    }

    /*retrofit connection*/
    public RetrofitApi getRetrofitApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitApi.class);
    }

    public void retrofitApiAction(String doApi) {
        String user_class = PreferenceManager.getString(mContext, PreferenceManager.KEY_LOGIN_CLASS);
        String login_code = PreferenceManager.getString(mContext, PreferenceManager.KEY_LOGIN_CODE);

        Log.e(TAG + " retrofit param", login_code);
        if(doApi.equals("getParentList")) {
            getRetrofitApi().getParentList(user_class, login_code).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();
                    ArrayList<UserVO> parentList = new ArrayList<>();

                    Log.e(TAG + " menu result", result);
                    try {
                        JSONObject jobj = new JSONObject(result);
                        JSONArray jarr = jobj.getJSONArray("lists");
                        UserVO vo;

                        for (int i = 0; i < jarr.length(); i++) {
                            String str = jarr.getString(i);

                            vo = (UserVO) new UserVO().fromJson(str);
                            vo.setUser_gender(vo.getGender());
                            parentList.add(vo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(mContext, ParentListAct.class);
                    intent.putExtra("userList", parentList);
                    mContext.startActivity(intent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        } else if(doApi.equals("getTeacherList")) {
            getRetrofitApi().getTeacherList(login_code).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result = response.body();
                    ArrayList<UserVO> teacherList = new ArrayList<>();

                    Log.e(TAG + " menu result", result);
                    try {
                        JSONObject jobj = new JSONObject(result);
                        JSONArray jarr = jobj.getJSONArray("lists");
                        UserVO vo;

                        for (int i = 0; i < jarr.length(); i++) {
                            String str = jarr.getString(i);

                            vo = (UserVO) new UserVO().fromJson(str);
                            vo.setUser_code(vo.getTeacher_code());
                            vo.setUser_name(vo.getTeacher_name());
                            vo.setUser_phone_num(vo.getPhone_number());
                            teacherList.add(vo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(mContext, TeacherListAct.class);
                    intent.putExtra("userList", teacherList);
                    mContext.startActivity(intent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }

    }
}
