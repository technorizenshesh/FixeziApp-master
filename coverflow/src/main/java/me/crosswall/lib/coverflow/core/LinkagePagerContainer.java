package me.crosswall.lib.coverflow.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import androidx.fragment.app.Fragment;
import androidx.core.view.LinkagePager;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * PagerContainer: A layout that displays a ViewPager with its children that are outside
 * the typical pager bounds.
 *
 * @see(<a href = "https://gist.github.com/devunwired/8cbe094bb7a783e37ad1"></>)
 */
public class LinkagePagerContainer extends FrameLayout implements LinkagePager.OnPageChangeListener {

    private LinkagePager mPager;
    private boolean mNeedsRedraw = false;
    private boolean isOverlapEnabled = false;
    private PageItemClickListener pageItemClickListener;


    public LinkagePagerContainer(Context context) {
        super(context);
        init();
    }

    public LinkagePagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinkagePagerContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //Disable clipping of children so non-selected pages are visible
        setClipChildren(false);

        //Child clipping doesn't work with hardware acceleration in Android 3.x/4.x
        //You need to set this value here if using hardware acceleration in an
        // application targeted at these releases.
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setOverlapEnabled(boolean overlapEnabled) {
        isOverlapEnabled = overlapEnabled;
    }

    public void setPageItemClickListener(PageItemClickListener pageItemClickListener) {
        this.pageItemClickListener = pageItemClickListener;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        try {
            mPager = (LinkagePager) getChildAt(0);
            mPager.addOnPageChangeListener(this);
        } catch (Exception e) {
            throw new IllegalStateException("The root child of PagerContainer must be a ViewPager");
        }
    }

    public LinkagePager getViewPager() {
        return mPager;
    }

    private Point mCenter = new Point();
    private Point mInitialTouch = new Point();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("@@@","w:"+w + "\n" + "h: "+ h);
        mCenter.x = w / 2;
        mCenter.y = h / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialTouch.x = (int) ev.getX();
                mInitialTouch.y = (int) ev.getY();
                ev.offsetLocation(mCenter.x - mInitialTouch.x, mCenter.y - mInitialTouch.y);
                break;
            case MotionEvent.ACTION_UP:
                int delta = Utils.isInNonTappableRegion(getWidth(),mPager.getWidth(),mInitialTouch.x, ev.getX());
                if(delta!=0){
                    int preItem = mPager.getCurrentItem();
                    int currentItem = preItem + delta;
                    mPager.setCurrentItem(currentItem);
                    ev.offsetLocation(mCenter.x - mInitialTouch.x, mCenter.y - mInitialTouch.y);
                    if (pageItemClickListener != null) {
                        pageItemClickListener.onItemClick(mPager.getChildAt(currentItem), currentItem);
                    }
                }
                break;
        }


        return mPager.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mNeedsRedraw) invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (isOverlapEnabled) {
            //Counter for loop
            int loopCounter = 0;
            int PAGER_LOOP_THRESHOLD = 2;

            //SET THE START POINT back 2 fragments
            if (position >= PAGER_LOOP_THRESHOLD) {
                loopCounter = position - PAGER_LOOP_THRESHOLD;
            }
            do {
                if (loopCounter < mPager.getAdapter().getCount()) {

                    Object object = mPager.getAdapter().instantiateItem(mPager, loopCounter);
                    //Elevate the Center View if it's the selected position and de-elevate the left and right fragment

                    if(object instanceof Fragment){
                        Fragment fragment = (Fragment) object;
                        if (loopCounter == position) {
                            ViewCompat.setElevation(fragment.getView(), 8.0f);
                        } else {
                            ViewCompat.setElevation(fragment.getView(), 0.0f);
                        }
                    }else{
                        View view = (View) object;
                        if (loopCounter == position) {
                            ViewCompat.setElevation(view, 8.0f);
                        } else {
                            ViewCompat.setElevation(view, 0.0f);
                        }
                    }
                }
                loopCounter++;
            } while (loopCounter < position + PAGER_LOOP_THRESHOLD);
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mNeedsRedraw = (state != ViewPager.SCROLL_STATE_IDLE);
    }
}