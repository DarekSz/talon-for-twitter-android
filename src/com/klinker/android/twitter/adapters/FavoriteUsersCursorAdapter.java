package com.klinker.android.twitter.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.klinker.android.twitter.R;
import com.klinker.android.twitter.data.sq_lite.FavoriteUsersDataSource;
import com.klinker.android.twitter.data.sq_lite.FavoriteUsersSQLiteHelper;
import com.klinker.android.twitter.ui.UserProfileActivity;
import com.klinker.android.twitter.ui.drawer_activities.FavoriteUsersActivity;
import com.klinker.android.twitter.utils.ImageUtils;

/**
 * Created by luke on 2/12/14.
 */
public class FavoriteUsersCursorAdapter extends PeopleCursorAdapter {
    public FavoriteUsersCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);
        final ViewHolder holder = (ViewHolder) view.getTag();

        final long id = cursor.getLong(cursor.getColumnIndex(FavoriteUsersSQLiteHelper.COLUMN_ID));
        holder.background.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.v("talon_favorite_users", "long clicked");
                new AlertDialog.Builder(context)
                        .setTitle(context.getResources().getString(R.string.removing_favorite) + "?")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    FavoriteUsersDataSource dataSource = new FavoriteUsersDataSource(context);
                                    dataSource.open();
                                    dataSource.deleteUser(id);
                                    dataSource.close();
                                    FavoriteUsersActivity.refreshFavs();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();

                return false;
            }
        });
    }
}