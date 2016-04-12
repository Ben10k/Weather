package com.birdisaword.birdsweather;

        import org.xmlpull.v1.XmlPullParser;
        import org.xmlpull.v1.XmlPullParserFactory;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;

/**
 * Created by Sairamkrishna on 4/11/2015.
 */
public class HandleXML {
    private String country = "city";
    private String temperature = "temp_c";
    private String humidity = "wind_kph";
    private String iconName =  "icon";
    private String iconUrl =  "icon";
    private String urlString = null;
    private String weather = "weather";
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public HandleXML(String url){
        this.urlString = url;
    }

    public String getCountry(){
        return country;
    }
    public String getWeather(){
        return weather;
    }
    public String getTemperature(){
        return temperature;
    }

    public String getHumidity(){
        return humidity;
    }

    public String getIconName(){
        return iconName;
    }


    public String getIconUrl(){
        return iconUrl;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        boolean miestas = true;

        float greitis;

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
                        if(name.equals("city")){
                            if(miestas)
                            {

                                if(!urlString.contains("pws")) {
                                    miestas = false;
                                    country = "Miestas: " + text;
                                }
                                else
                                    country = "Stotelė: " + text;;
                            }
                        }

                        else if(name.equals("wind_kph")){
                            greitis = Float.parseFloat(text);
                            greitis /=3.6;
                            humidity = "Vėjas: "+String.valueOf(Math.round(greitis))+" m/s";
                            // humidity = myParser.getAttributeValue(null,"value");
                        }

                        else if(name.equals("temp_c")){
                            temperature = "Temperatūra: "+text+" °C";
                            //= myParser.getAttributeValue(null,"value");
                        }
                        else if(name.equals("weather")){
                            weather = "Dabar: "+text;
                            //= myParser.getAttributeValue(null,"value");
                        }

                        else if(name.equals("icon")){
                            iconName = text;

                        }

                        else if(name.equals("icon_url")){
                            iconUrl = text;

                        }




                        else{
                        }
                        break;
                }
                event = myParser.next();
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