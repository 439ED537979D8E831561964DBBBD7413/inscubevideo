package com.test.aashi.inscubenewbuild;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ReadActivity extends AppCompatActivity {
    private  PrefManager prefManager;
    ViewPager viewPager;
    private int[] layouts;
    MyViewPagerAdapter myViewPagerAdapter;
    CardView gotit;
    TextView change;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String launch ="0";
    TabLayout tab1;
    LinearLayout tab2,tab3,tab4,tab5;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        viewPager = (ViewPager) findViewById(R.id.view_pager1);
        gotit =(CardView)findViewById(R.id.gotit);
        prefManager = new PrefManager(this);
        change =(TextView)findViewById(R.id.change);
        sharedPreferences = getApplicationContext().getSharedPreferences("explain",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tab1 =(TabLayout)findViewById(R.id.tab_layout1);
        tab2 =(LinearLayout)findViewById(R.id.tab_layout2);
        tab3 =(LinearLayout)findViewById(R.id.tab_layout3);
        tab4=(LinearLayout)findViewById(R.id.tab_layout4);
        tab1.setVisibility(View.VISIBLE);
        layouts = new int[]
        {
                R.layout.explain,
                R.layout.explain1,
               R.layout.explain2,
                R.layout.explain3,
                R.layout.explain4,
        };
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        gotit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int current = getItem(+1);
                if (current < layouts.length)
                {
                    viewPager.setCurrentItem(current);
                }
                else
                {
                   launchHomeScreen();
                }
            }
        });
    }
    private int getItem(int i)
    {
        return viewPager.getCurrentItem() + i;
    }
    private void launchHomeScreen()
    {
        //prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(ReadActivity.this, StoryPage.class));
        editor.putString("explains","1");
        editor.apply();
        finish();
    }
    public class MyViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;
        public MyViewPagerAdapter()
        {
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }
        @Override
        public int getCount()
        {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object obj)
        {
            return view == obj;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }



    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {



           if ( position == 2)
            {
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.VISIBLE);
                tab3.setVisibility(View.GONE);
            }
            if (position == 3)
            {
                tab3.setVisibility(View.VISIBLE);
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);
                tab1.setVisibility(View.GONE);

            }
            if(position == 4)
            {
               tab4.setVisibility(View.VISIBLE);
                tab3.setVisibility(View.GONE);
                tab1.setVisibility(View.GONE);
                tab2.setVisibility(View.GONE);


            }

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1)
            {
                // last page. make button text to GOT IT
                change.setText(getString(R.string.start));
            }
            else
            {
                // still pages are left
                change.setText(getString(R.string.next));
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

}
