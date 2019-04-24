/**
 * Copyright (c) 2015-2016 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt.example;

import org.echeveria.snippets.swt.TreeItemToolTip;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * TreeItem tooltip example
 *
 * @author ryan131
 * @since Apr 22, 2019, 4:39:31 PM
 */
public class TreeItemToolTipExample {

  public static void main(String[] args) {
    new TreeItemToolTipExample();
  }

  private Image itemIcon;

  private Tree tree;
  private TreeItemToolTip itemTooltip;

  public TreeItemToolTipExample() {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    shell.setSize(250, 400);

    itemIcon = ImageDescriptor.createFromFile(null, "src/main/resources/icons/file-new-16x16.png").createImage();

    // Create the tree and its items

    tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER);

    TreeItem item_1 = new TreeItem(tree, SWT.NONE);
    item_1.setText("Item 1");
    item_1.setImage(itemIcon);
    TreeItem item_1_1 = new TreeItem(item_1, SWT.NONE);
    item_1_1.setText("Item 1.1");
    item_1_1.setImage(itemIcon);
    TreeItem item_1_2 = new TreeItem(item_1, SWT.NONE);
    item_1_2.setText("Item 1.2");
    item_1_2.setImage(itemIcon);
    TreeItem item_2 = new TreeItem(tree, SWT.NONE);
    item_2.setText("Item 2");
    item_2.setImage(itemIcon);
    TreeItem item_3 = new TreeItem(tree, SWT.NONE);
    item_3.setText("Item 3");
    item_3.setImage(itemIcon);
    TreeItem item_3_1 = new TreeItem(item_3, SWT.NONE);
    item_3_1.setText("Item 3.1");
    item_3_1.setImage(itemIcon);
    TreeItem item_3_1_1 = new TreeItem(item_3_1, SWT.NONE);
    item_3_1_1.setText("Item 3.1.1");
    item_3_1_1.setImage(itemIcon);

    // Add the tooltip

    itemTooltip = new TreeItemToolTip(tree);
    itemTooltip.setPopupDelay(250);

    // Prepare the UI

    shell.open();

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    shell.dispose();
    display.dispose();
  }

}
