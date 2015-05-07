/*
 * Licensed Materials - Property of IBM
 *
 * 5747-SM3
 *
 * (C) Copyright IBM Corp. 1999, 2012 All Rights Reserved.
 *
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 *
 */
package net.nym.library.util;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * Provides static methods for creating and showing notifications to the user.
 *
 */
public class Notify {

  /** Message ID Counter **/
  private static int MessageID = 0;

  /**
   * Displays a notification in the notification area of the UI
   * @param context Context from which to create the notification
   * @param messageString The string to display to the user as a message
   * @param smallIconId drawable id
   * @param intent The intent which will start the activity when the user clicks the notification
   * @param notificationTitle The resource reference to the notification title
   * @return messageID
   */
  public static int notifcation(Context context, String messageString,int smallIconId, Intent intent, int notificationTitle) {

    //Get the notification manage which we will use to display the notification
    String ns = Context.NOTIFICATION_SERVICE;
    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

    Calendar.getInstance().getTime().toString();

    long when = System.currentTimeMillis();

    //get the notification title from the application's strings.xml file
    CharSequence contentTitle = context.getString(notificationTitle);

    //the message that will be displayed as the ticker
    String ticker = contentTitle + " " + messageString;

    //build the pending intent that will start the appropriate activity
    PendingIntent pendingIntent = PendingIntent.getActivity(context,
        3, intent, 0);

    //build the notification
    Builder notificationCompat = new Builder(context);
    notificationCompat.setAutoCancel(true)
        .setContentTitle(contentTitle)
        .setContentIntent(pendingIntent)
        .setContentText(messageString)
        .setTicker(ticker)
        .setWhen(when)
        .setSmallIcon(smallIconId);

    Notification notification = notificationCompat.build();
//    notification.defaults |= Notification.DEFAULT_ALL;
    notification.defaults = Notification.DEFAULT_ALL;
    //display the notification
    mNotificationManager.notify(MessageID, notification);
    return MessageID++;

  }




}
