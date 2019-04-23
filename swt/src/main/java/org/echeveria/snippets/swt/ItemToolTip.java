/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ItemToolTip
 *
 * @author ryan131
 */
public abstract class ItemToolTip<C extends Control, I extends Item> extends ToolTip {

  protected final static int MAX_WIDTH = 500;

  protected final ItemAdapter itemAdapter;
  protected final ItemEnabler<C, I> itemEnabler;

  protected ItemToolTip(ItemEnabler<C, I> itemEnabler) {
    super(itemEnabler.getControl(), ToolTip.RECREATE, true);
    this.itemAdapter = new ItemAdapter();
    this.itemEnabler = itemEnabler;
    for (I item : this.itemEnabler.getControlItems()) {
      registerItem(item);
    }
  }

  @Override
  protected abstract Composite createToolTipContentArea(Event event, Composite parent);

  protected Composite createContentComposite(Composite parent) {
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    gridLayout.marginWidth = 5;
    gridLayout.marginHeight = 2;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(gridLayout);
    composite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
    return composite;
  }

  protected void addImageAndText(Composite parent, Image image, String text, boolean bold) {
    Label imageLabel = new Label(parent, SWT.NONE);
    imageLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
    imageLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
    imageLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
    imageLabel.setImage(image);

    Label textLabel = new Label(parent, SWT.WRAP);
    textLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
    textLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
    textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
    if (bold) {
      textLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
    }
    textLabel.setText(removeTrailingNewline(text));
    int width = Math.min(textLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, MAX_WIDTH);
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).hint(width, SWT.DEFAULT).applyTo(textLabel);
  }

  private String removeTrailingNewline(String text) {
    if (text.endsWith("\n")) {
      return text.substring(0, text.length() - 1);
    }
    return text;
  }

  /**
   * Registers the item to respond to item tooltip.
   */
  public void registerItem(I item) {
    item.addListener(SWT.MouseEnter, itemAdapter);
    item.addListener(SWT.MouseExit, itemAdapter);
  }

  // Item adapter for SWT.MouseEnter and SWT.MouseExit events.

  protected class ItemAdapter implements Listener {

    @Override
    public void handleEvent(Event event) {
      if (event.type == SWT.MouseEnter) {
        mouseEnter(event);
      }
      if (event.type == SWT.MouseExit) {
        mouseExit(event);
      }
    }

    private TreeItem getItem(Event event) {
      return (TreeItem) event.widget;
    }

    private void mouseEnter(Event event) {
      TreeItem item = getItem(event);
      Rectangle bounds = item.getBounds();
      ItemToolTip.this.show(new Point(bounds.x - 28, bounds.y + 19));
    }

    private void mouseExit(Event event) {
      if (getItem(event) == itemEnabler.getPreviousItem()) {
        // No item is current
        ItemToolTip.this.hide();
      }
    }

  }

}
