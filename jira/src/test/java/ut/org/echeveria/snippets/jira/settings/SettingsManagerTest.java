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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.echeveria.snippets.jira.api.MyPluginComponent;
import org.echeveria.snippets.jira.settings.Settings;
import org.echeveria.snippets.jira.settings.SettingsManager;
import org.junit.After;
import org.junit.Assert;
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

    Echeveria echeveria_colorata = new Echeveria(-1);
    echeveria_colorata.species = "colorata";
    echeveria_colorata.author = " E.Walther";

    Echeveria echeveria_derenbergii = new Echeveria(-1);
    echeveria_derenbergii.species = "derenbergii";
    echeveria_derenbergii.author = "J.A.Purpus";

    Echeveria echeveria_elegans = new Echeveria(-1);
    echeveria_elegans.species = "elegans";
    echeveria_elegans.author = "Rose";

    Echeveria echeveria_hyalina = new Echeveria(-1);
    echeveria_hyalina.species = "hyalina";
    echeveria_hyalina.author = "E.Walther";

    Graptopetalum graptopetalum_amethystinum = new Graptopetalum(-1);
    graptopetalum_amethystinum.species = "amethystinum";
    graptopetalum_amethystinum.author = "(Rose) E.Walther";

    Graptopetalum graptopetalum_macdougallii = new Graptopetalum(-1);
    graptopetalum_macdougallii.species = "macdougallii";
    graptopetalum_macdougallii.author = "Alexander";

    Pachyphytum pachyphytum_oviferum = new Pachyphytum(-1);
    pachyphytum_oviferum.species = "oviferum";
    pachyphytum_oviferum.author = "Purpus";

    settingsManager.saveSettings(echeveria_colorata);
    settingsManager.saveSettings(echeveria_derenbergii);
    settingsManager.saveSettings(echeveria_elegans);
    settingsManager.saveSettings(echeveria_hyalina);
    settingsManager.saveSettings(graptopetalum_amethystinum);
    settingsManager.saveSettings(graptopetalum_macdougallii);
    settingsManager.saveSettings(pachyphytum_oviferum);
  }

  @Test
  public void testPluginKey() {
    assertThat(settingsManager.getPluginKey(), is(MyPluginComponent.PLUGIN_KEY));
  }

  @Test()
  public void testManifest() {
    Assert.assertTrue(settingsManager.getManifest().size() == 3);
    Assert.assertTrue(settingsManager.getManifest().contains("echeveria"));
    Assert.assertTrue(settingsManager.getManifest().contains("graptopetalum"));
    Assert.assertTrue(settingsManager.getManifest().contains("pachyphytum"));
  }

  @After
  public void tearDown() {
  }

  abstract class Crassulaceae extends Settings {

    protected String genus;
    protected String species;
    protected String author;

    Crassulaceae(String settingsKey, int settingsId) {
      super(settingsKey, settingsId);
    }

    @Override
    public String toString() {
      return genus + " " + species + " " + author;
    }

  }

  class Echeveria extends Crassulaceae {

    Echeveria(int settingsId) {
      super("echeveria", settingsId);
      super.genus = "Echeveria";
    }

  }

  class Graptopetalum extends Crassulaceae {

    Graptopetalum(int settingsId) {
      super("graptopetalum", settingsId);
      super.genus = "Graptopetalum";
    }

  }

  class Pachyphytum extends Crassulaceae {

    Pachyphytum(int settingsId) {
      super("pachyphytum", settingsId);
      super.genus = "Pachyphytum";
    }

  }

}
