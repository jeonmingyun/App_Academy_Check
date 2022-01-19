package com.mx.academy_check.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.academy_check.Api.RetrofitApi;
import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;
import com.mx.academy_check.util.PreferenceManager;
import com.mx.academy_check.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.mx.academy_check.Constants.Constants.BASE_URL;

public class UserAddAct extends BaseAct implements View.OnClickListener {

    public static final String TAG = UserAddAct.class.getSimpleName();

    private RadioGroup userRadioGroup;
    private Spinner genderSpin;
    private EditText name, phoneNum, parentCode;
    private TextView parentCodeLable;
    private UserVO selectedUserVo = new UserVO();
    private boolean isEditPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_user_add);

        setToolbar(View.VISIBLE, "회원 등록");

        userRadioGroup = findViewById(R.id.user_radio_group);
        genderSpin = findViewById(R.id.user_gender);
        name = findViewById(R.id.user_name);
        phoneNum = findViewById(R.id.user_phone_num);
        parentCode = findViewById(R.id.user_parent_code);
        parentCodeLable = findViewById(R.id.user_parent_code_lable);

        findViewById(R.id.user_add_submit_btn).setOnClickListener(this);

        ArrayAdapter<CharSequence> vacationStateAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_spin_arr, android.R.layout.simple_spinner_item);
        vacationStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpin.setAdapter(vacationStateAdapter);

        /* Used when modifying user information */
        if (getIntent().getSerializableExtra("uservo") != null) {
            selectedUserVo = (UserVO) getIntent().getSerializableExtra("uservo");
            String gender = selectedUserVo.getUser_gender();
            ArrayAdapter myAdap = (ArrayAdapter) genderSpin.getAdapter();
            int spinPosition = myAdap.getPosition(gender);

            Log.e(TAG + " vo", selectedUserVo.toJson());

            ((RadioButton) userRadioGroup.getChildAt(Integer.parseInt(selectedUserVo.getUser_class()))).setChecked(true);
            name.setText(selectedUserVo.getUser_name());
            genderSpin.setSelection(spinPosition);
            phoneNum.setText(selectedUserVo.getUser_phone_num());
            parentCode.setText(selectedUserVo.getParent_code());

            for (int i = 0; i < userRadioGroup.getChildCount(); i++) {
                userRadioGroup.getChildAt(i).setEnabled(false);
            }

            isEditPage = true;
        }

        userRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.student_radio_btn:
                        parentCode.setVisibility(View.VISIBLE);
                        parentCodeLable.setVisibility(View.VISIBLE);
                        break;
                    case R.id.teacher_radio_btn:
                    case R.id.parent_radio_btn:
                        parentCode.setVisibility(View.GONE);
                        parentCodeLable.setVisibility(View.GONE);
                        break;
                    default:

                }
            }
        });

        setRadioBtnVisible(PreferenceManager.getString(this, PreferenceManager.KEY_LOGIN_CLASS));
        setParentCode();
    }


    private void setParentCode() {
        String login_class = PreferenceManager.getString(this, PreferenceManager.KEY_LOGIN_CLASS);
        String login_code = PreferenceManager.getString(this, PreferenceManager.KEY_LOGIN_CODE);

        if (login_class.equals(Constants.USER_CLASS_PARENT)) {
            parentCode.setText(login_code);
            parentCode.setEnabled(false);
        }
    }

    public void setRadioBtnVisible(String login_class) {
        if (login_class.equals(Constants.USER_CLASS_TEACHER)) {
            findViewById(R.id.teacher_radio_btn).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_add_submit_btn:
                int radioId = userRadioGroup.getCheckedRadioButtonId();
                String userClass = Constants.USER_CLASS_STUDENT;
                String name, gender, phoneNum, parentCode;

                if (radioId == R.id.student_radio_btn) {
                    userClass = Constants.USER_CLASS_STUDENT;
                } else if (radioId == R.id.teacher_radio_btn) {
                    userClass = Constants.USER_CLASS_TEACHER;
                } else if (radioId == R.id.parent_radio_btn) {
                    userClass = Constants.USER_CLASS_PARENT;
                } else {
                    Log.e(TAG, "radio button unchecked");
                    return;
                }

                name = this.name.getText().toString();
                gender = this.genderSpin.getSelectedItem().toString();
                phoneNum = this.phoneNum.getText().toString();
                parentCode = this.parentCode.getText().toString();

                if (name.equals("")) {
                    Toast.makeText(this, "성명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedUserVo.setUser_class(userClass);
                selectedUserVo.setUser_name(name);
                selectedUserVo.setUser_gender(gender);
                selectedUserVo.setUser_phone_num(phoneNum);
                selectedUserVo.setParent_code(parentCode);
                retrofitApiAction(selectedUserVo, isEditPage);
                break;
            default:
                Log.e(TAG, "no click event");
        }
    }

    public void retrofitApiAction(final UserVO addUserVo, boolean isEditPage) {
        String login_code = PreferenceManager.getString(this, "login_code");
        String user_class = addUserVo.getUser_class();
        String user_code = addUserVo.getUser_code();
        String user_name = addUserVo.getUser_name();
        String user_gender = addUserVo.getUser_gender();
        String user_phone_num = addUserVo.getUser_phone_num();
        String parent_code = addUserVo.getParent_code();

        /*retrofit connection*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        Log.e(TAG + " retrofit", user_code + " / " + user_class + " / " + user_name + " / " + user_gender + " / " + user_phone_num + " / " + parent_code);

        if (isEditPage) { // is user edit page
            if(user_code.equals("") && user_class.equals("") && user_name.equals("") && parent_code.equals("")) {
                Toast.makeText(this, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            retrofitApi.editUser(user_code, user_class, user_name, user_gender, user_phone_num, parent_code)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            Log.e(TAG + " edit result", result);
                            try {
                                JSONObject jobj = new JSONObject(result);
                                JSONArray jarr = jobj.getJSONArray("lists");
                                UserVO responseVo;

                                String str = jarr.getString(0);
                                responseVo = (UserVO) new UserVO().fromJson(str);
                                String editUserClass = responseVo.getUser_class();
                                String editUserCode = responseVo.getUser_code();
                                addUserVo.setUser_class(editUserClass);
                                addUserVo.setUser_code(editUserCode);

                                if (editUserClass.equals(Constants.USER_CLASS_STUDENT)) {
                                    ((MainAct)MainAct.mainActContext).userListItemUpdate(addUserVo);
                                    finish();
                                }

                                if (!result.equals("fail")) {
                                    Toast.makeText(getBaseContext(), "수정 되었습니다.", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e(TAG, t.getMessage());
                        }
                    });
        } else { // is user add page
            if(user_class.equals("") && user_name.equals("") && parent_code.equals("")) {
                Toast.makeText(this, "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            retrofitApi.addUser(login_code, user_class, user_name, user_gender, user_phone_num, parent_code)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            Log.e(TAG + " add result", result);

                            try {
                                JSONObject jobj = new JSONObject(result);
                                JSONArray jarr = jobj.getJSONArray("lists");
                                UserVO responseVo;

                                String str = jarr.getString(0);
                                responseVo = (UserVO) new UserVO().fromJson(str);
                                String addUserClass = responseVo.getUser_class();
                                String addUserCode = responseVo.getUser_code();

                                Log.e(TAG + " responseVo", responseVo.toJson());
                                addUserVo.setUser_class(addUserClass);
                                addUserVo.setUser_code(addUserCode);

                                if (addUserClass.equals(Constants.USER_CLASS_STUDENT)) {
                                    ((MainAct)MainAct.mainActContext).userListItemAdd(addUserVo);
                                    finish();
                                }

                                if (!result.equals("fail")) {
                                    Toast.makeText(getBaseContext(), "등록 코드 : " + addUserCode, Toast.LENGTH_LONG).show();
                                }

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
}
