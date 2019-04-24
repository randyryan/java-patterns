/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * ItemToolTip
 *
 * @author ryan131
 */
public abstract class ItemToolTip<C extends Control, I extends Item> extends DefaultToolTip {

  protected final static int MAX_WIDTH = 500;

  protected C control;
  protected final ItemAdapter itemAdapter;
  protected final ItemEnabler itemEnabler;

  protected ItemToolTip(C control) {
    super(control, ToolTip.RECREATE, true);
    this.control = control;
    this.itemAdapter = getItemAdapter();
    this.itemEnabler = getItemEnabler();
    for (I item : this.itemEnabler.getControlItems()) {
      registerItem(item);
    }
  }

  public C getControl() {
    return control;
  }

  protected abstract ItemAdapter getItemAdapter();

  protected abstract ItemEnabler getItemEnabler();

  @Override
  protected abstract Composite createToolTipContentArea(Event event, Composite parent);

  protected Composite createContentComposite(Event event, Composite parent) {
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    gridLayout.marginWidth = 5;
    gridLayout.marginHeight = 2;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(gridLayout);
    composite.setBackground(getBackgroundColor(event));
    return composite;
  }

  protected void addLine(Event event, Composite parent, Image image, String text, boolean bold) {
    Label imageLabel = new Label(parent, SWT.NONE);
    imageLabel.setForeground(getForegroundColor(event));
    imageLabel.setBackground(getBackgroundColor(event));
    imageLabel.setLayoutData(
        new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
    imageLabel.setImage(image);

    Label textLabel = new Label(parent, SWT.WRAP);
    textLabel.setForeground(getForegroundColor(event));
    textLabel.setBackground(getBackgroundColor(event));
    textLabel.setLayoutData(
        new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
    if (bold) {
      Font font = textLabel.getFont();
      font = FontDescriptor.createFrom(font).setStyle(SWT.BOLD).createFont(textLabel.getDisplay());
      textLabel.setFont(font);
    }
    textLabel.setText(text);
    GridDataFactory.fillDefaults()
        .align(SWT.FILL, SWT.BEGINNING)
        .hint(Math.min(textLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, MAX_WIDTH), SWT.DEFAULT)
        .applyTo(textLabel);
  }

  /**
   * Registers the item to respond to item tooltip.
   */
  public void registerItem(I item) {
    item.addListener(SWT.MouseEnter, itemAdapter);
    item.addListener(SWT.MouseExit, itemAdapter);
  }

  /**
   * Item adapter for SWT.MouseEnter and SWT.MouseExit events.
   *
   * @author ryan131
   */
  protected abstract class ItemAdapter implements Listener {

    @Override
    public void handleEvent(Event event) {
      if (event.type == SWT.MouseEnter) {
        mouseEnter(event);
      }
      if (event.type == SWT.MouseExit) {
        mouseExit(event);
      }
    }

    @SuppressWarnings("unchecked")
	protected I getItem(Event event) {
      return (I) event.widget;
    }

    protected abstract Point getToolTipLocation(Event event);

    private void mouseEnter(Event event) {
      ItemToolTip.this.show(getToolTipLocation(event));
    }

    private void mouseExit(Event event) {
      if (getItem(event) == itemEnabler.getPreviousItem()) {
        // No item is current
        ItemToolTip.this.hide();
      }
    }

  }

  /**
   * Enables certain events of Item objects, such as SWT.MouseEnter, SWT.MouseExit.
   * Note: This is a listener to be added to the parent of the items to be enabled.
   *
   * @author ryan131
   */
  public abstract class ItemEnabler implements Listener {

    protected C control;
    protected I previousItem;
    protected I currentItem;

    protected ItemEnabler(C control) {
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

    public abstract I[] getControlItems();

    public I getPreviousItem() {
      return previousItem; 
    }

    public I getCurrentItem() {
      return currentItem;
    }

  }

}
