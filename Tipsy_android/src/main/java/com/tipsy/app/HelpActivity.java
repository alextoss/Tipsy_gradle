package com.tipsy.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import java.util.List;
import java.util.Vector;

import com.tipsy.commun.User;

/**
 * Created by Alexandre on 12/12/13.
 */
public class HelpActivity extends FragmentActivity implements ViewSwitcher.ViewFactory {

    protected PagerAdapter mPagerAdapter;
    protected ImageView focustep;
    protected RelativeLayout layout;
    protected ImageSwitcher switcher;
    protected int index = 0;
    protected final int[] images = {R.drawable.first_bg_flou, R.drawable.second_bg_flou,
            R.drawable.last_bg_flou};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.act_help);

        focustep = (ImageView) findViewById(R.id.focustep);
        focustep.setImageDrawable(getResources().getDrawable(R.drawable.focustepone));
        switcher = (ImageSwitcher) findViewById(R.id.switcher);
        switcher.setFactory(this);
        switcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        switcher.setImageResource(images[index]);

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(this, Help1Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Help2Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Help3Fragment.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        ViewPager pager = (ViewPager) super.findViewById(R.id.pager);
        // Affectation de l'adapter au ViewPager
        pager.setAdapter(this.mPagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        switcher.setImageResource(images[0]);
                        focustep.setImageDrawable(getResources().getDrawable(R.drawable.focustepone));
                        break;
                    case 1:
                        switcher.setImageResource(images[1]);
                        focustep.setImageDrawable(getResources().getDrawable(R.drawable.focusteptwo));
                        break;
                    case 2:
                        switcher.setImageResource(images[2]);
                        focustep.setImageDrawable(getResources().getDrawable(R.drawable.focustepthree));
                        break;
                    default:
                        break;
                }
            }
        });

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("Connected", false))
                    finish();
                else {
                    TipsyApp app = (TipsyApp) getApplication();
                    app.setSkipHelp(HelpActivity.this, true);
                    User.tryLogin(HelpActivity.this);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        //On fournit à l'adapter la liste des fragments à afficher
        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        return imageView;
    }

}