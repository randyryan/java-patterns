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

package org.echeveria.snippets.jira.listener;

import javax.inject.Inject;

import org.echeveria.snippets.jira.api.MyPluginComponent;
import org.echeveria.snippets.jira.settings.SettingsManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.event.events.PluginEnabledEvent;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.lifecycle.LifecycleAware;

@Component
public class Initializer implements LifecycleAware, InitializingBean, DisposableBean {

  private EventPublisher eventPublisher;

  private SettingsManager settingsManager;

  @Inject
  public Initializer(@ComponentImport EventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
    this.settingsManager = SettingsManager.getOrCreate(MyPluginComponent.PLUGIN_KEY);
  }

  @Override
  public void onStart() {
    eventPublisher.register(this);
  }

  @Override
  public void onStop() {
    eventPublisher.unregister(this);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    eventPublisher.register(this);
  }

  @Override
  public void destroy() throws Exception {
    eventPublisher.unregister(this);
  }

  @EventListener
  public void onPluginEnabled(PluginEnabledEvent pluginEnabledEvent) {
    String pluginKey = pluginEnabledEvent.getPlugin().getKey();
    if (pluginKey.equals(MyPluginComponent.PLUGIN_KEY)) {
      // Do our plug-in initialization here.
    }
  }

}
