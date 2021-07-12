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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.echeveria.snippets.jira.api.MyPluginComponent;
import org.echeveria.snippets.jira.settings.SettingsManager;
import org.echeveria.snippets.jira.settings.SettingsSample;
import org.echeveria.snippets.jira.settings.SettingsSample.Echeveria;
import org.junit.After;
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
public class SettingsManagerTest {

  private Map<String, List<String>> settingsMap;

  @Mock
  private PluginSettings pluginSettings;

  @Mock
  private PluginSettingsFactory pluginSettingsFactory;

  private SettingsManager settingsManager;

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
  public void setup() {
    setUpMocks();
    settingsManager = SettingsManager.getOrCreate(pluginSettingsFactory, MyPluginComponent.PLUGIN_KEY);
  }

  // Plug-in key tests

  @Test
  public void testPluginKey() {
    assertThat(settingsManager.getPluginKey(), is(MyPluginComponent.PLUGIN_KEY));
  }

  // Settings CRUD tests

  @Test
  public void testSaveSettings() {
    String settingsKey = "echeveria";

    settingsManager.saveSettings(SettingsSample.echeveria_colorata());

    Echeveria echeveria_colorata = settingsManager.getSettings(settingsKey, Echeveria.class);

    assertThat(settingsManager.hasSettings(settingsKey), is(true));
    assertThat(echeveria_colorata.getSettingsId(), is(0));
  }

  @Test
  public void testUpdateSettings() {
    String settingsKey = "echeveria";

    settingsManager.saveSettings(SettingsSample.echeveria_colorata());

    Echeveria echeveria_colorata = settingsManager.getSettings(settingsKey, Echeveria.class);

    assertThat(echeveria_colorata.hasSynonym(), is(false));

    echeveria_colorata.setSynonym("Echeveria lindsayana E.Walther");
    settingsManager.saveSettings(echeveria_colorata);
    echeveria_colorata = settingsManager.getSettings(settingsKey, Echeveria.class);

    assertThat(echeveria_colorata.hasSynonym(), is(true));
    assertThat(echeveria_colorata.getSynonym(), is("Echeveria lindsayana E.Walther"));
  }

  @Test
  public void testRemoveSettings() {
    String settingsKey = "echeveria";

    settingsManager.saveSettings(SettingsSample.echeveria_colorata());

    Echeveria echeveria_colorata = settingsManager.getSettings(settingsKey, Echeveria.class);

    settingsManager.removeSettings(echeveria_colorata);

    assertThat(settingsManager.hasSettings(settingsKey), is(false));
  }

  @Test
  public void testGetSettings() {
    String settingsKey = "echeveria";

    settingsManager.saveSettings(SettingsSample.echeveria_colorata());
    settingsManager.saveSettings(SettingsSample.echeveria_derenbergii());

    // Retrieve by settings key without index (to retrieve the first one)

    Echeveria echeveria_colorata = settingsManager.getSettings(settingsKey, Echeveria.class);

    assertThat(echeveria_colorata.getSettingsKey(), is("echeveria"));
    assertThat(echeveria_colorata.getSettingsId(), is(not(-1)));
    assertThat(echeveria_colorata.getSettingsId(), is(0));

    assertThat(echeveria_colorata.getGenus(), is("Echeveria"));
    assertThat(echeveria_colorata.getSpecies(), is("colorata"));
    assertThat(echeveria_colorata.getAuthor(), is("E.Walther"));

    // Retrieve by settings key with index

    Echeveria echeveria_derenbergii = settingsManager.getSettings(settingsKey, Echeveria.class, 1);

    assertThat(echeveria_derenbergii.getSettingsKey(), is("echeveria"));
    assertThat(echeveria_derenbergii.getSettingsId(), is(not(-1)));
    assertThat(echeveria_derenbergii.getSettingsId(), is(1));

    assertThat(echeveria_derenbergii.getGenus(), is("Echeveria"));
    assertThat(echeveria_derenbergii.getSpecies(), is("derenbergii"));
    assertThat(echeveria_derenbergii.getAuthor(), is("J.A.Purpus"));
  }

