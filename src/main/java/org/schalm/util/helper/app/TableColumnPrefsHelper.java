package org.schalm.util.helper.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper to load, restore and save the size of each column in a {@link JTable}.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: TableColumnPrefsHelper.java 161 2014-03-06 13:27:59Z cschalm $
 */
public class TableColumnPrefsHelper {
    private final String configFileName;
    private final List<TableConfig> tablesList;

    /**
     * Constructor taking the fileName to save the configursation to or load from.
     *
     * @param configFileName filename for config
     */
    public TableColumnPrefsHelper(String configFileName) {
        this.configFileName = configFileName;
        this.tablesList = new ArrayList<>();
    }

    /**
     * Add a new table to the configuration.
     *
     * @param config table-config to add
     */
    public void addTable(TableConfig config) {
        tablesList.add(config);
    }

    /**
     * Save the configuration for all known tables.
     */
    public void save() {
        if (!tablesList.isEmpty()) {
            UiConfig uiConfig = new UiConfig();
            for (TableConfig tableConfig : tablesList) {
                TableUiConfig tableUiConfig = new TableUiConfig();
                tableUiConfig.setTableName(tableConfig.getConfigElementName());
                TableColumnModel columnModel = tableConfig.getTable().getColumnModel();
                Enumeration<TableColumn> columns = columnModel.getColumns();
                int i = 0;
                while (columns.hasMoreElements()) {
                    TableColumn column = columns.nextElement();
                    TableColumnUiConfig tableColumnUiConfig = new TableColumnUiConfig();
                    tableColumnUiConfig.setIndex(i++);
                    tableColumnUiConfig.setSize(column.getPreferredWidth());
                    tableUiConfig.getColumns().add(tableColumnUiConfig);
                }
                uiConfig.getTables().add(tableUiConfig);
            }
            File uiConfigFile = new File(configFileName);
            JAXB.marshal(uiConfig, uiConfigFile);
        }
    }

    /**
     * Load the configuration for all known tables and adjust the look of all columns.
     */
    public void load() {
        if (!tablesList.isEmpty()) {
            File uiConfigFile = new File(configFileName);
            if (uiConfigFile.exists() && uiConfigFile.canRead()) {
                UiConfig uiConfig = JAXB.unmarshal(uiConfigFile, UiConfig.class);
                for (TableConfig tableConfig : tablesList) {
                    TableUiConfig tableUiConfig = uiConfig.getConfig4Table(tableConfig.getConfigElementName());
                    if (tableUiConfig != null) {
                        TableColumnModel columnModel = tableConfig.getTable().getColumnModel();
                        for (TableColumnUiConfig tableColumnUiConfig : tableUiConfig.getColumns()) {
                            TableColumn column = columnModel.getColumn(tableColumnUiConfig.getIndex());
                            column.setPreferredWidth(tableColumnUiConfig.getSize());
                        }
                    }
                }
            }
        }
    }

    /**
     * Configuration for this helper. Holds the table to save and restore and a name for it.
     *
     * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
     * @version $Id: TableColumnPrefsHelper.java 161 2014-03-06 13:27:59Z cschalm $
     */
    public static class TableConfig {
        private final String configElementName;
        private final JTable table;

        public TableConfig(String configElementName, JTable table) {
            this.configElementName = configElementName;
            this.table = table;
        }

        public String getConfigElementName() {
            return configElementName;
        }

        public JTable getTable() {
            return table;
        }

    }

    /**
     * Configuration how to display the tables in the ui.
     *
     * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
     * @version $Id: TableColumnPrefsHelper.java 161 2014-03-06 13:27:59Z cschalm $
     */
    @XmlRootElement(name = "uiConfig", namespace = "http://www.schalm.org")
    public static final class UiConfig {
        private Set<TableUiConfig> tables = new HashSet<>();

        public UiConfig() {
        }

        @XmlElement(name = "table")
        public Set<TableUiConfig> getTables() {
            return tables;
        }

        public void setTables(Set<TableUiConfig> tables) {
            this.tables = tables;
        }

        public TableUiConfig getConfig4Table(String tableName) {
            for (TableUiConfig tableUiConfig : tables) {
                if (tableName.equalsIgnoreCase(tableUiConfig.getTableName())) {
                    return tableUiConfig;
                }
            }

            return null;
        }

    }

    /**
     * Configuration how to display a table with it's columns.
     *
     * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
     * @version $Id: TableColumnPrefsHelper.java 161 2014-03-06 13:27:59Z cschalm $
     */
    public static final class TableUiConfig {
        private String tableName;
        private Set<TableColumnUiConfig> columns = new HashSet<>();

        public TableUiConfig() {
        }

        @XmlElement(name = "column")
        public Set<TableColumnUiConfig> getColumns() {
            return columns;
        }

        public void setColumns(Set<TableColumnUiConfig> columns) {
            this.columns = columns;
        }

        @XmlAttribute
        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

    }

    /**
     * Configuration how to display a column in a table.
     *
     * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
     * @version $Id: TableColumnPrefsHelper.java 161 2014-03-06 13:27:59Z cschalm $
     */
    public static final class TableColumnUiConfig {
        private int index;
        private int size;

        public TableColumnUiConfig() {
        }

        @XmlAttribute
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @XmlAttribute
        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

    }

}
