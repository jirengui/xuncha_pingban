package p03.example.li.xuncha;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by li on 2018/1/14.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private List<Fragment> mFragmentList = null;
    public FragmentPagerAdapter(FragmentManager mFragmentManager, ArrayList<Fragment> fragmentList) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
    }
    /**
     * titles是给TabLayout设置title用的
     *
     * * @param mFragmentManager
     * @param fragmentList
     * */
    public FragmentPagerAdapter(FragmentManager mFragmentManager, List<Fragment> fragmentList) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
    }
            /**
      * 描述：获取数量.
      *
      * @return the count
      * @see android.support.v4.view.PagerAdapter#getCount()
      */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    /**
     * 描述：获取索引位置的Fragment.
     * *
     * @param position the position
     * @return the item
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < mFragmentList.size()) {
            fragment = mFragmentList.get(position);
        } else {
            fragment = mFragmentList.get(0);
        }
        return fragment;
    }
}

