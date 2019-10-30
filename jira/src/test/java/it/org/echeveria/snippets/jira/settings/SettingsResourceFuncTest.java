package it.org.echeveria.snippets.jira.settings;

import static org.junit.Assert.assertEquals;

import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.echeveria.snippets.jira.settings.SettingsResourceModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SettingsResourceFuncTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {

        String baseUrl = System.getProperty("baseurl");
        String resourceUrl = baseUrl + "/rest/settingsresource/1.0/message";

        RestClient client = new RestClient();
        Resource resource = client.resource(resourceUrl);

        SettingsResourceModel message = resource.get(SettingsResourceModel.class);

        assertEquals("wrong message","Hello World",message.getMessage());
    }
}
