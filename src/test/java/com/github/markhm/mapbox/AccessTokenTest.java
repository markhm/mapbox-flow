package com.github.markhm.mapbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

public class AccessTokenTest
{
    private Log log = LogFactory.getLog(AccessTokenTest.class);

    public AccessTokenTest()
    {}

    @Test
    public void testPropertyFileLoad()
    {
        String fileLocation = AccessToken.getJSFileLocation();
        log.warn("fileLocation = "+fileLocation);
    }

}
