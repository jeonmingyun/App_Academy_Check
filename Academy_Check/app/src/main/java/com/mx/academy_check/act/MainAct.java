package com.mx.academy_check.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.academy_check.Constants.Constants;
import com.mx.academy_check.R;
import com.mx.academy_check.frag.CheckListFrag;
import com.mx.academy_check.frag.MainFrag;
import com.mx.academy_check.menu.SideMenu;
import com.mx.academy_check.util.PreferenceManager;
import com.mx.academy_check.vo.UserVO;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class MainAct extends BaseAct implements View.OnClickListener {

    private static final String TAG = "MainAct";
    public static Context mainActContext;

    private boolean backKeyPressedTime = false;
    private ArrayList<UserVO> userList = new ArrayList<>(); // 전체 학생 리스트
    private UserVO selectedUserVo = new UserVO(); // userList 선택된 학생 정보
    private ImageButton userAddBtn;
    public int togglePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        mainActContext = this;
        userList = (ArrayList<UserVO>) getIntent().getSerializableExtra("userList");
        new SideMenu(this).setDrawer();

        userAddBtn = findViewById(R.id.user_add_btn);

        userAddBtn.setOnClickListener(this);

        showView(MainFrag.newInstance(), MainFrag.TAG);

        String login_class = PreferenceManager.getString(this, PreferenceManager.KEY_LOGIN_CLASS);
        if (login_class.equals(Constants.USER_CLASS_PARENT)) {
            findViewById(R.id.user_add_btn).setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setFragItem();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        /*StudentListAct.java 학생 클릭시*/
        String selectedUserCode = intent.getStringExtra("selectedUserCode");
        if(selectedUserCode != null) {
            togglePosition = -1;
            for(int i = 0; i < userList.size(); i++) {
                if(selectedUserCode.equals(userList.get(i).getUser_code())) {
                    selectedUserVo = userList.get(i);
                    togglePosition = i;
                    setFragItem();
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(PreferenceManager.getBoolean(this, PreferenceManager.KEY_AUTO_LOGIN_IS_CHECKED)) {
            // ...
        } else if(PreferenceManager.getBoolean(this, PreferenceManager.KEY_SAVE_ID_IS_CHECKED)) {
            PreferenceManager.removeKey(this, PreferenceManager.KEY_LOGIN_CODE);
            PreferenceManager.removeKey(this, PreferenceManager.KEY_LOGIN_CLASS);
        } else {
            PreferenceManager.removeKey(this, PreferenceManager.KEY_LOGIN_ACADEMY);
            PreferenceManager.removeKey(this, PreferenceManager.KEY_LOGIN_CODE);
            PreferenceManager.removeKey(this, PreferenceManager.KEY_LOGIN_CLASS);
        }
    }

    @Override
    public void onBackPressed() {
        if (backKeyPressedTime) {
            finish();
            return;
        }

        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        if(frag instanceof MainFrag) {
            this.backKeyPressedTime = true;
            Toast.makeText(this, "한번 더 클릭하면 종료 됩니다.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backKeyPressedTime =false;
                }
            }, 2000);
        } else {
            showView(MainFrag.newInstance(), MainFrag.TAG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_add_btn:
                startActivity(new Intent(this, UserAddAct.class));
                break;
            default:
        }
    }

    public ArrayList<UserVO> getUserList() {
        return userList;
    }

    public UserVO getSelectedUserVo() {
        return selectedUserVo;
    }
    public void setSelectedUserVo(UserVO userVo) {
        this.selectedUserVo = userVo;
    }

    public void setFragItem() {
        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            if (frag.isVisible()) {
                if (frag instanceof MainFrag) {
                    ((MainFrag) frag).userListAdapter.updateAllItem(userList);
                } else if (frag instanceof CheckListFrag) {
                    ((TextView) findViewById(R.id.name)).setText(selectedUserVo.getUser_name());
                    ((TextView) findViewById(R.id.gender)).setText(selectedUserVo.getUser_gender());
                    ((TextView) findViewById(R.id.code)).setText(selectedUserVo.getUser_code());
                    ((TextView) findViewById(R.id.phone_num)).setText(selectedUserVo.getUser_phone_num());
                    ((TextView) findViewById(R.id.today_start_time)).setText(selectedUserVo.getStart_time());
                    ((TextView) findViewById(R.id.today_end_time)).setText(selectedUserVo.getEnd_time());
                }
            }
        }
    }

    public void userListItemAdd(UserVO item) {
        this.selectedUserVo = item;
        this.userList.add(item);
        setFragItem();
    }

    public void userListItemDelete(String userCode) {
        int idx = -1;

        for(int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getUser_code().equals(userCode)) {
                idx = i;
                break;
            }
        }
        if( idx != -1) {
            this.userList.remove(idx);
        }
        setFragItem();
    }

    public void userListItemUpdate(UserVO item) {
        int pos = -1;
        for(UserVO vo : userList) {
            pos++;
            if(vo.getUser_code().equals(item.getUser_code())) {
                break;
            }
        }
        this.selectedUserVo = item;
        this.userList.set(pos, item);
        setFragItem();
    }


}
