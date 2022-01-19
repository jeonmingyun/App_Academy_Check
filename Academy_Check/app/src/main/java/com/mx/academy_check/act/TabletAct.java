package com.mx.academy_check.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.academy_check.Api.RetrofitApi;
import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TabletAct extends AppCompatActivity implements View.OnClickListener {

    private final static int STUDENT_CODE_MAX_LENGTH = 4;
    private static final String TAG = "TabletAct";

    private TextView student_code_tv;
    private Button tel1, tel2, tel3, tel4, tel5, tel6, tel7, tel8, tel9, tel0;
    private ImageButton delete_btn, send_btn;
    private String student_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tablet);

        student_code_tv = findViewById(R.id.student_code_txt);

        findViewById(R.id.tel0).setOnClickListener(this);
        findViewById(R.id.tel1).setOnClickListener(this);
        findViewById(R.id.tel2).setOnClickListener(this);
        findViewById(R.id.tel3).setOnClickListener(this);
        findViewById(R.id.tel4).setOnClickListener(this);
        findViewById(R.id.tel5).setOnClickListener(this);
        findViewById(R.id.tel6).setOnClickListener(this);
        findViewById(R.id.tel7).setOnClickListener(this);
        findViewById(R.id.tel8).setOnClickListener(this);
        findViewById(R.id.tel9).setOnClickListener(this);
        findViewById(R.id.delete_btn).setOnClickListener(this);
        findViewById(R.id.send_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tel0:
                student_code_tv.append("0");
                break;
            case R.id.tel1:
                student_code_tv.append("1");
                break;
            case R.id.tel2:
                student_code_tv.append("2");
                break;
            case R.id.tel3:
                student_code_tv.append("3");
                break;
            case R.id.tel4:
                student_code_tv.append("4");
                break;
            case R.id.tel5:
                student_code_tv.append("5");
                break;
            case R.id.tel6:
                student_code_tv.append("6");
                break;
            case R.id.tel7:
                student_code_tv.append("7");
                break;
            case R.id.tel8:
                student_code_tv.append("8");
                break;
            case R.id.tel9:
                student_code_tv.append("9");
                break;
            case R.id.delete_btn:
                student_code = student_code_tv.getText().toString();

                if (!student_code.equals("")) {
                    student_code_tv.setText(student_code.substring(0, student_code.length() - 1));
                }
                break;
            case R.id.send_btn:
                student_code = student_code_tv.getText().toString();

                if (student_code.length() == STUDENT_CODE_MAX_LENGTH) {
                    retrofitApiFcm(student_code);

                    student_code_tv.setText("");
                    Toast.makeText(TabletAct.this, "등하원 확인하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "코드를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void retrofitApiAction(String student_code) {
        /*retrofit connection*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi api = retrofit.create(RetrofitApi.class);

        api.tabletCheck(student_code).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();

                Log.e(TAG + " result 1", result);
                if (result.equals("success")) {
                    Toast.makeText(TabletAct.this, "시간을 확인 하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TabletAct.this, "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void retrofitApiFcm(String student_code) {
        /*retrofit connection*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi api = retrofit.create(RetrofitApi.class);

        Log.e(TAG, "fcm code = " + student_code);
        api.tabletFcm(Constants.FCM_TYPE_TIME_CHECK_TABLET, student_code).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body().trim();

                Log.e(TAG + " FCM response", result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }
}
