package com.github.markhm.mapbox;

import com.github.markhm.mapbox.directions.DirectionsResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Util
{
    private static Log log = LogFactory.getLog(Util.class);

    public static String readLineByLine(String resourceName)
    {
        log.info("Loading DirectionsResponse from "+resourceName);

        InputStream is = DirectionsResponse.class.getClassLoader().getResourceAsStream(resourceName);

        String result = null;
        try
        {
            result = readFromInputStream(is);
            is.close();
        }
        catch (IOException e)
        {
            log.error(e);
        }

        return result;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException
    {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                resultStringBuilder.append(line).append("\n");
            }
        }
        log.info("resultStringBuilder.toString().length(): "+ resultStringBuilder.toString().length());
        // log.info("resultStringBuilder.toString(): "+ resultStringBuilder.toString());
        return resultStringBuilder.toString();
    }

}
