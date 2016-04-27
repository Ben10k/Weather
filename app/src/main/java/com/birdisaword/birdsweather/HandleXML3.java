package com.birdisaword.birdsweather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sairamkrishna on 4/11/2015.
 */
public class HandleXML3 {




    private String Date[];
    public String[] getDate(){
        return Date;
    }

    private String Temp[];
    public String[] getTemp(){
        return Temp;
    }

    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public HandleXML3(String url){
        this.urlString = url;
    }




    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;


        List<String> Days = new ArrayList<String>();
        List<String> Months = new ArrayList<String>();

        List<Float> hTemps = new ArrayList<Float>();
        List<Float> lTemps = new ArrayList<Float>();
        boolean isTemp = true;
        boolean T = false;



        try {
            event = myParser.getEventType();






            while (event != XmlPullParser.END_DOCUMENT) {
               String name=myParser.getName();



                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        //if(T) {
                            if (name.equals("day")) {
                                Days.add(text);
                            }
                            if (name.equals("monthname")) {
                                Months.add(text);
                            }


                        if (name.equals("celsius")) {
                            hTemps.add(Float.parseFloat(text));

                        }


                        /*
                            if (name.equals("celsius") && isTemp) {
                                hTemps.add(Float.parseFloat(text));
                                isTemp = false;
                            }
                            if (name.equals("celsius") && !isTemp) {
                                lTemps.add(Float.parseFloat(text));
                                isTemp = true;
                            }
                            */
                            else {
                            }

                /*        }

                        else
                        {
                            if("simpleforecast" == myParser.getName())
                                T= true;
                        }
                        */
                        break;
                }
                event = myParser.next();
            }
            Date = new String[Days.size()];
            Temp = new String[hTemps.size()];

            for(int i = 0; i < Days.size();i++) {
                Date[i]= Months.toArray()[i].toString()+" "+Days.toArray()[i].toString();
                Temp[i]= hTemps.toArray()[2*i+1].toString()+" - " +hTemps.toArray()[2*i].toString();
            }

            parsingComplete = false;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}