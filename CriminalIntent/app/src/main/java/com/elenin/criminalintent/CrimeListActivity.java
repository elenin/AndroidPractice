package com.elenin.criminalintent;


import android.support.v4.app.Fragment;

/**
 * Created by ele on 2017/1/10.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

}
