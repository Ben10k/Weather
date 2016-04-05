package com.birdisaword.birdsweather;

import android.appwidget.AppWidgetProvider;


import android.appwidget.AppWidgetManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    public static String WIDGET_BUTTON = "com.birdisaword.birdsweather.WIDGET_BUTTON";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        /*CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);*/
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);*/

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, WeatherWidget.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

            Intent intent2 = new Intent(WIDGET_BUTTON);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.wbutton, pendingIntent2);
        }
    }

    @Override
    public void onEnabled(Context context) {
        /*Intent i = new Intent(context, WeatherWidget.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,0, i,0);
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        Intent i = new Intent();
        i.setClassName("com.birdisaword.birdsweather", "com.birdisaword.birdsweather.WeatherWidget");
        PendingIntent myPI = PendingIntent.getService(context, 0, i, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setOnClickPendingIntent(R.id.wbutton, myPI);
        ComponentName comp = new ComponentName(context.getPackageName(), WeatherWidget.class.getName());
        mgr.updateAppWidget(comp, views);*/
        // Enter relevant functionality for when the first widget is created
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        rv.setTextViewText(R.id.TV, "aaa");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

           // if (WIDGET_BUTTON.equals(intent.getAction())) {
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

