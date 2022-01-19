package com.mx.academy_check.frag;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.academy_check.Api.RetrofitApi;
import com.mx.academy_check.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.mx.academy_check.Constants.Constants.BASE_URL;

public class BaseFrag extends Fragment {

    private Context mContext;

    public Context getmContext() {
        if (mContext == null) {
            mContext = getContext();
        }
        return mContext;
    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToast(int msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void movePage(Fragment fragment, String tag) {
        if (isCheck(tag)) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    protected void movePage(Fragment fragment, String tag, String bundleKey, Parcelable bundleValue) {
        if (isCheck(tag)) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(bundleKey, bundleValue);

        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_frame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    protected void removeBackStack(String tag) {
        getFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected void setToolbar(int arrowVisiblity, String title) {
        ((TextView) getActivity().findViewById(R.id.title_textview)).setText(title);
        getActivity().findViewById(R.id.back_img_btn).setVisibility(arrowVisiblity);
        getActivity().findViewById(R.id.back_img_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private Boolean isCheck(String tag) {
        return getFragmentManager().getFragments().get(0).getTag().equals(tag);
    }

    public int getViewHeight(View view) {
        final int[] height = new int[1];
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height[0] = getView().getHeight();
            }
        });

        return height[0];
    }

    // 화면이 내려가는걸 방지한다.
    public void setScrollViewPos(ScrollView scroll) {
        if (scroll.getParent() instanceof RelativeLayout) {
            RelativeLayout lineLayout = (RelativeLayout) scroll.getParent();
            int x = lineLayout.getLeft();
            int y = lineLayout.getTop();
            scroll.smoothScrollTo(x, y);
        } else if (scroll.getParent() instanceof LinearLayout) {
            LinearLayout lineLayout = (LinearLayout) scroll.getParent();
            int x = lineLayout.getLeft();
            int y = lineLayout.getTop();
            scroll.smoothScrollTo(x, y);
        }
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

}
