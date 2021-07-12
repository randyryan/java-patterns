/**
 * Copyright (c) 2010-2019 Ryan Li Wan. All rights reserved.
 */

package org.echeveria.snippets.swt.example;

import org.echeveria.snippets.swt.TableItemToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * TableItem tooltip example
 *
 * @author ryan131
 */
public class TableItemToolTipExample {

  public static final String[][] MAJOR_SCALES = new String[][] {
      {"C", "C", "D", "E", "F", "G", "A", "B"},
      {"G", "G", "A", "B", "C", "D", "E", "F♯"},
      {"D", "D", "E", "F♯", "G", "A", "B", "C♯"},
      {"A", "A", "B", "C♯", "D", "E", "F♯", "G♯"},
      {"E", "E", "F♯", "G♯", "A", "B", "C♯", "D♯"},
      {"B", "B", "C♯", "D♯", "E", "F♯", "G♯", "A♯"},
      {"F♯", "F♯", "G♯", "A♯", "B", "C♯", "D♯", "E♯"},
      {"C♯", "C♯", "D♯", "E♯", "F♯", "G♯", "A♯", "B♯"},
      {"G♯", "G♯", "A♯", "B♯", "C♯", "D♯", "E♯", "F♯♯"},
      {"D♯", "D♯", "E♯", "F♯♯", "G♯", "A♯", "B♯", "C♯♯"},
      {"A♯", "A♯", "B♯", "C♯♯", "D♯", "E♯", "F♯♯", "G♯♯"},
      {"E♯", "E♯", "F♯♯", "G♯♯", "A♯", "B♯", "C♯♯", "D♯♯"},
      {"B♯", "B♯", "C♯♯", "D♯♯", "E♯", "F♯♯", "G♯♯", "A♯♯"}
  };

  public static final ItemInfo[] ITEMS = new ItemInfo[] {
    new ItemInfo("C Major", 0, MAJOR_SCALES[0]),
    new ItemInfo("G Major", 1, MAJOR_SCALES[1]),
    new ItemInfo("D Major", 2, MAJOR_SCALES[2]),
    new ItemInfo("A Major", 3, MAJOR_SCALES[3]),
    new ItemInfo("E Major", 4, MAJOR_SCALES[4]),
    new ItemInfo("B Major", 5, MAJOR_SCALES[5]),
    new ItemInfo("F♯ Major", 6, MAJOR_SCALES[6]),
    new ItemInfo("C♯ Major", 7, MAJOR_SCALES[7]),
    new ItemInfo("G♯ Major", 8, MAJOR_SCALES[8]),
    new ItemInfo("D♯ Major", 9, MAJOR_SCALES[9]),
    new ItemInfo("A♯ Major", 10, MAJOR_SCALES[10]),
    new ItemInfo("E♯ Major", 11, MAJOR_SCALES[11]),
    new ItemInfo("B♯ Major", 12, MAJOR_SCALES[12])
  };

  public static class ItemInfo {

    public ItemInfo(String key, int majors, String[] notes) {
      this.key = key;
      this.majors = majors;
      this.notes = notes;
    }

    public String key;

    public String[] notes;

    public int majors;

  }

  public static void main(String[] args) {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("TableItem tooltip example");
    shell.setLayout(new GridLayout(1, true));

    // Create the table and its items

    Table table = new Table(shell, SWT.BORDER);

    TableColumn column0 = new TableColumn(table, SWT.LEFT);
    column0.setText("Key");
    column0.setWidth(35);
    TableColumn column1 = new TableColumn(table, SWT.LEFT);
    column1.setText("I");
    column1.setWidth(35);
    TableColumn column2 = new TableColumn(table, SWT.LEFT);
    column2.setText("II");
    column2.setWidth(35);
    TableColumn column3 = new TableColumn(table, SWT.LEFT);
    column3.setText("III");
    column3.setWidth(35);
    TableColumn column4 = new TableColumn(table, SWT.LEFT);
    column4.setText("IV");
    column4.setWidth(35);
    TableColumn column5 = new TableColumn(table, SWT.LEFT);
    column5.setText("V");
    column5.setWidth(35);
    TableColumn column6 = new TableColumn(table, SWT.LEFT);
    column6.setText("VI");
    column6.setWidth(35);
    TableColumn column7 = new TableColumn(table, SWT.LEFT);
    column7.setText("VII");
    column7.setWidth(35);

    table.setHeaderVisible(true);

    for (int i = 0; i < Math.min(MAJOR_SCALES.length, ITEMS.length); i++) {
      TableItem tableItem = new TableItem(table, SWT.NONE);
      tableItem.setText(MAJOR_SCALES[i]);
      tableItem.setData(ITEMS[i]);
    }

    // Add the tooltip

    TableItemToolTip itemTooltip = new TableItemToolTip(table);
    itemTooltip.setPopupDelay(250);

    // Prepare the UI

    shell.pack();
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

