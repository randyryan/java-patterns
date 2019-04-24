/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ItemEnabler for Tree and TreeItem.
 *
 * @author ryan131
 */
public class TreeItemEnabler extends ItemEnabler<Tree, TreeItem> {

  public TreeItemEnabler(Tree tree) {
    super(tree);
  }

  @Override
  protected TreeItem getEventItem(Event event) {
    return control.getItem(new Point(event.x, event.y));
  }

  @Override
  public TreeItem[] getControlItems() {
    return control.getItems();
  }

}
