package ut.org.echeveria.snippets.jira;

import org.junit.Test;
import org.echeveria.snippets.jira.api.MyPluginComponent;
import org.echeveria.snippets.jira.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}