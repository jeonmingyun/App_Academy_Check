package com.mx.academy_check.act;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.academy_check.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class BaseAct extends AppCompatActivity {
    public final String TAG = getClass().getSimpleName();
    private final int PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        overridePendingTransition(0,0);
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToast(int msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public String getVersionInfo() {
        String version = "Unknown";
        PackageInfo packageInfo;

        try {
            packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("getVersionInfo", e.getMessage());
        }
        return version;
    }

    protected void showView(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment, tag)
                .commit();
    }

    protected void showView(Fragment fragment, String tag, String bundleKey, String bundleValue) {
        Bundle bundle = new Bundle();
        bundle.putString(bundleKey, bundleValue);

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment, tag)
                .commit();
    }

    protected void removeBackStack(String tag) {
        getSupportFragmentManager().popBackStack(1, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected void clearFragStack() {
        getSupportFragmentManager().popBackStack();
    }

    protected void setToolbar(int arrowVisiblity, String title) {
        ((TextView) findViewById(R.id.title_textview)).setText(title);
        findViewById(R.id.back_img_btn).setVisibility(arrowVisiblity);
        findViewById(R.id.back_img_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /* permission part*/
//    private final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 1;
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case PERMISSION_REQUEST_CODE: {
//                showGuide();
//            }
//            break;
//            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE: {
////                SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
////                boolean isFirst = pref.getBoolean("isFirst", false);
////                if (!isFirst) {
////                    Intent intent = new Intent(getApplicationContext(), GuideAct.class);
////                    startActivity(intent);
////                    pref.edit().putBoolean("isFirst", true).apply();
////                }
//    }
//            break;
//}
//    }
//
//    protected void showGuide() {
//        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
//        boolean isFirst = pref.getBoolean("isFirst", false);
//        if (!isFirst) {
//            Intent intent = new Intent(getApplicationContext(), GuideAct.class);
//            startActivity(intent);
//            pref.edit().putBoolean("isFirst", true).apply();
//        }
//    }
//
//    protected boolean requestPermission() {
//        // 파일 읽기/쓰기 권한
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    PERMISSION_REQUEST_CODE);
//
//            return false;
//        } else {
//            return true;
//        }
//
//    }
//
//    protected boolean requestLocationPermission(){
//        // 위치 권한
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSION_REQUEST_CODE);
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    protected boolean requestCameraPermission(){
//        //  카메라 권한
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
//                    PERMISSION_REQUEST_CODE);
//
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public boolean hasPermissions(Context context, String... permissions){
//        if(context != null && permissions != null){
//
//            for(String permission : permissions){
//                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
//                    return false;
//                }
//            }
//
//        }
//        return true;
//    }
//
//    public void showPopup(final Context con){
//        final AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
//        localBuilder.setTitle("권한 설정").setCancelable(false).setMessage("원할한 서비스를 위해서는 권한을 허용해주세요.");
//
//        AlertDialog alertDialog = localBuilder.create();
//
//        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "권한 설정", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                ActivityCompat.requestPermissions((Activity) con, Common.PERMISSIONS, Common.PERMISSION_ALL);
//                dialogInterface.dismiss();
//            }
//        });
//
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "종료하기", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
//            }
//        });
//
//        alertDialog.show();
//    }
}
