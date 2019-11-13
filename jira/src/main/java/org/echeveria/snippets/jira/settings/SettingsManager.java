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
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

@SuppressWarnings({"unused", "unchecked"})
public class SettingsManager {

  public static SettingsManager getOrCreate(String pluginKey) {
    PluginSettingsFactory pluginSettingsFactory =
            ComponentAccessor.getOSGiComponentInstanceOfType(PluginSettingsFactory.class);
    PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
    return new SettingsManager(pluginSettings, pluginKey);
  }

  public static SettingsManager getOrCreate(PluginSettingsFactory pluginSettingsFactory, String pluginKey) {
    PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
    return new SettingsManager(pluginSettings, pluginKey);
  }

  public static SettingsManager getOrCreate(PluginSettings pluginSettings, String pluginKey) {
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

  private SequencedSettingsAdapter getSequencedSettingsAdapter(String settingsKey) {
    return new SequencedSettingsAdapter(settingsKey);
  }

  // Settings

  /**
   * Returns a list of the settings that are currently associated with the current settings manager,
   * so that we are not agnostic about what are stored.
   * @return a list of settings keys
   */
  public List<String> getManifest() {
    return manifest.getList();
  }

  /**
   * Query the Settings Manager about if the specified settings (key) exists.
   *
   * @param settingsKey to query the existence of settings
   * @return true if there are settings of the specified settings key
   */
  public boolean hasSettings(String settingsKey) {
    return manifest.has(settingsKey);
  }

  /**
   * Add or update the specified settings.
   *
   * @param settings to be added or updated
   * @return number the settings of the specified kind that currently exist
   */
  public int saveSettings(Settings.Sequenced settings) {
    SequencedSettingsAdapter adapter = getSequencedSettingsAdapter(settings.getSettingsKey());

    if (settings.isNewIn(this)) {
      int settingsId = adapter.getCount(); // Starting with 0, the second would be 1.
      settings.setSettingsId(settingsId);
    }

    String settingsJson = gson.toJson(settings);
    if (settings.isNewIn(this)) {
      adapter.add(settingsJson);
      String settingsKey = settings.getSettingsKey();
      if (!manifest.getList().contains(settingsKey)) {
        manifest.add(settingsKey);
      }
    } else {
      adapter.update(settingsJson, settings.getSettingsId());
    }

    return adapter.getCount();
  }

  /**
   * Remove the specified the settings.
   *
   * @param settings to be removed.
   * @return number the settings of the specified kind that currently exist
   */
  public int removeSettings(Settings.Sequenced settings) {
    SequencedSettingsAdapter adapter = getSequencedSettingsAdapter(settings.getSettingsKey());

    if (settings.isNewIn(this)) {
      throw new IllegalArgumentException("Cannot remove settings that does not exist yet.");
    } else {
      String settingsJson = gson.toJson(settings);
      adapter.remove(settingsJson);
      if (adapter.getCount() == 0) {
        manifest.remove(settings.getSettingsKey());
      }
    }

    return adapter.getCount();
  }

  /**
   * Remove all settings of the specified settings key.
   *
   * @param settingsKey to remove all settings of
   */
  public void removeAllSettings(String settingsKey) {
    getSequencedSettingsAdapter(settingsKey).removeAll();
    manifest.remove(settingsKey);
  }

  /**
   * Retrieve the settings of the specified settings key and its class type, if there are multiple
   * settings found with the specified settings key, the first one is returned.
   *
   * @param settingsKey to retrieve the settings
   * @param classOfT to return the settings object generically
   * @param <T> the type of the settings
   * @return the settings object
   */
  @Nullable
  public <T> T getSettings(String settingsKey, Class<T> classOfT) {
    return getSettings(settingsKey, classOfT, 0);
  }

  /**
   * Retrieve the settings of the specified settings key and its class type, of the specified index.
   *
   * @param settingsKey to retrieve the settings
   * @param classOfT to return the settings object generically
   * @param index of the settings
   * @param <T> the type of the settings
   * @return the settings object
   */
  @Nullable
  public <T> T getSettings(String settingsKey, Class<T> classOfT, int index) {
    String settingsJson = getSequencedSettingsAdapter(settingsKey).getSettings(index);
    return gson.fromJson(settingsJson, classOfT);
  }

  /**
   * Retrieve all settings of the specified settings key as a list.
   *
   * @param settingsKey to retrive the settings
   * @param classOfT to return the settings objects generically
   * @param <T> the type of the settings
   * @return the settings object list
   */
  public <T> List<T> getAllSettings(String settingsKey, Class<T> classOfT) {
    return getSequencedSettingsAdapter(settingsKey).getOrCreate().stream()
        .map(settingsJson -> gson.fromJson(settingsJson, classOfT))
        .collect(Collectors.toList());
  }

  /**
   * Provide operations for the settings list of given key.
   */
  private class SequencedSettingsAdapter {

    private final String settingsKey;

    private SequencedSettingsAdapter(String settingsKey) {
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

    public String getSettings(int index) {
      return getOrCreate().get(index);
    }

  }

  /**
   * Provide a manifest of the settings so that we are not agnostic about what are existing.
   */
  private class SettingsManifest {

    private final String settingsKey = "manifest";
    private final SequencedSettingsAdapter adapter = getSequencedSettingsAdapter(settingsKey);

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

    public boolean has(String settingsKey) {
      return adapter.contains(settingsKey);
    }

    public List<String> getList() {
      return ImmutableList.copyOf(adapter.getOrCreate());
    }

  }

}