  @Test
  public void testGetAllSettingsShouldBeEmpty() {
    String settingsKey = "echeveria";

    List<Echeveria> echeverias = settingsManager.getAllSettings(settingsKey, Echeveria.class);

    assertThat(echeverias, empty());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetAllSettingsCannotBeModifiedDirectly() {
    String settingsKey = "echeveria";

    List<Echeveria> echeverias = settingsManager.getAllSettings(settingsKey, Echeveria.class);

    echeverias.add(SettingsSample.echeveria_derenbergii());
  }

  @Test
  public void testGetAllSettings() {
    String settingsKey = "echeveria";

    settingsManager.saveSettings(SettingsSample.echeveria_colorata());
    settingsManager.saveSettings(SettingsSample.echeveria_derenbergii());
    settingsManager.saveSettings(SettingsSample.echeveria_elegans());
    settingsManager.saveSettings(SettingsSample.echeveria_hyalina());
    settingsManager.saveSettings(SettingsSample.graptopetalum_amethystinum());
    settingsManager.saveSettings(SettingsSample.graptopetalum_macdougallii());
    settingsManager.saveSettings(SettingsSample.pachyphytum_oviferum());

    // Retrieve all by settings key

    List<Echeveria> echeverias = settingsManager.getAllSettings(settingsKey, Echeveria.class);

    assertThat(echeverias.size(), is(4));

    assertThat(echeverias.get(0).getSettingsId(), is(0));
    assertThat(echeverias.get(0).getSpecies(), is("colorata"));

    assertThat(echeverias.get(1).getSettingsId(), is(1));
    assertThat(echeverias.get(1).getSpecies(), is("derenbergii"));

    assertThat(echeverias.get(2).getSettingsId(), is(2));
    assertThat(echeverias.get(2).getSpecies(), is("elegans"));

    assertThat(echeverias.get(3).getSettingsId(), is(3));
    assertThat(echeverias.get(3).getSpecies(), is("hyalina"));
  }

  // Settings manifest tests

  @Test
  public void testManifestShouldBeEmpty() {
    assertThat(settingsManager.getManifest(), empty());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testManifestCannotBeModifiedDirectly() {
    settingsManager.getManifest().add("test");
  }

  @Test
  public void testManifestShouldBeOne() {
    settingsManager.saveSettings(SettingsSample.echeveria_colorata());

    assertThat(settingsManager.getManifest().size(), is(1));

    settingsManager.saveSettings(SettingsSample.echeveria_derenbergii());

    assertThat(settingsManager.getManifest().size(), is(1));
  }

  @Test
  public void testManifestShouldBeTwo() {
    settingsManager.saveSettings(SettingsSample.echeveria_colorata());
    settingsManager.saveSettings(SettingsSample.graptopetalum_amethystinum());

    assertThat(settingsManager.getManifest().size(), is(2));
  }

  @Test
  public void testManifestAfterSavingSettings() {
    settingsManager.saveSettings(SettingsSample.echeveria_colorata());
    settingsManager.saveSettings(SettingsSample.echeveria_derenbergii());
    settingsManager.saveSettings(SettingsSample.echeveria_elegans());
    settingsManager.saveSettings(SettingsSample.echeveria_hyalina());
    settingsManager.saveSettings(SettingsSample.graptopetalum_amethystinum());
    settingsManager.saveSettings(SettingsSample.graptopetalum_macdougallii());
    settingsManager.saveSettings(SettingsSample.pachyphytum_oviferum());

    List<String> manifest = settingsManager.getManifest();

    assertThat(manifest.size(), is(3));
    assertThat(manifest, hasItem("echeveria"));
    assertThat(manifest, hasItem("graptopetalum"));
    assertThat(manifest, hasItem("pachyphytum"));
  }

  @Test
  public void testManifestAfterRemovingSettings() {
    String settingsKey = "echeveria"; // The specific settings

    assertThat(settingsManager.hasSettings(settingsKey), is(false));

    settingsManager.saveSettings(SettingsSample.echeveria_colorata());

    assertThat(settingsManager.hasSettings(settingsKey), is(true));

    settingsManager.removeAllSettings(settingsKey);

    assertThat(settingsManager.hasSettings(settingsKey), is(false));
  }

  @After
  public void tearDown() {
  }

}
