package com.nashesz.cudroid.WebParser;

import android.content.Context;

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
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import 	javax.xml.parsers.DocumentBuilderFactory;
/**
 * Created by Dani on 31/01/2015.
 *
 * @author Dani
 * @version 0.0.1
 */
public class CUParser
{
    private boolean _logged;
    private Context _ctx;
    private CUContainer _data;
    private String _novedades;

    public CUParser(Context context, CUContainer Data)
    {
        _ctx = context;
        _data = Data;
        _logged = false;
    }

    //
    public boolean logIn(String user, String pass) {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(_ctx.getString(R.string.CU_URL));

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(_ctx.getString(R.string.CU_TAG_LOG_USER), user));
            nameValuePairs.add(new BasicNameValuePair(_ctx.getString(R.string.CU_TAG_LOG_PASSWORD), pass));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            int stCode = response.getStatusLine().getStatusCode();
            if (stCode == 200) {
                InputStream stream = response.getEntity().getContent();
                _novedades = toHtml(stream);
                stream.close();
            } else {
                throw new HttpResponseException(stCode, "LOGIN: Failed to retrieve response from login");
            }
        } catch (HttpResponseException e){
            // TODO Auto-generated catch block
            return false;
        } catch (UnsupportedEncodingException e){
            // TODO Auto-generated catch block
            return false;
        } catch (IllegalStateException e){
            // TODO Auto-generated catch block
            return false;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }

        return true;
    }

    private String toHtml(InputStream stream) throws IOException {
        String encoding = "UTF-8";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = stream.read(buffer)) != -1)
        {
            baos.write(buffer, 0, length);
        }

        return new String(baos.toByteArray(), encoding);
    }

    public String getUmbria() throws CUException {
        if(!_logged) {
            throw new CUException("You have not logged in Comunidad Umbria");
        }

        try {

            if (_novedades != null) {
                _novedades = null;
            }

            URL url = new URL(_ctx.getString(R.string.CU_URL));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            InputStream stream = conn.getInputStream();
            _novedades = toHtml(stream);
            stream.close();

        } catch (IOException e){
            // TODO Auto-generated catch block
        }

        return _novedades;
    }

    public void ParseUmbria() throws CUException
    {
        if(!_logged) {
            throw new CUException("You have not logged in Comunidad Umbria");
        }
        if (_novedades == null) {
            getUmbria();
        }


    }

}
