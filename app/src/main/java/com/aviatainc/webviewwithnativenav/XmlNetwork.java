package com.aviatainc.webviewwithnativenav;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.aviatainc.webviewwithnativenav.R;
import com.aviatainc.webviewwithnativenav.XmlParser.Entry;
import com.aviatainc.webviewwithnativenav.XmlParser.PEMenuItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

//AsyncTask<Params, Progress, Result>
//meaning in this instance <String used by doInBackground(), Void since not currently using Progress function, List<PEMenuItem> being returned because objects!>
public class XmlNetwork extends AsyncTask< String, Void, List<PEMenuItem> > {
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL =
            "http://wwwtest.onlineregister.com/~jrivera/android/xml/menu.xml";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;

    // The user's current network preference setting.
    public static String sPref = null;

    // The BroadcastReceiver that tracks network connectivity changes.
    //private NetworkReceiver receiver = new NetworkReceiver();


    @Override
    public List<PEMenuItem> doInBackground(String... urls) {
    //protected String doInBackground(String... urls) {
        try {
            return loadMenuXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            //return getResources().getString(R.string.connection_error);
            //return "connection error";
            return null;
        } catch (XmlPullParserException e) {
            //Log.i("Error!", "Error: " + e);
            //e.printStackTrace();
            //return getResources().getString(R.string.xml_error);
            //String temp = e.getMessage();
            //return "xml error";
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<PEMenuItem> result) {
    //protected void onPostExecute(String result) {
        //setContentView(R.layout.main);
        // Displays the HTML string in the UI via a WebView
        //WebView myWebView = (WebView) findViewById(R.id.webview);
        //myWebView.loadData(result, "text/html", null);
        //String temp = result;
        List<PEMenuItem> temp = result;
        List<PEMenuItem> temp2 = result;

        //final MenuItemsAdapter menuItemsAdapter = new MenuItemsAdapter(
        //        getActionBar().getThemedContext(),
        //        android.R.layout.simple_list_item_activated_1);

        //whatever.setNewValues(0;)

    }

    // Uploads XML from stackoverflow.com, parses it, and combines it with
    // HTML markup. Returns HTML string.
    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser stackOverflowXmlParser = new XmlParser();
        List<Entry> entries = null;
        String title = null;
        String url = null;
        String summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        // Checks whether the user set the preference to include summary text
        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        StringBuilder htmlString = new StringBuilder();
        //htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
        //htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
        //        formatter.format(rightNow.getTime()) + "</em>");

        try {
            stream = downloadUrl(urlString);
            entries = stackOverflowXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (Entry entry : entries) {
            htmlString.append("<p><a href='");
            htmlString.append(entry.link);
            htmlString.append("'>" + entry.title + "</a></p>");
            // If the user set the preference to include summary text,
            // adds it to the display.
            //if (pref) {
            //    htmlString.append(entry.summary);
            //}
        }

        return htmlString.toString();
    }



    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }



    private List<PEMenuItem> loadMenuXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        XmlParser stackOverflowXmlParser = new XmlParser();
        List<PEMenuItem> menuItems = null;
        String title = null;
        String url = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        // Checks whether the user set the preference to include summary text
        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        StringBuilder htmlString = new StringBuilder();
        //htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
        //htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
        //        formatter.format(rightNow.getTime()) + "</em>");

        try {
            stream = downloadUrl(urlString);
            menuItems = stackOverflowXmlParser.parseMenu(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (PEMenuItem menuItem : menuItems) {
            htmlString.append("<p><a href='");
            htmlString.append(menuItem.link);
            htmlString.append("'>" + menuItem.title + "</a></p>");
            // If the user set the preference to include summary text,
            // adds it to the display.
            //if (pref) {
            //    htmlString.append(entry.summary);
            //}
        }

        //return htmlString.toString();
        return menuItems;
    }
}
