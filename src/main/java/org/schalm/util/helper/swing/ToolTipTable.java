package org.schalm.util.helper.swing;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * <code>JTable</code> showing every cell's content in a tooltip.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: ToolTipTable.java 158 2014-03-01 22:22:07Z cschalm $
 */
public class ToolTipTable extends JTable {
    private static final long serialVersionUID = -8081302665127448798L;

    public ToolTipTable() {
        super();
    }

    public ToolTipTable(TableModel dm) {
        super(dm);
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        Point p = e.getPoint();
        int rowIndex = this.rowAtPoint(p);
        int colIndex = this.columnAtPoint(p);
        return this.getValueAt(rowIndex, colIndex).toString();
    }

}
