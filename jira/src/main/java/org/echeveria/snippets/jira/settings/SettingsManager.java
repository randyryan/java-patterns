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

@SuppressWarnings({"unused", "unchecked"})
public class SettingsManager {

  public static SettingsManager getOrCreate(String pluginKey) {
    PluginSettings pluginSettings = ComponentAccessor
            .getOSGiComponentInstanceOfType(PluginSettingsFactory.class)
            .createGlobalSettings();
    return new SettingsManager(pluginSettings, pluginKey);
  }

  private static final Logger logger = LoggerFactory.getLogger(SettingsManager.class);

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

  // Internal methods

  private String getSettingsStorageKey(String settingsKey) {
    return pluginKey + ":" + settingsKey;
  }

  private SettingsAdapter getSettingsAdapter(String settingsKey) {
    return new SettingsAdapter(settingsKey);
  }

  // Settings

  public boolean hasSettings(String settingsKey) {
    return getSettingsAdapter(settingsKey).isExist();
  }

  /**
   * Add or update a setting
   *
   * @param settings to be saved. (or updated)
   * @return numbers of settings. (saved, with the same settings key)
   */
  public int saveSettings(Settings settings) {
    boolean isNewSettings = settings.getSettingsId() == -1;
    String settingsJson = gson.toJson(settings);

    SettingsAdapter adapter = getSettingsAdapter(settings.getSettingsKey());
    if (isNewSettings) {
      adapter.add(settingsJson);
      if (adapter.getCount() == 0) {
        manifest.add(settings.getSettingsKey());
      }
    } else {
      adapter.update(settingsJson, settings.getSettingsId());
    }

    return adapter.getCount();
  }

  public int removeSettings(Settings settings) {
    boolean isNewSettings = settings.getSettingsId() == -1;
    String settingsJson = gson.toJson(settings);

    SettingsAdapter adapter = getSettingsAdapter(settings.getSettingsKey());
    if (isNewSettings) {
      throw new IllegalArgumentException("The settings does not exist.");
    } else {
      adapter.remove(settingsJson);
      if (adapter.getCount() == 0) {
        manifest.remove(settings.getSettingsKey());
      }
    }

    return adapter.getCount();
  }

  public void removeAllSettings(String settingsKey) {
    getSettingsAdapter(settingsKey).removeAll();
    manifest.remove(settingsKey);
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

  /**
   * Provide operations for the settings list of given key.
   */
  private class SettingsAdapter {

    private final String settingsKey;

    private SettingsAdapter(String settingsKey) {
      this.settingsKey = getSettingsStorageKey(settingsKey);
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
      if (!isExist()) {
        return true;
      }
      return getOrCreate().isEmpty();
    }

    public int getCount() {
      return getOrCreate().size();
    }

    public boolean contains(String settingsJson) {
      return getOrCreate().contains(settingsJson);
    }

    public void add(String settingsJson) {
      List<String> settingsList = getOrCreate();
      settingsList.add(settingsJson);
      pluginSettings.put(settingsKey, settingsList);
    }

    public void update(String settingsJson, int index) {
      List<String> settingsList = getOrCreate();
      settingsList.set(index, settingsJson);
      pluginSettings.put(settingsKey, settingsList);
    }

    public void remove(String settingsJson) {
      List<String> settingsList = getOrCreate();
      settingsList.remove(settingsJson);
      pluginSettings.put(settingsKey, settingsList);
    }

    public void removeAll() {
      pluginSettings.remove(settingsKey);
    }

  }

  /**
   * Provide a manifest of the settings so that we are not agnostic about what are existing.
   */
  private class SettingsManifest {

    private final String settingsKey = "manifest";
    private final SettingsAdapter adapter = getSettingsAdapter(settingsKey);

    public void add(String settingsKey) {
      if (!adapter.contains(settingsKey)) {
        adapter.add(settingsKey);
      }
    }

    public void remove(String settingsKey) {
      if (adapter.contains(settingsKey)) {
        adapter.remove(settingsKey);
      }
    }

    public List<String> getList() {
      return adapter.getOrCreate();
    }

  }

}
