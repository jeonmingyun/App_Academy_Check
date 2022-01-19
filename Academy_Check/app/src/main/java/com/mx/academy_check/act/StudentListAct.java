package com.mx.academy_check.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mx.academy_check.R;
import com.mx.academy_check.adapter.StudentListAdapter;
import com.mx.academy_check.frag.MainFrag;
import com.mx.academy_check.menu.SideMenu;
import com.mx.academy_check.vo.UserVO;

import java.util.ArrayList;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StudentListAct extends BaseAct {

    private ArrayList<UserVO> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_student_list);

        userList = (ArrayList<UserVO>) getIntent().getSerializableExtra("userList");
        setToolbar(View.VISIBLE, "학생 리스트");
        new SideMenu(this).setDrawer();

        RecyclerView userRecycler = findViewById(R.id.student_recycler);
        StudentListAdapter userAdapter = new StudentListAdapter(this, userList);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        userRecycler.setLayoutManager(lm);
        userRecycler.addItemDecoration(new DividerItemDecoration(this, lm.getOrientation()));
        userAdapter.setOnItemClickListener(new StudentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(View v, int position) {
                Intent intent = new Intent(StudentListAct.this, MainAct.class);
                intent.putExtra("selectedUserCode", ((TextView)v.findViewById(R.id.code)).getText().toString());
                startActivity(intent);
            }
        });
        userRecycler.setAdapter(userAdapter);

        if(userList.isEmpty()) {
            findViewById(R.id.is_empty_txt).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.is_empty_txt).setVisibility(View.GONE);
        }
    }

}
