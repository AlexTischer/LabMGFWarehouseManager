package it.polimi.geodesicwarehousemanager.daos;

import it.polimi.geodesicwarehousemanager.beans.DocumentBean;
import it.polimi.geodesicwarehousemanager.beans.ItemBean;
import it.polimi.geodesicwarehousemanager.enums.DocumentLanguage;
import it.polimi.geodesicwarehousemanager.enums.DocumentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ItemDAO {
    private final Connection connection;
    public ItemDAO(Connection connection) {
        this.connection = connection;
    }

    private final String insertItemQuery = "INSERT INTO items (name, description, type, serialnumber, inventorynumber, location, imagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public void insertItem(String name, String description, int itemType, String serialNumber, String inventoryNumber, int itemLocation, String imagePath) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(insertItemQuery);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.setInt(3, itemType);
        preparedStatement.setString(4, serialNumber);
        preparedStatement.setString(5, inventoryNumber);
        preparedStatement.setInt(6, itemLocation);
        preparedStatement.setString(7, imagePath);
        preparedStatement.executeUpdate();
    }

    private final String getItemByIdQuery = "SELECT * FROM items WHERE id = ?";
    public ItemBean getItemById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getItemByIdQuery);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createItemBeanFromResultSet(resultSet);
        else
            return null;
    }

    private final String getItemByInventoryNumberQuery = "SELECT * FROM items WHERE inventorynumber = ?";
    public ItemBean getItemByInventoryNumber(String inventoryNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getItemByInventoryNumberQuery);
        preparedStatement.setString(1, inventoryNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createItemBeanFromResultSet(resultSet);
        else
            return null;
    }

    private final String getItemBySerialNumberQuery = "SELECT * FROM items WHERE serialnumber = ?";
    public ItemBean getItemBySerialNumber(String serialNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getItemBySerialNumberQuery);
        preparedStatement.setString(1, serialNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createItemBeanFromResultSet(resultSet);
        else
            return null;
    }

    private String getAllItemsByTypeQuery = "SELECT * FROM items WHERE type = ?";
    public ArrayList<ItemBean> getAllItemsByType(int itemType) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(getAllItemsByTypeQuery);
        preparedStatement.setInt(1, itemType);

        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ItemBean> itemBeans = new ArrayList<>();
        while (resultSet.next()){
            itemBeans.add(createItemBeanFromResultSet(resultSet));
        }
        if(itemBeans.size() == 0)
            return null;
        else
            return itemBeans;
    }

    private String getAvailableToolsByTimeRange = "SELECT * FROM items WHERE type = 0" +
            "                      AND id NOT IN (SELECT item_id FROM maintenance m WHERE ((? BETWEEN start AND end)" +
            "                                                                            OR (? BETWEEN start AND end)" +
            "                                                                            OR (start BETWEEN ? AND ?)" +
            "                                                                            OR (end BETWEEN ? AND ?)" +
            "                                                                            )" +
            "                                                                       )" +
            "                      AND id NOT IN (SELECT item_id FROM request_items JOIN requests r on r.id = request_items.request_id" +
            "                                        WHERE r.status = 1 AND ((? BETWEEN r.start AND r.end)" +
            "                                                            OR (? BETWEEN r.start AND r.end)" +
            "                                                            OR (r.start BETWEEN ? AND ?)" +
            "                                                            OR (r.end BETWEEN ? AND ?)" +
            "                                                            )" +
            "                                        )";

    private String getTools = "SELECT * FROM items WHERE type = 0";
    public ArrayList<ItemBean> getAvailableToolsByTimeRange(Timestamp start, Timestamp end) throws SQLException {

        ResultSet resultSet;
        ArrayList<ItemBean> itemBeans = new ArrayList<>();

        if(start == null || end == null) {
            PreparedStatement preparedStatement = connection.prepareStatement(getTools);
            resultSet = preparedStatement.executeQuery();
        } else {

            PreparedStatement preparedStatement = connection.prepareStatement(getAvailableToolsByTimeRange);
            preparedStatement.setTimestamp(1, start);
            preparedStatement.setTimestamp(2, end);
            preparedStatement.setTimestamp(3, start);
            preparedStatement.setTimestamp(4, end);
            preparedStatement.setTimestamp(5, start);
            preparedStatement.setTimestamp(6, end);
            preparedStatement.setTimestamp(7, start);
            preparedStatement.setTimestamp(8, end);
            preparedStatement.setTimestamp(9, start);
            preparedStatement.setTimestamp(10, end);
            preparedStatement.setTimestamp(11, start);
            preparedStatement.setTimestamp(12, end);

            resultSet = preparedStatement.executeQuery();
        }
        while (resultSet.next()) {
                itemBeans.add(createItemBeanFromResultSet(resultSet));
            }
            if (itemBeans.size() == 0)
                return null;
            else
                return itemBeans;
    }


    private String getAvailableAccessoriesByToolAndTimeRange = "SELECT * FROM items WHERE type = 1" +
            "                      AND id IN (SELECT accessoryId FROM items_links WHERE toolId = ?)" +
            "                      AND id NOT IN (SELECT item_id FROM maintenance m WHERE ((? BETWEEN start AND end)" +
            "                                                                            OR (? BETWEEN start AND end)" +
            "                                                                            OR (start BETWEEN ? AND ?)" +
            "                                                                            OR (end BETWEEN ? AND ?)" +
            "                                                                            )" +
            "                                                                       )" +
            "                      AND id NOT IN (SELECT item_id FROM request_items JOIN requests r on r.id = request_items.request_id" +
            "                                        WHERE r.status = 1 AND ((? BETWEEN r.start AND r.end)" +
            "                                                            OR (? BETWEEN r.start AND r.end)" +
            "                                                            OR (r.start BETWEEN ? AND ?)" +
            "                                                            OR (r.end BETWEEN ? AND ?)" +
            "                                                            )" +
            "                                        )";
    private String getAccessoriesByTool = "SELECT * FROM items WHERE type = 1" +
            "                      AND id IN (SELECT accessoryId FROM items_links WHERE toolId = ?)";

    public ArrayList<ItemBean> getAvailableAccessoriesByToolAndTimeRange(int toolId, Timestamp start, Timestamp end) throws SQLException {
        PreparedStatement preparedStatement;
        if(start!=null && end!=null) {
            preparedStatement = connection.prepareStatement(getAvailableAccessoriesByToolAndTimeRange);
            preparedStatement.setInt(1, toolId);
            preparedStatement.setTimestamp(2, start);
            preparedStatement.setTimestamp(3, end);
            preparedStatement.setTimestamp(4, start);
            preparedStatement.setTimestamp(5, end);
            preparedStatement.setTimestamp(6, start);
            preparedStatement.setTimestamp(7, end);
            preparedStatement.setTimestamp(8, start);
            preparedStatement.setTimestamp(9, end);
            preparedStatement.setTimestamp(10, start);
            preparedStatement.setTimestamp(11, end);
            preparedStatement.setTimestamp(12, start);
            preparedStatement.setTimestamp(13, end);
        } else {
            preparedStatement = connection.prepareStatement(getAccessoriesByTool);
            preparedStatement.setInt(1, toolId);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ItemBean> itemBeans = new ArrayList<>();
        while (resultSet.next()){
            itemBeans.add(createItemBeanFromResultSet(resultSet));
        }
        if(itemBeans.size() == 0)
            return null;
        else
            return itemBeans;
    }


    private final String removeItemByIdQuery = "DELETE FROM items WHERE id = ?";
    public void removeItemById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(removeItemByIdQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public void updateItemById(int id, String name, String description, int itemLocation, String imagePath) throws SQLException {
        System.out.println("updateItemById");
        System.out.println("id: " + id + " name: " + name + " description: " + description + " itemLocation: " + itemLocation + " imagePath: " + imagePath);
        StringBuilder stringBuilder = new StringBuilder("UPDATE items SET ");
        stringBuilder.append("name = '" + name + "'");
        stringBuilder.append(", description = '" + description + "'");
        stringBuilder.append(", location = " + itemLocation);
        if (imagePath != null) {
            stringBuilder.append(", imagePath = '" + imagePath + "'");
        }
        stringBuilder.append(" WHERE id = " + id);
        PreparedStatement preparedStatement = connection.prepareStatement(stringBuilder.toString());
        preparedStatement.executeUpdate();
    }


    private final String insertDocumentQuery = "INSERT INTO documents (name, path, type, language) VALUES (?, ?, ?, ?)";
    public DocumentBean insertDocument(String name, String path, int type, int language) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(insertDocumentQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, path);
        preparedStatement.setInt(3, type);
        preparedStatement.setInt(4, language);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if(resultSet.next())
            return getDocumentById(resultSet.getInt(1));
        else
            return null;
    }

    private final String getDocumentByIdQuery = "SELECT * FROM documents WHERE id = ?";
    public DocumentBean getDocumentById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getDocumentByIdQuery);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createDocumentBeanFromResultSet(resultSet);
        else
            return null;
    }

    private String getAllDocumentsByTypeAndLanguageQuery = "SELECT * FROM documents";
    public ArrayList<DocumentBean> getAllDocumentsByTypeAndLanguage(int type, int language) throws SQLException {
        if(type != -1) {
            getAllDocumentsByTypeAndLanguageQuery += " WHERE type = " + type;

            if(language != -1) {
                getAllDocumentsByTypeAndLanguageQuery += " AND language = " + language;
            }
        } else if(language != -1) {
            getAllDocumentsByTypeAndLanguageQuery += " WHERE language = " + language;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(getAllDocumentsByTypeAndLanguageQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<DocumentBean> documentBeans = new ArrayList<>();
        while (resultSet.next()){
            documentBeans.add(createDocumentBeanFromResultSet(resultSet));
        }
        if(documentBeans.size() == 0)
            return null;
        else
            return documentBeans;
    }

    private final String removeDocumentByIdQuery = "DELETE FROM documents WHERE id = ?";
    public void removeDocumentById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(removeDocumentByIdQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public Map<String, ArrayList<Object>> getToolLinks(int toolId) throws SQLException {
        Map<String, ArrayList<Object>> links = new HashMap<>();
        links.put("linkedAccessories", getAllLinkedAccessories(0, toolId));
        links.put("nonLinkedAccessories", getAllNonLinkedAccessories(0, toolId));
        links.put("linkedDocuments", getAllLinkedDocuments(toolId));
        links.put("nonLinkedDocuments", getAllNonLinkedDocuments(toolId));
        return links;
    }

    public Map<String, ArrayList<Object>> getDocumentLinks(int documentId) throws SQLException {
        Map<String, ArrayList<Object>> links = new HashMap<>();
        links.put("linkedTools", getAllLinkedTools(2, documentId));
        links.put("nonLinkedTools", getAllNonLinkedTools(2, documentId));
        links.put("linkedAccessories", getAllLinkedAccessories(2, documentId));
        links.put("nonLinkedAccessories", getAllNonLinkedAccessories(2, documentId));
        return links;
    }

    public Map<String, ArrayList<Object>> getAccessoryLinks(int accessoryId) throws SQLException {
        Map<String, ArrayList<Object>> links = new HashMap<>();
        links.put("linkedTools", getAllLinkedTools(1, accessoryId));
        links.put("nonLinkedTools", getAllNonLinkedTools(1, accessoryId));
        links.put("linkedDocuments", getAllLinkedDocuments(accessoryId));
        links.put("nonLinkedDocuments", getAllNonLinkedDocuments(accessoryId));
        return links;
    }

    private final String insertItemsLinkQuery = "INSERT IGNORE INTO items_links (toolId, accessoryId) VALUES (?, ?)";
    public void linkItem(int itemId, int itemType, ArrayList<Integer> linkedItemsIds, ArrayList<Integer> linkedDocumentsIds) throws SQLException {
        if(linkedItemsIds != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertItemsLinkQuery);
            for (int linkedItemId : linkedItemsIds) {
                preparedStatement.setInt(1, itemType == 0 ? itemId : linkedItemId);
                preparedStatement.setInt(2, itemType == 0 ? linkedItemId : itemId);
                preparedStatement.executeUpdate();
            }
        }
        if(linkedDocumentsIds != null) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertDocumentsLinkQuery);
            for (int linkedDocumentId : linkedDocumentsIds) {
                preparedStatement.setInt(1, itemId);
                preparedStatement.setInt(2, linkedDocumentId);
                preparedStatement.executeUpdate();
            }
        }
    }
    private final String insertDocumentsLinkQuery = "INSERT IGNORE INTO documents_links (itemId, documentId) VALUES (?, ?)";
    public void linkDocument(int documentId, ArrayList<Integer> linkedItemsIds) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(insertDocumentsLinkQuery);
        for(int itemId : linkedItemsIds) {
            preparedStatement.setInt(1, itemId);
            preparedStatement.setInt(2, documentId);
            preparedStatement.executeUpdate();
        }
    }

    public ArrayList<Map<String, String>> getUnavailablePeriods(ArrayList<Integer> items) throws SQLException {

        ArrayList<Map<String,String>> periods = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT r.start, r.end ");
        queryBuilder.append("FROM requests r ");
        queryBuilder.append("JOIN request_items ri ON r.id = ri.request_id ");
        queryBuilder.append("WHERE r.end >= NOW() AND ");
        queryBuilder.append("ri.item_id IN (");
        for (int i = 0; i < items.size(); i++) {
            queryBuilder.append("?");
            if (i < items.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");
        String query = queryBuilder.toString();

        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < items.size(); i++) {
                statement.setInt(i + 1, items.get(i));
            }
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Map<String, String> period = new HashMap<>();
            period.put("start", resultSet.getTimestamp("start").toString().split(" ")[0]);
            period.put("end", resultSet.getTimestamp("end").toString().split(" ")[0]);
            periods.add(period);
        }

        StringBuilder queryBuilder2 = new StringBuilder();
        queryBuilder2.append("SELECT DISTINCT m.start, m.end ");
        queryBuilder2.append("FROM maintenance m ");
        queryBuilder2.append("WHERE m.end >= NOW() AND ");
        queryBuilder2.append("m.item_id IN (");
        for (int i = 0; i < items.size(); i++) {
            queryBuilder2.append("?");
            if (i < items.size() - 1) {
                queryBuilder2.append(", ");
            }
        }
        queryBuilder2.append(")");
        String query2 = queryBuilder2.toString();

        PreparedStatement statement2 = connection.prepareStatement(query2);
        for (int i = 0; i < items.size(); i++) {
            statement2.setInt(i + 1, items.get(i));
        }
        ResultSet resultSet2 = statement2.executeQuery();

        while (resultSet2.next()) {
            Map<String, String> period = new HashMap<>();
            period.put("start", resultSet2.getTimestamp("start").toString().split(" ")[0]);
            period.put("end", resultSet2.getTimestamp("end").toString().split(" ")[0]);
            periods.add(period);
        }

        return periods;
    }

    private final String addToMaintenanceQuery = "INSERT INTO maintenance (item_id, reason, start, end) VALUES (?, ?, ?, ?)";
    private final String removeFromMaintenanceQuery = "UPDATE maintenance SET end = NOW() WHERE item_id = ?";
    public void updateMaintenance(int itemId, int maintenanceStatus, String reason, Timestamp start, Timestamp end) throws SQLException {
        if(maintenanceStatus ==1) {
            PreparedStatement preparedStatement = connection.prepareStatement(addToMaintenanceQuery);
            preparedStatement.setInt(1, itemId);
            preparedStatement.setString(2, reason);
            preparedStatement.setTimestamp(3, start);
            preparedStatement.setTimestamp(4, end);
            preparedStatement.executeUpdate();
        } else if(maintenanceStatus == 0) {
            PreparedStatement preparedStatement = connection.prepareStatement(removeFromMaintenanceQuery);
            preparedStatement.setInt(1, itemId);
            preparedStatement.executeUpdate();
        }
    }

    private final String getAllLinkedToolsToAccessoryQuery = "SELECT * FROM items WHERE id IN (SELECT toolId FROM items_links WHERE accessoryId = ?)";
    private final String getAllLinkedToolsToDocumentQuery = "SELECT * FROM items WHERE id IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 0";

    private ArrayList<Object> getAllLinkedTools(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 1) {
            preparedStatement = connection.prepareStatement(getAllLinkedToolsToAccessoryQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(getAllLinkedToolsToDocumentQuery);
            preparedStatement.setInt(1, itemId);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Object> itemBeans = new ArrayList<>();
        while (resultSet.next()){
            ItemBean tool = createItemBeanFromResultSet(resultSet);
            tool.setIsLinked(true);
            itemBeans.add(tool);
        }
        if(itemBeans.size() == 0)
            return null;
        else
            return itemBeans;
    }
    private final String getAllNonLinkedToolsToAccessoryQuery = "SELECT * FROM items WHERE id NOT IN (SELECT toolId FROM items_links WHERE accessoryId = ?)";
    private final String getAllNonLinkedToolsToDocumentQuery = "SELECT * FROM items WHERE id NOT IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 0";

    private ArrayList<Object> getAllNonLinkedTools(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 1) {
            preparedStatement = connection.prepareStatement(getAllNonLinkedToolsToAccessoryQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(getAllNonLinkedToolsToDocumentQuery);
            preparedStatement.setInt(1, itemId);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Object> itemBeans = new ArrayList<>();
        while (resultSet.next()){
            ItemBean tool = createItemBeanFromResultSet(resultSet);
            tool.setIsLinked(false);
            itemBeans.add(tool);
        }
        if(itemBeans.size() == 0)
            return null;
        else
            return itemBeans;
    }
    private final String getAllLinkedAccessoriesToToolQuery = "SELECT * FROM items WHERE id IN (SELECT accessoryId FROM items_links WHERE toolId = ?)";
    private final String getAllLinkedAccessoriesToDocumentQuery = "SELECT * FROM items WHERE id IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 1";

    private ArrayList<Object> getAllLinkedAccessories(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 0) {
            preparedStatement = connection.prepareStatement(getAllLinkedAccessoriesToToolQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(getAllLinkedAccessoriesToDocumentQuery);
            preparedStatement.setInt(1, itemId);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Object> itemBeans = new ArrayList<>();
        while (resultSet.next()){
            ItemBean accessory = createItemBeanFromResultSet(resultSet);
            accessory.setIsLinked(true);
            itemBeans.add(accessory);
        }
        if(itemBeans.size() == 0)
            return null;
        else
            return itemBeans;
    }
    private final String getAllNonLinkedAccessoriesToToolQuery = "SELECT * FROM items WHERE id NOT IN (SELECT accessoryId FROM items_links WHERE toolId = ?) AND type = 1";
    private final String getAllNonLinkedAccessoriesToDocumentQuery = "SELECT * FROM items WHERE id NOT IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 1";

    private ArrayList<Object> getAllNonLinkedAccessories(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 0) {
            preparedStatement = connection.prepareStatement(getAllNonLinkedAccessoriesToToolQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(getAllNonLinkedAccessoriesToDocumentQuery);
            preparedStatement.setInt(1, itemId);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Object> itemBeans = new ArrayList<>();
        while (resultSet.next()){
            ItemBean accessory = createItemBeanFromResultSet(resultSet);
            accessory.setIsLinked(false);
            itemBeans.add(accessory);
        }
        if(itemBeans.size() == 0)
            return null;
        else
            return itemBeans;
    }
    private final String getAllLinkedDocumentsQuery = "SELECT * FROM documents WHERE id IN (SELECT documentId FROM documents_links WHERE itemId = ?)";

    private ArrayList<Object> getAllLinkedDocuments(int itemId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getAllLinkedDocumentsQuery);
        preparedStatement.setInt(1, itemId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Object> documentBeans = new ArrayList<>();
        while (resultSet.next()){
            DocumentBean document = createDocumentBeanFromResultSet(resultSet);
            document.setIsLinked(true);
            documentBeans.add(document);
        }
        if(documentBeans.size() == 0)
            return null;
        else
            return documentBeans;
    }
    private final String getAllNonLinkedDocumentsQuery = "SELECT * FROM documents WHERE id NOT IN (SELECT documentId FROM documents_links WHERE itemId = ?)";

    private ArrayList<Object> getAllNonLinkedDocuments(int itemId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getAllNonLinkedDocumentsQuery);
        preparedStatement.setInt(1, itemId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Object> documentBeans = new ArrayList<>();
        while (resultSet.next()){
            DocumentBean document = createDocumentBeanFromResultSet(resultSet);
            document.setIsLinked(false);
            documentBeans.add(document);
        }
        if(documentBeans.size() == 0)
            return null;
        else
            return documentBeans;
    }
    private final String getMaintenanceStatusQuery = "SELECT * FROM maintenance WHERE item_id = ? AND start < NOW() AND end > NOW()";
    private boolean isInMaintenance(int itemId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getMaintenanceStatusQuery);
        preparedStatement.setInt(1, itemId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private ItemBean createItemBeanFromResultSet(ResultSet resultSet) throws SQLException {
        ItemBean itemBean = new ItemBean();

        itemBean.setId(resultSet.getInt("id"));
        itemBean.setName(resultSet.getString("name"));
        itemBean.setDescription(resultSet.getString("description"));
        itemBean.setType(resultSet.getInt("type"));
        itemBean.setSerialNumber(resultSet.getString("serialnumber"));
        itemBean.setInventoryNumber(resultSet.getString("inventorynumber"));
        itemBean.setLocation(resultSet.getInt("location"));
        itemBean.setImagePath(resultSet.getString("imagePath"));
        itemBean.setIsInMaintenance(isInMaintenance(itemBean.getId()));

        return itemBean;
    }

    private DocumentBean createDocumentBeanFromResultSet(ResultSet resultSet) throws SQLException {
        DocumentBean documentBean = new DocumentBean();

        documentBean.setId(resultSet.getInt("id"));
        documentBean.setName(resultSet.getString("name"));
        documentBean.setPath(resultSet.getString("path"));
        documentBean.setType(DocumentType.getDocumentTypeFromInt(resultSet.getInt("type")));
        documentBean.setLanguage(DocumentLanguage.getDocumentLanguageFromInt(resultSet.getInt("language")));

        return documentBean;
    }

    public ArrayList<DocumentBean> getDocumentsByItemId(int item) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(getAllLinkedDocumentsQuery);
        preparedStatement.setInt(1, item);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<DocumentBean> documentBeans = new ArrayList<>();
        while (resultSet.next()){
            DocumentBean document = createDocumentBeanFromResultSet(resultSet);
            documentBeans.add(document);
        }
        if(documentBeans.size() == 0)
            return null;
        else
            return documentBeans;
    }
}
