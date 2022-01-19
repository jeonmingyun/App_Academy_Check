package com.mx.academy_check.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mx.academy_check.Api.RetrofitApi;
import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;
import com.mx.academy_check.util.PreferenceManager;
import com.mx.academy_check.vo.LoginUserVO;
import com.mx.academy_check.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.mx.academy_check.Constants.Constants.BASE_URL;

public class LoginAct extends AppCompatActivity implements View.OnClickListener {

    private static String TAG;

    private LoginUserVO loginVo;
    private TextView loginAcademy, loginCode;
    private ArrayList<UserVO> userList;
    private UserVO userVo;
    private String login_class;
    private CheckBox autoLoginCheckBox, saveIdCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        TAG = getClass().getSimpleName();

        loginAcademy = findViewById(R.id.login_academy);
        loginCode = findViewById(R.id.login_code);
        autoLoginCheckBox = findViewById(R.id.auto_login_icon);
        saveIdCheckBox = findViewById(R.id.save_id_icon);

        userList = new ArrayList<>();

        findViewById(R.id.auto_login).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);

        boolean autoLoginIsChecked = PreferenceManager.getBoolean(this, PreferenceManager.KEY_AUTO_LOGIN_IS_CHECKED);
        boolean saveIdIsChecked = PreferenceManager.getBoolean(this, PreferenceManager.KEY_SAVE_ID_IS_CHECKED);
        String loginAcademyValue = PreferenceManager.getString(this, PreferenceManager.KEY_LOGIN_ACADEMY);
        String loginCodeValue = PreferenceManager.getString(this, PreferenceManager.KEY_LOGIN_CODE);
        if(autoLoginIsChecked) {
            retrofitApiAction(loginAcademyValue, loginCodeValue);
        }
        if(saveIdIsChecked) {
            loginCode.setText(loginCodeValue);
        }

        /*push 알림 token 얻기*/
    }

    private void setId() {
    }

    private void autoLogin() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        userList.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auto_login:
                onCheckboxClicked(R.id.auto_login_icon);
                break;
            case R.id.save_id:
                onCheckboxClicked(R.id.save_id_icon);
                break;
            case R.id.login_btn:
                if (autoLoginCheckBox.isChecked()) {
                    PreferenceManager.setBoolean(this, PreferenceManager.KEY_AUTO_LOGIN_IS_CHECKED, true);
                    PreferenceManager.setString(this, PreferenceManager.KEY_LOGIN_ACADEMY, ((TextView)findViewById(R.id.login_academy)).getText().toString());
                    PreferenceManager.setString(this, PreferenceManager.KEY_LOGIN_CODE, ((TextView)findViewById(R.id.login_code)).getText().toString());
                }
                if (saveIdCheckBox.isChecked()) {
                    PreferenceManager.setBoolean(this, PreferenceManager.KEY_SAVE_ID_IS_CHECKED, true);
                    PreferenceManager.setString(this, PreferenceManager.KEY_LOGIN_ACADEMY, ((TextView)findViewById(R.id.login_academy)).getText().toString());
                }
                retrofitApiAction(loginAcademy.getText().toString(), loginCode.getText().toString().trim());

                /*retrofitApiAction();으로 server 접근 하지 않고 이동할때 사용*/
//                Intent intent = new Intent(LoginAct.this, MainAct.class);
//                userList = new UserVO().getTestList();
//                PreferenceManager.setString(LoginAct.this, PreferenceManager.KEY_LOGIN_CLASS, Constants.USER_CLASS_PARENT);
//                intent.putExtra("userList", userList);
//                startActivity(intent);
                break;
            default:
        }

    }

    public void onCheckboxClicked(int id) {
        CheckBox checkbox = findViewById(id);
        boolean checked = !checkbox.isChecked();

        checkbox.setChecked(checked);

        if (checked)
            Log.e(TAG, "auto btn is checked");
        else
            Log.e(TAG, "auto btn not checked");
    }

    public void retrofitApiAction(final String login_academy, final String login_code) {
        /*retrofit connection*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi api = retrofit.create(RetrofitApi.class);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        PreferenceManager.setString(LoginAct.this, PreferenceManager.KEY_TOKEN, token);

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.e(TAG, token);
                    }
                });

        String token = PreferenceManager.getString(this, PreferenceManager.KEY_TOKEN);
        api.doLogin(login_academy, login_code, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();

                try {
                    JSONObject jobj = new JSONObject(result); // jobj = { "lists":[], "checkList":[], "user_class":"" }

                    JSONArray listsJson = jobj.getJSONArray("lists");
                    JSONArray checkListJson = jobj.getJSONArray("checkList");
                    UserVO userVo;
                    UserVO listsVo;
                    UserVO checkListVo;

                    for (int i = 0; i < listsJson.length(); i++) {
                        userVo = new UserVO();

                        String listsStr = listsJson.getString(i);
                        String checkListStr = checkListJson.getString(i);

                        listsVo = (UserVO) new UserVO().fromJson(listsStr);
                        userVo = listsVo;
                        userVo.setUser_gender(listsVo.getGender());

                        checkListVo = (UserVO) new UserVO().fromJson(checkListStr);
                        userVo.setStart_time(checkListVo.getStart_time());
                        userVo.setEnd_time(checkListVo.getEnd_time());
                        userVo.setStart_day(checkListVo.getDay());

                        userList.add(userVo);
                    }

                    LoginAct.this.login_class = jobj.getString("user_class");
                    PreferenceManager.setString(LoginAct.this, PreferenceManager.KEY_LOGIN_CLASS, login_class);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (result.equals("fail")) {
                    Toast.makeText(LoginAct.this, "로그인 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    PreferenceManager.setString(LoginAct.this, PreferenceManager.KEY_LOGIN_ACADEMY, login_academy);
                    PreferenceManager.setString(LoginAct.this, PreferenceManager.KEY_LOGIN_CODE, login_code);
                    PreferenceManager.setString(LoginAct.this, PreferenceManager.KEY_LOGIN_CLASS, login_class);
                    Intent intent = new Intent(LoginAct.this, MainAct.class);
                    intent.putExtra("userList", userList);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }



}
