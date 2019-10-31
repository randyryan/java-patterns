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
import org.echeveria.snippets.jira.settings.Settings;
import org.echeveria.snippets.jira.settings.SettingsManager;
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
