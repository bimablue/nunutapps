package com.blackbeltcoder.nunut.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.component.CustomFormDialog;
import com.blackbeltcoder.nunut.component.CustomInfoDialog;
import com.blackbeltcoder.nunut.component.CustomProgressDialog;
import com.blackbeltcoder.nunut.component.CustomTextView;
import com.blackbeltcoder.nunut.global.App;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Hashtable;

public class WelcomeScreenActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private App app;
    private FirebaseAuth firebaseAuth;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnPrev, btnNext;
    private CustomTextView tvAccountExist;
    private CustomProgressDialog dialogProgress;
    private CustomInfoDialog dialogInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        app = (App) getApplicationContext();
        if (!app.isFirstTimeLaunch()) {
            launchHomeScreen(0);
        }

        dialogInfo = new CustomInfoDialog(WelcomeScreenActivity.this,
                ConstantVariable.TITLE_INFO,
                getResources().getString(R.string.msg_login_failed),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInfo.dismiss();
                    }
                });

        dialogProgress = new CustomProgressDialog(WelcomeScreenActivity.this);

        bindActivity();
        loadObject();
    }

    private void bindActivity(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnNext = (Button) findViewById(R.id.btn_next);
        tvAccountExist = (CustomTextView) findViewById(R.id.tvAccountExist);

        layouts = new int[]{
                R.layout.content_welcome_screen,
                R.layout.content_welcome_screen_2,
                R.layout.content_welcome_screen_3};

        myViewPagerAdapter = new MyViewPagerAdapter();
    }

    private void loadObject(){
        setSupportActionBar(toolbar);

        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(-1);
                viewPager.setCurrentItem(current);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen(0);
                }
            }
        });

        tvAccountExist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hashtable<String, String> param = new Hashtable<String, String>();
                new CustomFormDialog(WelcomeScreenActivity.this, param
                        , new CustomFormDialog.OnResultFormDialogListener() {

                    @Override
                    public void OnResultFormDialog(String email, String password) {
                        // TODO Auto-generated method stub
                        dialogProgress.show();

                        /*
                        * REST
                        * */
                        /*
                        NuterModel nmObj = new NuterModel();
                        nmObj.email = email;
                        nmObj.phone = phone;

                        Intent intent = new Intent(getActivity().getApplicationContext(), RestService.class);
                        intent.putExtra(ConstantVariable.BROADCAST_KEY, ConstantVariable.BROADCAST_KEY_LOGIN);
                        intent.putExtra(ConstantVariable.BROADCAST_OBJ, nmObj);
                        getActivity().startService(intent);
                        */

                        /*
                        * FIREBASE
                        * */
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(WelcomeScreenActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(dialogProgress.isShowing())
                                            dialogProgress.dismiss();

                                        if (!task.isSuccessful()) {
                                            dialogInfo.show();
                                        } else {
                                            launchHomeScreen(6);
                                        }
                                    }
                                });
                    }
                }).show();
            }
        });

        addBottomDots(0);
        btnPrev.setVisibility(View.GONE);
        tvAccountExist.setVisibility(View.GONE);
    }

    private void launchHomeScreen(int pageMode){
        app.setFirstTimeLaunch(false);
        Intent intent = new Intent(WelcomeScreenActivity.this, MainActivity.class);
        intent.putExtra("ACT_MODE", pageMode);
        startActivity(intent);
        finish();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == layouts.length - 1) {
                btnNext.setText("START");
                btnPrev.setVisibility(View.VISIBLE);
                tvAccountExist.setVisibility(View.VISIBLE);
            } else if (position == 0) {
                btnPrev.setVisibility(View.GONE);
                tvAccountExist.setVisibility(View.GONE);
            } else {
                btnNext.setText("NEXT");
                btnPrev.setVisibility(View.VISIBLE);
                tvAccountExist.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
