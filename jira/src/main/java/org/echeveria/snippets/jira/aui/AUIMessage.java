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

/**
 * A class for encapsulating AUI Message related information. For more details, refer to
 * https://docs.atlassian.com/aui/7.9.9/docs/messages.html.
 */
public class AUIMessage {

  static Builder builder() {
    return new Builder();
  }

  private AUIMessageAction parent;

  private Type type;
  private String titleKey;
  private String title;

  public Type getType() {
    return type;
  }

  public String getTitleKey() {
    return titleKey;
  }

  public String getTitle() {
    if (title != null) {
      title = parent.getI18nHelper().getText(titleKey);
    }
    return title;
  }

  public void addMessageKey(String messageKey) {
    parent.addMessageKeyIfAbsent(messageKey);
    if (getMessages().size() > 1) {
      this.titleKey = ResourceKeys.AUI_MESSAGE_MULTIPLE_ERRORS_TITLE;
    }
  }

  public void addMessage(String message) {
    parent.addMessageIfAbsent(message);
    if (getMessages().size() > 1) {
      this.titleKey = ResourceKeys.AUI_MESSAGE_MULTIPLE_ERRORS_TITLE;
    }
  }

  public Collection getMessages() {
    return parent.getMessages();
  }

  public boolean hasMessages() {
    return parent.hasMessages();
  }

  public enum Type {

    ERROR("aui-message-error"),
    WARNING("aui-message-warning"),
    SUCCESS("aui-message-success"),
    INFO("aui-message-info");

    private String htmlClass;

    private Type(String htmlClass) {
      this.htmlClass = htmlClass;
    }

    public String getHtmlClass() {
      return htmlClass;
    }

  }

  public static class Builder {

    private AUIMessage instance;

    Builder() {
      this.instance = new AUIMessage();
    }

    public Builder setParent(AUIMessageAction parent) {
      instance.parent = parent;
      return this;
    }

    public Builder setType(Type type) {
      instance.type = type;
      return this;
    }

    public Builder setTitle(String title) {
      instance.title = title;
      return this;
    }

    public Builder setTitleKey(String titleKey) {
      instance.titleKey = titleKey;
      return this;
    }

    public Builder addMessage(String message) {
      instance.addMessage(message);
      return this;
    }

    public Builder addMessageKey(String messageKey) {
      instance.addMessageKey(messageKey);
      return this;
    }

    public AUIMessage build() {
      if (instance.parent == null) {
        throw new IllegalStateException();
      }
      if (instance.type == null) {
        instance.type = Type.INFO;
      }
      if (instance.title == null) {
        if (instance.titleKey == null) {
          throw new IllegalStateException();
        } else {
          instance.title = instance.parent.getI18nHelper().getText(instance.titleKey);
        }
      }
      return instance;
    }

  }

}
