package com.ezimgur.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 5/25/13
 * Time: 11:28 AM
 */
public class CommunityFragmentAdapter extends FragmentStatePagerAdapter {

    private Fragment mFragmentAtZero;
    private FragmentManager mFragmentManager;

    public CommunityFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (mFragmentAtZero == null) {
                    mFragmentAtZero = MessagesFragment.newInstance(mListener);
                }
                return mFragmentAtZero;
            case 1:
                return ImguriansFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "messages";
            case 1:
                return "imgurians";
            default:
                return super.getPageTitle(position);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof MessagesFragment && mFragmentAtZero instanceof MessageDetailFragment)
            return POSITION_NONE;
        if (object instanceof MessageDetailFragment && mFragmentAtZero instanceof MessagesFragment)
            return POSITION_NONE;
        if (object instanceof  MessagesFragment && mFragmentAtZero instanceof CreateMessageFragment)
            return POSITION_NONE;
        if (object instanceof CreateMessageFragment && mFragmentAtZero instanceof MessagesFragment)
            return POSITION_NONE;

        return POSITION_UNCHANGED;
    }

    OnMessagesFragmentChangedListener mListener = new OnMessagesFragmentChangedListener() {
        @Override
        public void onSwitchToNext(int id) {

            mFragmentManager.beginTransaction().remove(mFragmentAtZero)
                    .commit();
            if ( id != 0 && mFragmentAtZero instanceof MessagesFragment){
                mFragmentAtZero = MessageDetailFragment.newInstance(id);
            }else if (mFragmentAtZero instanceof MessagesFragment){ // Instance of NextFragment
                mFragmentAtZero = CreateMessageFragment.newInstance();
            } else {
                mFragmentAtZero = MessagesFragment.newInstance(mListener);
            }
            notifyDataSetChanged();
        }

        @Override
        public void composeTo(String username) {
            //mFragmentManager.beginTransaction().remove(mFragmentAtZero).commit();

            mFragmentAtZero = CreateMessageFragment.newInstance(username);
            notifyDataSetChanged();
        }
    };

    public void goToMessageDetail(int id){
        mListener.onSwitchToNext(id);
    }

    public void composeMessage(String userName){
        mListener.composeTo(userName);
    }

    public boolean onBackPressed(int currentPos){
        if (currentPos == 0 && mFragmentAtZero instanceof MessageDetailFragment){
            mListener.onSwitchToNext(0);
            return true;
        } else if (currentPos == 0 && mFragmentAtZero instanceof CreateMessageFragment && !((CreateMessageFragment)mFragmentAtZero).wasFirstFragment()){
            mListener.onSwitchToNext(0);
            return true;
        }
        return false;
    }

    public interface OnMessagesFragmentChangedListener {
        void onSwitchToNext(int id);
        void composeTo(String username);
    }
}
