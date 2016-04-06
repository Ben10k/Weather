package com.birdisaword.birdsweather;

import android.appwidget.AppWidgetProvider;


import android.appwidget.AppWidgetManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        Intent configIntent = new Intent(context, FullscreenActivity2.class);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.wbutton, configPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {



        super.onReceive(context, intent);


                if (intent.getAction().equals("update_widget")) {
                    // Manual or automatic widget update started

                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                            R.layout.weather_widget);

                    // Update text, images, whatever - here
                    remoteViews.setTextViewText(R.id.TV, intent.getStringExtra("T"));

                    // Trigger widget layout update
                    AppWidgetManager.getInstance(context).updateAppWidget(
                            new ComponentName(context, WeatherWidget.class), remoteViews);
                }
    }
}

