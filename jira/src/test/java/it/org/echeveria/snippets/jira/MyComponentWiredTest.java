/*
 * The MIT License
 *
 * Copyright (c) 2019 Li Wan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package it.org.echeveria.snippets.jira;

import org.echeveria.snippets.jira.api.MyPluginComponent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
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
