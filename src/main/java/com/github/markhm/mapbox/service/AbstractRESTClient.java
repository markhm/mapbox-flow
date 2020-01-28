package com.github.markhm.mapbox.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractRESTClient
{
    private static Log log = LogFactory.getLog(AbstractRESTClient.class);

    private String apiToken = null;

    boolean useHttps = true;

    public AbstractRESTClient(String apiToken)
    {
        this.apiToken = apiToken;
    }

    public abstract String getRootUrl();

    public String doGetMethod(String action) throws Exception
    {
        String address = getRootUrl() + action + "?access_token="+apiToken+"&steps=true&overview=full&geometries=geojson";
        log.info("Calling GET at: "+address);

        URL url = new URL(address);

        // Open connection
        HttpURLConnection con = openConnection(url);
        con.setRequestMethod("GET");

        return doCommunication(con, null);
    }

    public String doPostMethod(String action, JSONObject request) throws Exception
    {
        String address = getRootUrl() + action + "&access_token="+apiToken;
        log.info("POSTing to: "+address);

        URL url = new URL(address);

        HttpURLConnection con = openConnection(url);

        con.setRequestMethod("POST");

        return doCommunication(con, request);
    }

    private HttpURLConnection openConnection(URL url) throws Exception
    {
        log.info("Opening connection... ");

        HttpURLConnection con = null;
        if (useHttps)
        {
            con = (HttpsURLConnection) url.openConnection();
        }
        else
        {
            con = (HttpURLConnection) url.openConnection();
        }

        return con;
    }

    private void setRequestParameters(HttpURLConnection con)
    {
        // Set request properties
        con.setRequestProperty("charset", "UTF-8");
        con.setRequestProperty("Content-Type", "application/json");
        // con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Tell the connection to process output
        con.setDoOutput(true);

        // Set Basic Authorization credentials
        // String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        // con.setRequestProperty ("Authorization", basicAuth);

        // for the http connection, we follow the permalink
        con.setInstanceFollowRedirects(false);
    }

    private String doCommunication(HttpURLConnection con, JSONObject request)
    {
        // Setting the request properties, parameters and credentials
        setRequestParameters(con);

        StringBuffer content = new StringBuffer();

        try
        {
            // Encode/add parameters if available
//            if (param != null)
//            {
//                DataOutputStream out = new DataOutputStream(con.getOutputStream());
//                String paramString = ParameterStringBuilder.getParamsString(param);
//                log.info("Ecoded parameter String: " + paramString);
//                out.writeBytes(paramString);
//                out.flush();
//                out.close();
//            }

            if (request != null)
            {
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                String paramString = request.toString();
                out.writeBytes(paramString);
                out.flush();
                out.close();
            }

            // Reading the result
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
            {
                content.append(inputLine + "\n");
            }

            System.out.println("Content: "+content);

            int status = con.getResponseCode();
            con.disconnect();

            log.info("Status returned: " + status);
            log.info("Content returned:");

            prettyPrintResult(content.toString());

            in.close();
        }
        catch(Exception e)
        {
            StringBuffer errorContent = new StringBuffer();
            try
            {
                log.error(e);
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));

                String errorLine;
                while ((errorLine = streamReader.readLine()) != null)
                {
                    errorContent.append(errorLine);
                }
                log.error("Error content is: ");
                prettyPrintResult(errorContent.toString());
            }
            catch(Exception deeperError)
            {
                log.error(deeperError);
            }
        }

        return content.toString();
    }

    private static void prettyPrintResult(String jsonContent)
    {
        try
        {
            JSONObject json = new JSONObject(jsonContent);
            System.out.println(json.toString(2));
        }
        catch(JSONException j)
        {
            log.error(j);
        }
    }

}
