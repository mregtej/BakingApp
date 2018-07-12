package com.udacity.mregtej.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.mregtej.bakingapp.R;

import java.util.Random;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Get all ids
        ComponentName bakingWidget = new ComponentName(context,
                BakingAppWidgetProvider.class);
        int[] allBakingWidgetIds = appWidgetManager.getAppWidgetIds(bakingWidget);
        for (int bakingWidgetId : allBakingWidgetIds) {

            // TODO Retrieve recipe name and ingredients
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_bakingapp_layout);

            // TODO Update recipe data

            // Register an onClickListener
            Intent intent = new Intent(context, BakingAppWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.tv_widget_recipe_name, pendingIntent);
            appWidgetManager.updateAppWidget(bakingWidgetId, remoteViews);
        }
    }


}
