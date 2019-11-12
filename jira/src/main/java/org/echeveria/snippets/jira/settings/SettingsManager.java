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

  private SequencedSettingsAdapter getSettingsAdapter(String settingsKey) {
    return new SequencedSettingsAdapter(settingsKey);
  }

  // Settings

  public boolean hasSettings(String settingsKey) {
    return manifest.has(settingsKey);
  }

  /**
   * Add or update a setting
   *
   * @param settings to be saved. (or updated)
   * @return numbers of settings. (saved, with the same settings key)
   */
  public int saveSettings(Settings.Sequenced settings) {
    SequencedSettingsAdapter adapter = getSettingsAdapter(settings.getSettingsKey());
    boolean isNewSettings = settings.getSettingsId() == -1;

    if (isNewSettings) {
      int settingsId = adapter.getCount(); // Starting with 0, the second would be 1.
      settings.setSettingsId(settingsId);
    }

    String settingsJson = gson.toJson(settings);
    if (isNewSettings) {
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

  public int removeSettings(Settings.Sequenced settings) {
    SequencedSettingsAdapter adapter = getSettingsAdapter(settings.getSettingsKey());
    boolean isNewSettings = settings.getSettingsId() == -1;

    if (isNewSettings) {
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

  public void removeAllSettings(String settingsKey) {
    getSettingsAdapter(settingsKey).removeAll();
    manifest.remove(settingsKey);
  }

  @Nullable
  public <T> T getSettings(String settingsKey, Class<T> classOfT) {
    return getSettings(settingsKey, classOfT, 0);
  }

  @Nullable
  public <T> T getSettings(String settingsKey, Class<T> classOfT, int index) {
    String settingsJson = getSettingsAdapter(settingsKey).getSettings(index);
    return gson.fromJson(settingsJson, classOfT);
  }

  public <T> List<T> getAllSettings(String settingsKey, Class<T> classOfT) {
    return getSettingsAdapter(settingsKey).getOrCreate().stream()
        .map(settingsJson -> gson.fromJson(settingsJson, classOfT))
        .collect(Collectors.toList());
  }

  public List<String> getManifest() {
    return manifest.getList();
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
    private final SequencedSettingsAdapter adapter = getSettingsAdapter(settingsKey);

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
