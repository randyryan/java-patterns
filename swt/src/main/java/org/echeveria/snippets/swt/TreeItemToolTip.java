/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


/**
 * TreeItemToolTip
 *
 * @author ryan131
 */
public class TreeItemToolTip extends ItemToolTip<Tree, TreeItem> {

  public TreeItemToolTip(Tree tree) {
    super(tree);
  }

  @Override
  public TreeItem[] getControlItems() {
    return getControl().getItems();
  }

  @Override
  public void registerItem(TreeItem item) {
    registerItem(item, true);
  }

  public void registerItem(TreeItem item, boolean recursive) {
    if (recursive) {
      TreeItem[] childItems = item.getItems();
      for (TreeItem childItem : childItems) {
        registerItem(childItem);
      }
    }
    super.registerItem(item);
  }

  @Override
  protected TreeItemAdapter createItemAdapter() {
    return new TreeItemAdapter();
  }

  @Override
  protected TreeItemEnabler createItemEnabler() {
    return new TreeItemEnabler(getControl());
  }

  @Override
  protected Composite createToolTipContentArea(Event event, Composite parent) {
    Composite content = createContentComposite(event, parent);

    Image itemIcon = itemEnabler.getCurrentItem().getImage();
    String itemName = itemEnabler.getCurrentItem().getText();
    String itemDesc = "This is a description for the item " + itemName;

    addLine(event, content, itemIcon, itemName, true);
    addLine(event, content, null, itemDesc, false);

    return content;
  }

  public class TreeItemAdapter extends ItemAdapter {

    @Override
    protected Point getToolTipLocation(Event event) {
      Rectangle bounds = getEventItem(event).getBounds();
      return new Point(bounds.x - 28, bounds.y + 19);
    }

  }

  public class TreeItemEnabler extends ItemEnabler {

    public TreeItemEnabler(Tree tree) {
      super(tree);
    }

    @Override
    protected TreeItem getEventItem(Event event) {
      return getControl().getItem(new Point(event.x, event.y));
    }

  }

}
