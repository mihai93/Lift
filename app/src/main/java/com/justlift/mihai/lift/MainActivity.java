package com.justlift.mihai.lift;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lift.Back");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_action_icon);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        CustomTabLayout tabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        for (int ii = 0; ii < tabLayout.getTabCount(); ii++) {
//            tabLayout.getTabAt(ii).setCustomView(R.layout.tab_view);
//        }

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int tabNumber = 0;

        switch(day)
        {
            case Calendar.SUNDAY:
                tabNumber = 0;
                break;
            case Calendar.MONDAY:
                tabNumber = 1;
                break;
            case Calendar.TUESDAY:
                tabNumber = 2;
                break;
            case Calendar.WEDNESDAY:
                tabNumber = 3;
                break;
            case Calendar.THURSDAY:
                tabNumber = 4;
                break;
            case Calendar.FRIDAY:
                tabNumber = 5;
                break;
            case Calendar.SATURDAY:
                tabNumber = 6;
                break;
        }

        tabLayout.getTabAt(tabNumber).select();

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
                switch(position){
                    case 0:
                        getSupportActionBar().setTitle("Lift.Rest");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Lift.Chest");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Lift.Back");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Lift.Chest/Cardio");
                        break;
                    case 4:
                        getSupportActionBar().setTitle("Lift.Legs");
                        break;
                    case 5:
                        getSupportActionBar().setTitle("Lift.Shoulders+Chest");
                        break;
                    case 6:
                        getSupportActionBar().setTitle("Lift.Rest");
                        break;
                }
            }
        });

    }

//    public void setActionBarTitle(String title){
//        getSupportActionBar().setTitle(title);
//    }

    private void setupViewPager(ViewPager viewPager) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        SimpleDateFormat df = new SimpleDateFormat("d");
//        SimpleDateFormat df2 = new SimpleDateFormat("E");
//        String formattedDate = df.format(cal.getTime());

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "SUN\n" + df.format(cal.getTime()));    // Tu W Th F Sa Su M
        cal.add(Calendar.DAY_OF_WEEK, 1);
        adapter.addFragment(new TwoFragment(), "MON\n" + df.format(cal.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        adapter.addFragment(new ThreeFragment(), "TUE\n" + df.format(cal.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        adapter.addFragment(new FourFragment(), "WED\n" + df.format(cal.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        adapter.addFragment(new FiveFragment(), "THU\n" + df.format(cal.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        adapter.addFragment(new SixFragment(), "FRI\n" + df.format(cal.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        adapter.addFragment(new SevenFragment(), "SAT\n" + df.format(cal.getTime()));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
