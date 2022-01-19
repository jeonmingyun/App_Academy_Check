package com.mx.academy_check.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mx.academy_check.Api.RetrofitApi;
import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;
import com.mx.academy_check.adapter.ParentListAdapter;
import com.mx.academy_check.menu.SideMenu;
import com.mx.academy_check.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.mx.academy_check.Constants.Constants.BASE_URL;

public class ParentListAct extends BaseAct {

    private String actUserClass;
    private ArrayList<UserVO> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_parent_list);

        actUserClass = Constants.USER_CLASS_PARENT ;// 학부모 리스트 = 2, 선생님 리스트 = 1
        userList = (ArrayList<UserVO>) getIntent().getSerializableExtra("userList");
        setToolbar(View.VISIBLE, "학부모 리스트");
        new SideMenu(this).setDrawer();

        RecyclerView userRecycler = findViewById(R.id.parent_recycler);
        ParentListAdapter userAdapter = new ParentListAdapter(this, userList);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        userRecycler.setLayoutManager(lm);
        userRecycler.addItemDecoration(new DividerItemDecoration(this, lm.getOrientation()));
        userAdapter.setOnItemClickListener(new ParentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                String userCode = ((TextView) v.findViewById(R.id.code)).getText().toString();
                retrofitApiAction(actUserClass, userCode);
            }
        });
        userRecycler.setAdapter(userAdapter);

        if(userList.isEmpty()) {
            findViewById(R.id.is_empty_txt).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.is_empty_txt).setVisibility(View.GONE);
        }

    }

    public void retrofitApiAction(String user_class, String user_code) {
        Log.e(TAG + " retrofit param", user_class + " / " + user_code);
        getRetrofitApi().getStudentList(user_class, user_code).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();
                ArrayList<UserVO> studentList = new ArrayList<>();

                Log.e(TAG + " side menu result", result);
                try {
                    JSONObject jobj = new JSONObject(result);
                    JSONArray jarr = jobj.getJSONArray("lists");
                    UserVO vo;

                    for( int i = 0; i < jarr.length(); i++) {
                        String str = jarr.getString(i);

                        vo = (UserVO) new UserVO().fromJson(str);
                        studentList.add(vo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getBaseContext(), StudentListAct.class);
                intent.putExtra("userList", studentList);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public RetrofitApi getRetrofitApi() {
        /*retrofit connection*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitApi.class);
    }

}
