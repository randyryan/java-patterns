/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.jira.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

import webwork.action.Action;

/**
 * Hello Action
 *
 * @author ryan131
 */
@SuppressWarnings("serial")
public class HelloAction extends JiraWebActionSupport {

  private String name;
  private String message;

  public void setName(String name) {
    this.name = name;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String doExecute() {
    if (name == null || name.equals("") || name.trim().equals("")) {
      return Action.ERROR;
    }

    message = "Hello, " + name + "!";

    return Action.SUCCESS;
  }

  @Override
  public String doDefault() {
    return "input";
  }

}
