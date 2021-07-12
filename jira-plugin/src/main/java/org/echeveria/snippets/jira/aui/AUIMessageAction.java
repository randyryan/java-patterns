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

package org.echeveria.snippets.jira.aui;

import java.util.Collection;

import org.echeveria.snippets.jira.ResourceKeys;

import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.web.action.JiraWebActionSupport;

public class AUIMessageAction extends JiraWebActionSupport {

  protected AUIMessage message = messageBuilder()
      .setParent(this)
      .setType(AUIMessage.Type.ERROR)
      .setTitle(ResourceKeys.AUI_MESSAGE_SINGLE_ERROR_TITLE)
      .build();

  protected AUIMessage.Builder messageBuilder() {
    return AUIMessage.builder();
  }

  public I18nHelper getI18nHelper() {
    return super.getI18nHelper();
  }

  public void addMessageKeyIfAbsent(String messageKey) {
    super.addErrorMessageByKeyIfAbsent(messageKey);
  }

  public void addMessageIfAbsent(String message) {
    super.addErrorMessageIfAbsent(message);
  }

  public Collection getMessages() {
    return super.getErrorMessages();
  }

  public boolean hasMessages() {
    return super.getHasErrorMessages();
  }

  /**
   * Provide $auiMessage access in the velocity templates for the AUIMessage object of the action.
   */
  public AUIMessage getAuiMessage() {
    return message;
  }

}
