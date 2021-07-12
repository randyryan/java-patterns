/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt;

import org.echeveria.snippets.swt.example.TableItemToolTipExample.ItemInfo;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;


/**
 * TableItemToolTip
 *
 * @author ryan131
 * @since Apr 24, 2019, 7:33:49 PM
 */
public class TableItemToolTip extends ItemToolTip<Table, TableItem> {

  public TableItemToolTip(Table table) {
    super(table);
  }

  @Override
  public TableItem[] getControlItems() {
    return getControl().getItems();
  }

  @Override
  protected TableItemAdapter createItemAdapter() {
    return new TableItemAdapter();
  }

  @Override
  protected TableItemEnabler createItemEnabler() {
    return new TableItemEnabler(getControl());
  }

  @Override
  protected Composite createToolTipContentArea(Event event, Composite parent) {
    Composite content = createContentComposite(event, parent);

    TableItem item = itemEnabler.getCurrentItem();
    ItemInfo itemInfo = (ItemInfo) item.getData();
    addLine(event, content, null, itemInfo.key, true);
    addLine(event, content, null, "Number of majors: " + itemInfo.majors, false);
    addLine(event, content, null, String.join(" - ", rotate(itemInfo.notes)), false);

    return content;
  }

  private <T> T[] rotate(T[] array) {
    T[] toReturn = array.clone();
    for (int i = 0; i < array.length; i++) {
      toReturn[(i + 7) % array.length] = array[i];
    }
    return toReturn;
  }

  public class TableItemAdapter extends ItemAdapter {

    @Override
    protected Point getToolTipLocation(Event event) {
      Rectangle bounds = getEventItem(event).getBounds();
      return new Point(bounds.x - 3, bounds.y + 21);
    }

  }

  public class TableItemEnabler extends ItemEnabler {

    protected TableItemEnabler(Table table) {
      super(table);
    }

    @Override
    protected TableItem getEventItem(Event event) {
      return getControl().getItem(new Point(event.x, event.y));
    }

  }

}
