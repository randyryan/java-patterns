/*
 * The MIT License
 *
 * Copyright (c) 2019 Li Wan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.echeveria.snippets.jira.settings;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

public class SettingsManager {

  public static SettingsManager getOrCreate(String pluginKey) {
    PluginSettings pluginSettings = ComponentAccessor
            .getOSGiComponentInstanceOfType(PluginSettingsFactory.class)
            .createGlobalSettings();
    return new SettingsManager(pluginSettings, pluginKey);
  }

  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(SettingsManager.class);
  @SuppressWarnings("unused")
  private static final Gson gson = new Gson();

  private final PluginSettings pluginSettings;
  private final String pluginKey;
  private final SettingsManifest manifest;

  public SettingsManager(PluginSettings pluginSettings, String pluginKey) {
    this.pluginSettings = pluginSettings;
    this.pluginKey = pluginKey;
    this.manifest = new SettingsManifest();
  }

  public String getPluginKey() {
    return pluginKey;
  }

  public String getSettingsStorageKey(String settingsKey) {
    return pluginKey + ":" + settingsKey;
  }

  // Settings

  public boolean hasSettings(String settingsKey) {
    return manifest.has(settingsKey);
  }

  /**
   * @param settings to be saved.
   * @return numbers of settings. (saved, with the same settings key)
   */
  public int saveSettings(Settings settings) {
    boolean isNewSettings = settings.getSettingsId() == -1;
    String json = gson.toJson(settings);

    SettingsAdapter settingsAdapter = new SettingsAdapter(settings);
    if (isNewSettings) {
      settingsAdapter.add(json);
      manifest.add(settings.getSettingsKey());
    } else {
      settingsAdapter.update(settings.getSettingsId(), json);
    }

    return settingsAdapter.count();
  }

  public Settings getSettings(String settingsKey) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public <T> T getSettings(String settingsKey, Class<T> classOfT) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  public List<Settings> getAllSettings(String settingsKey) {
    throw new UnsupportedOperationException("To be implemented.");
  }

  private class SettingsAdapter {

    private final String settingsKey;

    private SettingsAdapter(String settingsKey) {
      this.settingsKey = getSettingsStorageKey(settingsKey);
    }

    private SettingsAdapter(Settings settings) {
      this.settingsKey = getSettingsStorageKey(settings.getSettingsKey());
    }

    private List<String> getOrCreate() {
      List<String> settings = (List<String>) pluginSettings.get(settingsKey);
      if (settings == null) {
        settings = Lists.newArrayList();
        pluginSettings.put(settingsKey, settings);
      }
      return settings;
    }

    public boolean isExist() {
      return pluginSettings.get(settingsKey) != null;
    }

    /**
     * The method assumes the settings is exist.
     */
    public boolean isEmpty() {
      return getOrCreate().isEmpty();
    }

    public int count() {
      return getOrCreate().size();
    }

    public boolean has(String setting) {
      return getOrCreate().contains(setting);
    }

    public void add(String setting) {
      List<String> settings = getOrCreate();
      settings.add(setting);
      pluginSettings.put(settingsKey, settings);
    }

    public void update(int index, String setting) {
      List<String> settings = getOrCreate();
      settings.set(index, setting);
      pluginSettings.put(settingsKey, setting);
    }

    public void remove(String setting) {
      List<String> settings = getOrCreate();
      settings.remove(setting);
      pluginSettings.put(settingsKey, settings);
    }

  }

  private class SettingsManifest {

    private Settings manifest = Settings.create("manifest");
    private SettingsAdapter adapter = new SettingsAdapter(manifest);

    public boolean has(String settingsKey) {
      return adapter.has(settingsKey);
    }

    public void add(String settingsKey) {
      adapter.add(settingsKey);
    }

    public void remove(String settingsKey) {
      adapter.remove(settingsKey);
    }

  }

}
