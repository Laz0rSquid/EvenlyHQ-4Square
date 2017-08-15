package com.evenly.marcus_kopp.evenlyhq4square;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EvenlyStartScreen extends ListActivity {
    ArrayList<FoursquareVenue> venuesList;

    // Foursquare API key and secret
    final String CLIENT_ID = "RL1CUD4M2TIKLSO2NXMBOEUVROTAFNPJZNRXHNDWXXMB0Q4K";
    final String CLIENT_SECRET = "52UEPPOGIYSSHTQP5HO3UCHRBGDA1DAAM1DOGD1KMEW2Z0GG";

    // hardcoded position of EvenlyHQ
    final String latitude = "52.500342";
    final String longitude = "13.425170";

    // progress bar :3
    private ProgressBar progressBar = null;

    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progessbar);
        // calls to Foursquare venues search API
        new Foursquare().execute();
    }

    private class Foursquare extends AsyncTask<View, Void, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            temp = makeCall("https://api.foursquare.com/v2/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20170815&ll=" + latitude + "," + longitude + "");
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {


            if (temp == null) {
                // error message if call failed
                Toast.makeText(EvenlyStartScreen.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            } else {
                // all things went right
                progressBar.setVisibility(View.GONE);
                // parseFoursquare venues search result
                venuesList = (ArrayList<FoursquareVenue>) parseFoursquare(temp);

                List<String> listTitle = new ArrayList<String>();

                for (int i = 0; i < venuesList.size(); i++) {
                    // make a list of the venus that are loaded in the list.


                    if (i == 0) {

                        TextView title = (TextView) findViewById(R.id.first_element);
                        title.setText(venuesList.get(i).getName() + "\n" + venuesList.get(i).getCategory() + "\n" + venuesList.get(i).getAddress() + "\n" + venuesList.get(i).getCity());

                    } else {
                        // show the name, the category and the city
                        listTitle.add(i - 1, venuesList.get(i).getName() + "\n" + venuesList.get(i).getCategory() + "\n" + venuesList.get(i).getAddress() + "\n" + venuesList.get(i).getCity());

                    }
                }

                // set the results to the list
                // and show them in the xml
                myAdapter = new ArrayAdapter<String>(EvenlyStartScreen.this, R.layout.row_layout, R.id.listText, listTitle);
                setListAdapter(myAdapter);
            }
        }
    }

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // trim the whitespaces
        return replyString.trim();
    }

    private static ArrayList<FoursquareVenue> parseFoursquare(final String response) {

        ArrayList<FoursquareVenue> temp = new ArrayList<FoursquareVenue>();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("response")) {
                if (jsonObject.getJSONObject("response").has("venues")) {
                    JSONArray venuesArray = jsonObject.getJSONObject("response").getJSONArray("venues");

                    for (int i = 0; i < venuesArray.length(); i++) {
                        FoursquareVenue poi = new FoursquareVenue();
                        if (venuesArray.getJSONObject(i).has("name")) {
                            poi.setName(venuesArray.getJSONObject(i).getString("name"));

                            if (venuesArray.getJSONObject(i).has("location")) {
                                if (venuesArray.getJSONObject(i).getJSONObject("location").has("address")) {
                                    poi.setAddress(venuesArray.getJSONObject(i).getJSONObject("location").getString("address"));
                                    if (venuesArray.getJSONObject(i).getJSONObject("location").has("city")) {
                                        poi.setCity(venuesArray.getJSONObject(i).getJSONObject("location").getString("city"));
                                    }


                                    if (venuesArray.getJSONObject(i).has("categories")) {
                                        if (venuesArray.getJSONObject(i).getJSONArray("categories").length() > 0) {
                                            if (venuesArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).has("icon")) {
                                                poi.setCategory(venuesArray.getJSONObject(i).getJSONArray("categories").getJSONObject(0).getString("name"));
                                            }
                                        }
                                    }

                                    temp.add(poi);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return temp;

    }
}
