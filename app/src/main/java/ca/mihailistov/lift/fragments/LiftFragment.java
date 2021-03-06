package ca.mihailistov.lift.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import ca.mihailistov.lift.helpers.CustomTabLayout;
import ca.mihailistov.lift.helpers.NonSwipeableViewPager;
import ca.mihailistov.lift.R;
import ca.mihailistov.lift.adapters.LiftPagerAdapter;

/**
 * Created by mihai on 16-09-04.
 */
public class LiftFragment extends Fragment {
    public static NonSwipeableViewPager viewPager;
    public static LiftPagerAdapter adapter = null;
    private CustomTabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.lift_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (NonSwipeableViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new LiftPagerAdapter(getChildFragmentManager()));
        tabLayout = (CustomTabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(15).select();

        Calendar c = Calendar.getInstance();

        TextView calText = (TextView) getActivity().findViewById(R.id.calendar_text);
        calText.setVisibility(View.VISIBLE);
        calText.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        calText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLayout.getTabAt(15).select();
            }
        });
    }
}
