package com.klinker.android.twitter_l.settings;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.adapters.ChangelogAdapter;


public class SettingsLinkDrawerClickListener extends SettingsDrawerClickListener {
    public SettingsLinkDrawerClickListener(Context context, DrawerLayout drawerLayout, ListView drawerList, ViewPager vp, LinearLayout drawer) {
        super(context, drawerLayout, drawerList, vp, drawer);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        Intent intent;

        final int mPos = position;

        if (mPos < 2) { // one of the settings pages
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDrawerLayout.closeDrawer(Gravity.START);
                }
            }, 300);

            viewPager.setCurrentItem(mPos + 7, true);
        } else if (mPos == 2) { // changelog

            final ListView list = new ListView(context);
            list.setDividerHeight(0);

            new AsyncTask<Spanned[], Void, Spanned[]>() {
                @Override
                public Spanned[] doInBackground(Spanned[]... params) {
                    return com.klinker.android.twitter_l.utils.XmlChangelogUtils.parse(context);
                }

                @Override
                public void onPostExecute(Spanned[] result) {
                    list.setAdapter(new ChangelogAdapter(context, result));
                }
            }.execute();

            new AlertDialog.Builder(context)
                    .setTitle(R.string.changelog)
                    .setView(list)
                    .setPositiveButton(R.string.ok, null)
                    .show();

        } else if (mPos == 3) { // rate it option
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    try {
                        context.startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "Couldn't launch the market", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 200);

        }
    }
}