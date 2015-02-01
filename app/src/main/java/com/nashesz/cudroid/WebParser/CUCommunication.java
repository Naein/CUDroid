package com.nashesz.cudroid.WebParser;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.nashesz.cudroid.DataStructure.CUContainer;
import com.nashesz.cudroid.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Dani on 01/02/2015.
 */
public class CUCommunication extends AsyncTask<Void, CUContainer, Void>{


    private boolean _logged;
    private Context _ctx;
    private InputStream _novedades;

    public CUCommunication(Context context)
    {
        _ctx = context;
        _logged = false;
    }

    @Override
    /**
     * This function should be calling periodically to CU and updating CUContainers
     */
    protected Void doInBackground(Void... params) {
        //TODO change hardcoded by preferences
        long sleepTime = 1 /*minute*/;
        String user = "dummy";
        String pass = "dummy";

        try {

            Activity a = (Activity) _ctx;

            // Comm life-cycle : check if logged --> Start searching through branches. --> sleep
            while (!a.isFinishing()) {

                try {

                    resetHTML();

                    if (!checkLogIn()) {
                        logIn(user, pass);
                    }

                    if(_logged)
                    {
                        //TODO: Parsing by chunks
                        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document doc = builder.parse(_novedades);

                        Element e = doc.getElementById("a");

                    }

                }catch (CUException e){
                    //TODO automatic things: not critical exception
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.sleep(sleepTime * 1000 * 60);
            }

        } catch (InterruptedException e) {
            //TODO automatic things: critical exception
            e.printStackTrace();
        }
        return null;
    }

    private void resetHTML()
    {
        _novedades = null;
    }

    private boolean checkLogIn() {

        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document ret = null;
        InputStream in = getHTMLStream(_ctx.getString(R.string.CU_URL));

        String html = htmlStream_to_String(in);

        try {
            in.close();
        } catch (IOException e) {
            //TODO automatic things
            e.printStackTrace();
        }

        _logged = !html.contains( _ctx.getString(R.string.CU_TAG_LOGIN_TAG) );

        return _logged;
    }

    //
    public boolean logIn(String user, String pass) throws CUException {

        if(checkLogIn())
            return true;

        // Create a new HttpClient and Post Header
//        InputStream stream;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(_ctx.getString(R.string.CU_URL));

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(_ctx.getString(R.string.CU_TAG_LOGIN_USER), user));
            nameValuePairs.add(new BasicNameValuePair(_ctx.getString(R.string.CU_TAG_LOGIN_PASSWORD), pass));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            int stCode = response.getStatusLine().getStatusCode();
            if (stCode == 200) {
                _logged = true;

//                stream = response.getEntity().getContent();
//                _novedades = htmlStream_to_String(stream);
//                stream.close();
                _novedades = response.getEntity().getContent();

                return true;
            } else {
                throw new HttpResponseException(stCode, "LOGIN: Failed to retrieve response from login");
            }
        } catch (HttpResponseException e){
            // TODO Auto-generated catch block
        } catch (UnsupportedEncodingException e){
            // TODO Auto-generated catch block
        } catch (IllegalStateException e){
            // TODO Auto-generated catch block
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return false;
    }

    public InputStream getUmbria() throws CUException {

        if(!_logged) {
            throw new CUException("You have not logged in Comunidad Umbria");
        }

        if (_novedades == null) {
            //InputStream sIn = getHTMLStream(_ctx.getString(R.string.CU_URL));
            //_novedades = htmlStream_to_String(sIn);
            _novedades = getHTMLStream(_ctx.getString(R.string.CU_URL));
            //try {
            //    sIn.close();
            //}catch (IOException e){
            //    //TODO impossible to close stream
            //}
        }

        return _novedades;
    }

    private InputStream __getHTMLStream(String url) {
        InputStream stream = null;
        HttpURLConnection conn = null;

        try {

            URL urlconn = new URL(url);
            conn = (HttpURLConnection) urlconn.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            stream = conn.getInputStream();

            if(conn != null)
                conn.disconnect();

        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
        }

        return stream;
    }
    private InputStream getHTMLStream(String url) {
        InputStream stream;
        try {
            URL urlconn= new URL(url);
            stream = urlconn.openStream();

            return stream;
        } catch (Exception e) {
            //TODO automatic things
            return null;
        }
    }

    private String htmlStream_to_String(InputStream stream) {
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
