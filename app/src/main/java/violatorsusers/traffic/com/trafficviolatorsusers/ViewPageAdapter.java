package violatorsusers.traffic.com.trafficviolatorsusers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragListTitles = new ArrayList<>();

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragListTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragListTitles.get(position);
    }

    public void AddFragment(Fragment fragment,String title) {
        fragmentList.add(fragment);
        fragListTitles.add(title);
    }

}