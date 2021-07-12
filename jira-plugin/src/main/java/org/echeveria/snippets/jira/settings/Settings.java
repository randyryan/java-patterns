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

/**
 * The base class for all settings types, your settings POJO should be sub-classing this class.
 */
public abstract class Settings<T> {

  private final String settingsKey;
  private T settingsId;

  protected Settings(String settingsKey, T settingsId) {
    this.settingsKey = settingsKey;
    this.settingsId = settingsId;
  }

  /**
   * Returns the settings key of this settings type.
   * @return the settings key of this settings type
   */
  public String getSettingsKey() {
    return settingsKey;
  }

  /**
   * Returns the settings ID of this individual settings.
   * @return the settings ID of this individual settings
   */
  public T getSettingsId() {
    return settingsId;
  }

  /**
   * Returns true if the settings has been newly created.
   * @return true if the settings has been newly created
   */
  public abstract boolean isNew();

  /**
   * Returns true if the settings has been newly created and yet to be saved in the specified
   * settings manager.
   *
   * @param settingsManager to check if this settings is exist in
   * @return true if the settings is yet to be saved in the specified settings manager
   */
  public abstract boolean isNewIn(SettingsManager settingsManager);

  void setSettingsId(T settingsId) {
    this.settingsId = settingsId;
  }

  public static class Sequenced extends Settings<Integer> {

    public static Sequenced create(String settingsKey) {
      return new Sequenced(settingsKey, -1);
    }

    public static Sequenced create(String settingsKey, int settingsId) {
      return new Sequenced(settingsKey, settingsId);
    }

    protected Sequenced(String settingsKey, int settingsId) {
      super(settingsKey, settingsId);
    }

    @Override
    public boolean isNew() {
      return getSettingsId() == -1;
    }

    @Override
    public boolean isNewIn(SettingsManager settingsManager) {
      if (!settingsManager.hasSettings(getSettingsKey())) {
        return true;
      }

      List<?> settingsList = settingsManager.getAllSettings(getSettingsKey(), getClass());
      if (settingsList == null) {
        return true;
      }

      Object settings = null;
      try {
        // The settings ID (in this implementation, the index in the list of the settings of the
        // same type) may have been assigned at this point, for the settings to be save by using
        // the set(index, e) of java.util.List.
        // But to notice that having a "legitimate" index doesn't mean it's saved in the list, so
        // we should query the list with the index to determine whether it's really new (in list).
        settings = settingsList.get(getSettingsId());
      } catch (IndexOutOfBoundsException e) {
        return true;
      }

      return settings == null;
    }

  }

}
