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
public class CUParser {

    private Context _ctx;
    private CUContainer _data;

    public CUParser(Context _ctx) {
        _ctx = _ctx;
        _data = new CUContainer();
    }




}
