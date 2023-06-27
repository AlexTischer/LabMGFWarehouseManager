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

    private final String selectItemByIdQuery = "SELECT * FROM items WHERE id = ?";
    public ItemBean selectItemById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectItemByIdQuery);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createItemBeanFromResultSet(resultSet);
        else
            return null;
    }

    private final String selectItemByInventoryNumberQuery = "SELECT * FROM items WHERE inventorynumber = ?";
    public ItemBean selectItemByInventoryNumber(String inventoryNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectItemByInventoryNumberQuery);
        preparedStatement.setString(1, inventoryNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createItemBeanFromResultSet(resultSet);
        else
            return null;
    }

    private final String selectItemBySerialNumberQuery = "SELECT * FROM items WHERE serialnumber = ?";
    public ItemBean selectItemBySerialNumber(String serialNumber) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectItemBySerialNumberQuery);
        preparedStatement.setString(1, serialNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createItemBeanFromResultSet(resultSet);
        else
            return null;
    }

    private String selectAllItemsByTypeAndLocationQuery = "SELECT * FROM items";
    public ArrayList<ItemBean> selectAllItemsByTypeAndLocation(int itemType, int itemLocation) throws SQLException {

        String tempQuery = selectAllItemsByTypeAndLocationQuery;

        if(itemType != -1) {
            tempQuery += " WHERE type = " + itemType;

            if(itemLocation != -1) {
                tempQuery += " AND location = " + itemLocation;
            }
        } else if(itemLocation != -1) {
            tempQuery += " WHERE location = " + itemLocation;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(tempQuery);

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

    private String selectAvailableItemsByTipeLocationAndTimeRange =
            "SELECT i.* FROM items i " +
                    "WHERE i.type = ? " +
                        "AND i.id NOT IN (" +
                            "SELECT ri.item_id FROM request_items ri INNER JOIN requests r ON ri.request_id = r.id " +
                                "WHERE (r.start <= ? " +
                                "AND r.end >= ?" +
                                "AND r.status = 1))";
    public ArrayList<ItemBean> selectAvailableItemsByTipeLocationAndTimeRange(int itemType, int itemLocation, Timestamp start, Timestamp end) throws SQLException {

        String tempQuery = selectAvailableItemsByTipeLocationAndTimeRange;
        if (itemLocation != -1) {
            tempQuery += " AND i.location = " + itemLocation;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(tempQuery);
        preparedStatement.setInt(1, itemType);
        preparedStatement.setTimestamp(2, start);
        preparedStatement.setTimestamp(3, end);
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


    private final String insertDocumentQuery = "INSERT INTO documents (name, path, type, language) VALUES (?, ?, ?, ?)";
    public void insertDocument(String name, String path, int type, int language) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(insertDocumentQuery);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, path);
        preparedStatement.setInt(3, type);
        preparedStatement.setInt(4, language);
        preparedStatement.executeUpdate();
    }

    private final String selectDocumentByIdQuery = "SELECT * FROM documents WHERE id = ?";
    public DocumentBean selectDocumentById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectDocumentByIdQuery);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
            return createDocumentBeanFromResultSet(resultSet);
        else
            return null;
    }

    private String selectAllDocumentsByTypeAndLanguageQuery = "SELECT * FROM documents";
    public ArrayList<DocumentBean> selectAllDocumentsByTypeAndLanguage(int type, int language) throws SQLException {
        if(type != -1) {
            selectAllDocumentsByTypeAndLanguageQuery += " WHERE type = " + type;

            if(language != -1) {
                selectAllDocumentsByTypeAndLanguageQuery += " AND language = " + language;
            }
        } else if(language != -1) {
            selectAllDocumentsByTypeAndLanguageQuery += " WHERE language = " + language;
        }
        PreparedStatement preparedStatement = connection.prepareStatement(selectAllDocumentsByTypeAndLanguageQuery);
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
        links.put("linkedAccessories", selectAllLinkedAccessories(0, toolId));
        links.put("nonLinkedAccessories", selectAllNonLinkedAccessories(0, toolId));
        links.put("linkedDocuments", selectAllLinkedDocuments(toolId));
        links.put("nonLinkedDocuments", selectAllNonLinkedDocuments(toolId));
        return links;
    }

    public Map<String, ArrayList<Object>> getDocumentLinks(int documentId) throws SQLException {
        Map<String, ArrayList<Object>> links = new HashMap<>();
        links.put("linkedTools", selectAllLinkedTools(2, documentId));
        links.put("nonLinkedTools", selectAllNonLinkedTools(2, documentId));
        links.put("linkedAccessories", selectAllLinkedAccessories(2, documentId));
        links.put("nonLinkedAccessories", selectAllNonLinkedAccessories(2, documentId));
        return links;
    }

    public Map<String, ArrayList<Object>> getAccessoryLinks(int accessoryId) throws SQLException {
        Map<String, ArrayList<Object>> links = new HashMap<>();
        links.put("linkedTools", selectAllLinkedTools(1, accessoryId));
        links.put("nonLinkedTools", selectAllNonLinkedTools(1, accessoryId));
        links.put("linkedDocuments", selectAllLinkedDocuments(accessoryId));
        links.put("nonLinkedDocuments", selectAllNonLinkedDocuments(accessoryId));
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


    private final String selectAllLinkedToolsToAccessoryQuery = "SELECT * FROM items WHERE id IN (SELECT toolId FROM items_links WHERE accessoryId = ?)";
    private final String selectAllLinkedToolsToDocumentQuery = "SELECT * FROM items WHERE id IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 0";
    private ArrayList<Object> selectAllLinkedTools(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 1) {
            preparedStatement = connection.prepareStatement(selectAllLinkedToolsToAccessoryQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(selectAllLinkedToolsToDocumentQuery);
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

    private final String selectAllNonLinkedToolsToAccessoryQuery = "SELECT * FROM items WHERE id NOT IN (SELECT toolId FROM items_links WHERE accessoryId = ?)";
    private final String selectAllNonLinkedToolsToDocumentQuery = "SELECT * FROM items WHERE id NOT IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 0";
    private ArrayList<Object> selectAllNonLinkedTools(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 1) {
            preparedStatement = connection.prepareStatement(selectAllNonLinkedToolsToAccessoryQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(selectAllNonLinkedToolsToDocumentQuery);
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

    private final String selectAllLinkedAccessoriesToToolQuery = "SELECT * FROM items WHERE id IN (SELECT accessoryId FROM items_links WHERE toolId = ?)";
    private final String selectAllLinkedAccessoriesToDocumentQuery = "SELECT * FROM items WHERE id IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 1";
    private ArrayList<Object> selectAllLinkedAccessories(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 0) {
            preparedStatement = connection.prepareStatement(selectAllLinkedAccessoriesToToolQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(selectAllLinkedAccessoriesToDocumentQuery);
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

    private final String selectAllNonLinkedAccessoriesToToolQuery = "SELECT * FROM items WHERE id NOT IN (SELECT accessoryId FROM items_links WHERE toolId = ?) AND type = 1";
    private final String selectAllNonLinkedAccessoriesToDocumentQuery = "SELECT * FROM items WHERE id NOT IN (SELECT itemId FROM documents_links WHERE documentId = ?) AND type = 1";
    private ArrayList<Object> selectAllNonLinkedAccessories(int itemType, int itemId) throws SQLException {
        PreparedStatement preparedStatement = null;
        if(itemType == 0) {
            preparedStatement = connection.prepareStatement(selectAllNonLinkedAccessoriesToToolQuery);
            preparedStatement.setInt(1, itemId);
        } else if(itemType == 2) {
            preparedStatement = connection.prepareStatement(selectAllNonLinkedAccessoriesToDocumentQuery);
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

    private final String selectAllLinkedDocumentsQuery = "SELECT * FROM documents WHERE id IN (SELECT documentId FROM documents_links WHERE itemId = ?)";
    private ArrayList<Object> selectAllLinkedDocuments(int itemId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectAllLinkedDocumentsQuery);
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

    private final String selectAllNonLinkedDocumentsQuery = "SELECT * FROM documents WHERE id NOT IN (SELECT documentId FROM documents_links WHERE itemId = ?)";
    private ArrayList<Object> selectAllNonLinkedDocuments(int itemId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(selectAllNonLinkedDocumentsQuery);
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
}
