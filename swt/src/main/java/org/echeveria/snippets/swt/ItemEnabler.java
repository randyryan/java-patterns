/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;

/**
 * Enables certain events of Item objects, such as SWT.MouseEnter, SWT.MouseExit.
 * Note: This is a listener to be added to the parent of the items to be enabled.
 *
 * @author ryan131
 */
public abstract class ItemEnabler<C extends Control, I extends Item> implements Listener {

  protected C control;
  protected I previousItem;
  protected I currentItem;

  public ItemEnabler(C control) {
    this.control = control;
    this.control.addListener(SWT.MouseMove, this);
  }

  @Override
  public void handleEvent(Event event) {
    if (event.type == SWT.MouseMove) {
      mouseMove(event);
    }
  }

  protected abstract I getEventItem(Event event);

  protected void mouseMove(Event event) {
    currentItem = getEventItem(event);
    if (previousItem != currentItem) {
      if (previousItem != null) {
        // When the current item is not the previous item, meaning that the mouse has
        // exited the previous item, so the event widget should be the previous item.
        event.widget = previousItem;
        currentItem = null;
        previousItem.notifyListeners(SWT.MouseExit, event);
      }
      previousItem = currentItem;
      if (previousItem != null) {
        // When the current item is the previous item, meaning that the mouse has
        // entered the current item, so the event widget should be the current item.
        event.widget = currentItem;
        previousItem.notifyListeners(SWT.MouseEnter, event);
      }
    }
  }

  public C getControl() {
    return control;
  }

  public abstract I[] getControlItems();

  public I getPreviousItem() {
    return previousItem; 
  }

  public I getCurrentItem() {
    return currentItem;
  }

}
