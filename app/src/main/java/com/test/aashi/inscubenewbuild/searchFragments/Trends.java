
package com.test.aashi.inscubenewbuild.searchFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.aashi.inscubenewbuild.R;


public class Trends extends LinearLayoutManager
{
    private boolean isScrollEnabled = true;

    public Trends(Context context) {
        super(context);
    }
    public void setScrollEnabled(boolean flag) {
    this.isScrollEnabled = flag;
}

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

}
