/*
 * Copyright 2014 - 2017 Cognizant Technology Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognizant.cognizantits.ide.main.utils.table;

import com.cognizant.cognizantits.datalib.undoredo.UndoRedoModel;
import com.cognizant.cognizantits.ide.main.utils.keys.ClipboardKeyAdapter;
import com.cognizant.cognizantits.ide.main.utils.keys.Keystroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;
import static javax.swing.JTable.AUTO_RESIZE_OFF;
import static javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class XTable extends JTable {

    private boolean inLayout;

    private SearchRenderer searchRenderer;

    private EditHeader editHeader;

    public XTable() {
        init();
    }

    public XTable(TableModel tm) {
        super(tm);
        init();
    }

    private void init() {
        searchRenderer = new SearchRenderer();
        setFillsViewportHeight(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        getTableHeader().setReorderingAllowed(false);
        setCellSelectionEnabled(true);
        setColumnSelectionAllowed(true);
        setGridColor(Color.GRAY);
        addKeyListeners();
        putClientProperty("terminateEditOnFocusLost", true);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                searchRenderer.focused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent fe) {
                searchRenderer.focused = false;
                repaint();
            }
        });
        TableCellDrag.install(this);
    }

    private void addKeyListeners() {
        addKeyListener(new ClipboardKeyAdapter(this));
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.DELETE, "Clear");
        getActionMap().put("Clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                deleteSelectedCells();
            }
        });

        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.INSERT_ROW, "Insert");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.ADD_ROW, "Add");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.ADD_ROWX, "Add");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.REMOVE_ROW, "Delete");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.REMOVE_ROWX, "Delete");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.ADD_COL, "Add Column");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.ADD_COLX, "Add Column");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.REMOVE_COL, "Delete Column");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.REMOVE_COLX, "Delete Column");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.COPY_ABOVE, "Copy Above");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.REPLICATE_ROW, "Replicate");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.SAVE, "Save");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.F5, "Reload");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.OPEN, "Open");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.FIND, "Search");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.COMMENT, "Comment");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.BREAKPOINT, "BreakPoint");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.UP, "MoveUp");
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.DOWN, "MoveDown");

        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.UNDO, "Undo");
        getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (getModel() instanceof UndoRedoModel) {
                    ((UndoRedoModel) getModel()).getUndoManager().undo();
                }
            }
        });
        getInputMap(JComponent.WHEN_FOCUSED).put(Keystroke.REDO, "Redo");
        getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (getModel() instanceof UndoRedoModel) {
                    ((UndoRedoModel) getModel()).getUndoManager().redo();
                }
            }
        });
    }

    private void deleteSelectedCells() {
        if (getModel() instanceof UndoRedoModel) {
            ((UndoRedoModel) getModel()).startGroupEdit();
        }
        int[] srow = getSelectedRows();
        int[] scol = getSelectedColumns();
        int lastSrow = srow.length;
        int lastScol = scol.length;
        for (int i = 0; i < lastSrow; i++) {
            for (int j = 0; j < lastScol; j++) {
                if (isCellEditable(srow[i], scol[j])) {
                    setValueAt("", srow[i], scol[j]);
                }
            }
        }
        if (getModel() instanceof UndoRedoModel) {
            ((UndoRedoModel) getModel()).stopGroupEdit();
        }
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return searchRenderer.setDefRenderer(super.getCellRenderer(row, column));
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return hasExcessWidth();

    }

    @Override
    public int getRowHeight() {
        return 25;
    }

    @Override
    public void doLayout() {
        if (hasExcessWidth()) {
            // fool super
            autoResizeMode = AUTO_RESIZE_SUBSEQUENT_COLUMNS;
        }
        inLayout = true;
        super.doLayout();
        inLayout = false;
        autoResizeMode = AUTO_RESIZE_OFF;
    }

    protected boolean hasExcessWidth() {
        return getPreferredSize().width < getParent().getWidth();
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
        if (isEditing()) {
            // JW: darn - cleanup to terminate editing ...
            removeEditor();
        }
        TableColumn resizingColumn = getTableHeader().getResizingColumn();
        // Need to do this here, before the parent's
        // layout manager calls getPreferredSize().
        if (resizingColumn != null && autoResizeMode == AUTO_RESIZE_OFF
                && !inLayout) {
            resizingColumn.setPreferredWidth(resizingColumn.getWidth());
        }
        resizeAndRepaint();
    }

    @Override
    public boolean editCellAt(int i, int i1, EventObject e) {
        Boolean flag = super.editCellAt(i, i1, e);
        if (flag) {
            if (e instanceof KeyEvent) {
                int code = ((KeyEvent) e).getKeyCode();
                if (code < KeyEvent.VK_DELETE
                        && code >= KeyEvent.VK_COMMA) {
                    getEditorComponent().requestFocusInWindow();
                }
            }
        }
        return flag;
    }

    public void setActionFor(String value, Action action) {
        getActionMap().put(value, action);
    }

    public void setKeyStrokeFor(String value, KeyStroke keyStroke) {
        getInputMap(JComponent.WHEN_FOCUSED).put(keyStroke, value);
    }

    public void setColumnRename(Action onColumnRenameAction, Integer... dontEditTheseColumns) {
        editHeader = EditHeader.setEditableHeader(this, onColumnRenameAction, dontEditTheseColumns);
    }

    public void disableColumnRename() {
        if (editHeader != null) {
            editHeader.disableEdit();
        }
    }

    public void searchFor(String text) {
        searchRenderer.searchRowMap.clear();
        Boolean isRegex = false;
        if (text != null && !text.isEmpty()) {
            if (text.startsWith("$")) {
                if (text.length() < 2) {
                    return;
                }
                isRegex = true;
                text = text.substring(1);
                if (!isRegexValid(text)) {
                    return;
                }
            }
            for (int row = 0; row < getRowCount(); row++) {
                for (int column = 0; column < getColumnCount(); column++) {
                    String value = Objects.toString(getValueAt(row, column), "");
                    if (isRegex ? value.matches(text) : value.contains(text)) {
                        if (!searchRenderer.searchRowMap.containsKey(row)) {
                            searchRenderer.searchRowMap.put(row, new ArrayList<Integer>());
                        }
                        searchRenderer.searchRowMap.get(row).add(column);
                    }
                }
            }
        }
        repaint();
    }

    private Boolean isRegexValid(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public void goToNextSearch() {
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();

        int nextRow = -1;
        int nextColumn = -1;

        if (!searchRenderer.searchRowMap.isEmpty()) {
            List<Integer> searchedRowList = new ArrayList<>(searchRenderer.searchRowMap.keySet());
            Collections.sort(searchedRowList);
            if (searchedRowList.contains(selectedRow)) {
                List<Integer> searchColumns = searchRenderer.searchRowMap.get(selectedRow);
                int index = searchColumns.indexOf(new Integer(selectedColumn));
                if (index + 1 < searchColumns.size()) {
                    nextRow = selectedRow;
                    nextColumn = searchColumns.get(index + 1);
                }
            }
            if (nextColumn == -1) {
                for (Integer searchedRow : searchedRowList) {
                    if (searchedRow > selectedRow) {
                        nextRow = searchedRow;
                        nextColumn = searchRenderer.searchRowMap.get(searchedRow).get(0);
                        break;
                    }
                }
            }
            if (nextColumn != -1) {
                changeSelection(nextRow, nextColumn, false, false);
            }
        }

    }

    public void goToPrevoiusSearch() {
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();

        int nextRow = -1;
        int nextColumn = -1;

        if (!searchRenderer.searchRowMap.isEmpty() && selectedColumn != -1) {
            List<Integer> searchedRowList = new ArrayList<>(searchRenderer.searchRowMap.keySet());
            Collections.sort(searchedRowList, Collections.reverseOrder());
            if (searchedRowList.contains(selectedRow)) {
                List<Integer> searchColumns = searchRenderer.searchRowMap.get(selectedRow);
                int index = searchColumns.indexOf(new Integer(selectedColumn));
                if (index - 1 != -1) {
                    nextRow = selectedRow;
                    nextColumn = searchColumns.get(index - 1);
                }
            }
            if (nextColumn == -1) {
                for (Integer searchedRow : searchedRowList) {
                    if (searchedRow < selectedRow) {
                        nextRow = searchedRow;
                        nextColumn = searchRenderer.searchRowMap.get(searchedRow)
                                .get(searchRenderer.searchRowMap.get(searchedRow).size() - 1);
                        break;
                    }
                }
            }
            if (nextColumn != -1) {
                changeSelection(nextRow, nextColumn, false, false);
            }
        }
    }

    public void cut() {
        XTableUtils.copyToClipboard(this, true);
    }

    public void copy() {
        XTableUtils.copyToClipboard(this, false);
    }

    public void paste() {
        XTableUtils.pasteFromClipboard(this);
    }

    public void selectColumn(int colIndex) {
        if (getRowCount() > 0) {
            changeSelection(0, colIndex, false, false);
            for (int i = 1; i < getRowCount(); i++) {
                changeSelection(i, colIndex, false, true);
            }
        }
    }

    //Implement table cell tool tips.
    @Override
    public String getToolTipText(MouseEvent e) {
        String tip = super.getToolTipText(e);
        if (tip != null && !tip.isEmpty()) {
            return tip;
        }
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);

        try {
            tip = getValueAt(rowIndex, colIndex).toString();
            if (tip.isEmpty()) {
                //hide tool tips in case of empty value
                tip = null;
            }
        } catch (RuntimeException e1) {
            //catch null pointer exception if mouse is over an empty line
        }

        return tip;
    }
}

class SearchRenderer extends DefaultTableCellRenderer {

    private static final Color BG_SELECT_COLOR = new Color(215, 215, 215);
    private static final Color BG_SEARCH_COLOR = Color.decode("#C8FACF");
    private static final Color DEF_SELECTION_COLOR = Color.decode("#39698a");
    private static final Color NOFOCUS_SELECTION_COLOR = Color.decode("#9AB2C2");

    public Map<Integer, List<Integer>> searchRowMap = new LinkedHashMap<>();

    TableCellRenderer defCellRenderer;

    Boolean focused = true;

    public SearchRenderer setDefRenderer(TableCellRenderer defCellRenderer) {
        this.defCellRenderer = defCellRenderer;
        return this;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent comp = (JComponent) defCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Boolean rowSelected = false;
        for (int srow : table.getSelectedRows()) {
            if (srow == row) {
                rowSelected = true;
                break;
            }
        }

        setSelectionColor(rowSelected, isSelected, comp, table.getBackground());
        setSearchColor(comp, row, column, isSelected);
        return comp;
    }

    private void setSearchColor(JComponent comp, int row, int column, Boolean cellSelected) {
        if (!cellSelected) {
            if (searchRowMap.get(row) != null && searchRowMap.get(row).indexOf(column) != -1) {
                comp.setBackground(BG_SEARCH_COLOR);
            }
        }
    }

    private void setSelectionColor(Boolean rowSelected, Boolean cellSelected,
            JComponent comp, Color defalutRowBgColor) {
        if (rowSelected) {
            comp.setBackground(BG_SELECT_COLOR);
        } else {
            comp.setBackground(defalutRowBgColor);
        }
        if (cellSelected) {
            if (focused) {
                comp.setBackground(DEF_SELECTION_COLOR);
            } else {
                comp.setBackground(NOFOCUS_SELECTION_COLOR);
            }
        }
    }
}
