package org.echeveria.snippets.jira.webwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class PluginSettingsViewerAction extends JiraWebActionSupport {

  public static final String VIEW = "view";

  private static final Logger log = LoggerFactory.getLogger(PluginSettingsViewerAction.class);

  @Override
  public String execute() throws Exception {
    return PluginSettingsViewerAction.VIEW;
  }

}
