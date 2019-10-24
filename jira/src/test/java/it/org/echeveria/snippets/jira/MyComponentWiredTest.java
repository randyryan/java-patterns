package it.org.echeveria.snippets.jira;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import org.echeveria.snippets.jira.api.MyPluginComponent;
import com.atlassian.sal.api.ApplicationProperties;

@RunWith(AtlassianPluginsTestRunner.class)
public class MyComponentWiredTest {

  private final ApplicationProperties applicationProperties;
  private final MyPluginComponent myPluginComponent;

  public MyComponentWiredTest(ApplicationProperties applicationProperties,
                              MyPluginComponent myPluginComponent) {
    this.applicationProperties = applicationProperties;
    this.myPluginComponent = myPluginComponent;
  }

  @Test
  public void testMyName() {
    Assert.assertEquals("names do not match!",
                        "myComponent:" + applicationProperties.getDisplayName(),
                        myPluginComponent.getName());
  }

}
