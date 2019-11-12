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

package it.org.echeveria.snippets.jira.settings;

import java.util.List;
import java.util.Map;

import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.echeveria.snippets.jira.api.MyPluginComponent;
import org.echeveria.snippets.jira.settings.SettingsManager;
import org.echeveria.snippets.jira.settings.SettingsSample;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atlassian.jira.mock.component.MockComponentWorker;

public class SettingsResourceFuncTest {

  private SettingsManager settingsManager;

  @BeforeClass
  public static void setUpClass() {
    new MockComponentWorker().init();
  }

  @Before
  public void setup() {
    settingsManager = SettingsManager.getOrCreate(MyPluginComponent.PLUGIN_KEY);

    settingsManager.saveSettings(SettingsSample.echeveria_colorata());
    settingsManager.saveSettings(SettingsSample.echeveria_derenbergii());
    settingsManager.saveSettings(SettingsSample.echeveria_elegans());
    settingsManager.saveSettings(SettingsSample.echeveria_hyalina());
    settingsManager.saveSettings(SettingsSample.graptopetalum_amethystinum());
    settingsManager.saveSettings(SettingsSample.graptopetalum_macdougallii());
    settingsManager.saveSettings(SettingsSample.pachyphytum_oviferum());
  }

  @After
  public void tearDown() {

  }

  @Test
  public void manifestIsValid() {
    String baseUrl = System.getProperty("baseurl");
    String resourceUrl = baseUrl + "/rest/settings/1.0/manifest";

    RestClient client = new RestClient();
    Resource resource = client.resource(resourceUrl);

    Map<String, List<String>> payload = resource.get(Map.class);

    Assert.assertTrue(payload.keySet().contains("manifest"));

    List<String> manifest = payload.get("manifest");

    Assert.assertEquals(3, manifest.size());
    Assert.assertTrue(manifest.contains("echeveria"));
    Assert.assertTrue(manifest.contains("graptopetalum"));
    Assert.assertTrue(manifest.contains("pachyphytum"));
  }

}
