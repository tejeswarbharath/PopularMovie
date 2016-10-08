package udacity.popular.tejeswar.popularmovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by tejeswar on 10/4/2016.
 */

public class ScreenslideActivity extends FragmentActivity

{

    private static final int NUM_PAGES = 5;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.screen_slide_view);

        mPager=(ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlideAdapter(getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);

    }

    @Override

    public void onBackPressed()

    {

        if (mPager.getCurrentItem() == 0)

        {

            super.onBackPressed();

        }

        else

        {

            mPager.setCurrentItem(mPager.getCurrentItem() - 1);

        }

    }

    private class ScreenSlideAdapter extends FragmentStatePagerAdapter

    {

        public ScreenSlideAdapter(FragmentManager fm)

        {
            super(fm);
        }

        @Override

        public Fragment getItem(int position)

        {

            return new MovieFragment();

        }

        @Override

        public int getCount()

        {

            return NUM_PAGES;

        }

    }

}
