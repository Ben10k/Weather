package com.birdisaword.birdsweather;

import android.app.AlarmManager;
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
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        Intent configIntent = new Intent(context, WidgetService.class);
        PendingIntent configPendingIntent = PendingIntent.getService(context, 0, configIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.wbutton, configPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("update_widget")) {
                    // Manual or automatic widget update started

                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                            R.layout.weather_widget);

                    // Update text, images, whatever - here
                    String temp = intent.getStringExtra("T");
                    String wind = intent.getStringExtra("V");
                    temp = temp.substring(12);
                    wind = wind.substring(7);
                    remoteViews.setTextViewText(R.id.TV, temp);
                    remoteViews.setTextViewText(R.id.TV2, wind);

                    // Trigger widget layout update
                    AppWidgetManager.getInstance(context).updateAppWidget(
                            new ComponentName(context, WeatherWidget.class), remoteViews);


                }
        super.onReceive(context, intent);
    }
}

