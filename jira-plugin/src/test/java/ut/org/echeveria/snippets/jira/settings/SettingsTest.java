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

package ut.org.echeveria.snippets.jira.settings;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.echeveria.snippets.jira.api.MyPluginComponent;
import org.echeveria.snippets.jira.settings.SettingsManager;
import org.echeveria.snippets.jira.settings.SettingsSample;
import org.echeveria.snippets.jira.settings.SettingsSample.Echeveria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class SettingsTest {

  private Map<String, List<String>> settingsMap;

  @Mock
  private PluginSettings pluginSettings;

  @Mock
  private PluginSettingsFactory pluginSettingsFactory;

  private SettingsManager settingsManager;

  private Echeveria settings;

  @SuppressWarnings("unchecked")
  private void setUpMocks() {
    settingsMap = Maps.newHashMap();

    ArgumentCaptor<String> settingsKeyArg = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> settingsArg = ArgumentCaptor.forClass(List.class);

    when(pluginSettingsFactory.createGlobalSettings()).thenReturn(pluginSettings);
    when(pluginSettings.get(settingsKeyArg.capture())).thenAnswer(
        (Answer<List<String>>) invocation -> settingsMap.get(settingsKeyArg.getValue()));
    when(pluginSettings.put(settingsKeyArg.capture(), settingsArg.capture())).thenAnswer(
        (Answer<List<String>>) invocation -> settingsMap.put(settingsKeyArg.getValue(), settingsArg.getValue()));
  }

  @Before
  public void setUp() {
    settings = SettingsSample.echeveria_colorata();
  }

  @Test
  public void testSettingsKey() {
    String settingsKey = "echeveria";

    assertThat(settings.getSettingsKey(), notNullValue());
    assertThat(settings.getSettingsKey(), is(settingsKey));
  }

  @Test
  public void testSettingsId() {
    assertThat(settings.getSettingsId(), is(-1));
  }

  @Test
  public void testSettingsIsNew() {
    assertThat(settings.isNew(), is(true));
  }

  @Test
  public void testSettingsIsNewInSettingsManager() {
    setUpMocks();
    settingsManager = SettingsManager.getOrCreate(pluginSettingsFactory, MyPluginComponent.PLUGIN_KEY);

    assertThat(settings.isNewIn(settingsManager), is(true));

    settingsManager.saveSettings(settings);

    assertThat(settings.isNewIn(settingsManager), is(false));
  }

}
