package it.polimi.geodesicwarehousemanager.daos;

import it.polimi.geodesicwarehousemanager.beans.ItemBean;
import it.polimi.geodesicwarehousemanager.beans.RequestBean;
import it.polimi.geodesicwarehousemanager.enums.RequestStatus;

import java.sql.*;
import java.util.ArrayList;

public class RequestDAO {
    private final Connection connection;
    public RequestDAO(Connection connection) {
        this.connection = connection;
    }


    private final String GET_REQUESTS_BY_TIMERANGE = "SELECT * FROM requests WHERE ((start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?))";
    public ArrayList<RequestBean> getRequestsByTimeRange(Timestamp start, Timestamp end) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_REQUESTS_BY_TIMERANGE);
        preparedStatement.setTimestamp(1, start);
        preparedStatement.setTimestamp(2, end);
        preparedStatement.setTimestamp(3, start);
        preparedStatement.setTimestamp(4, end);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<RequestBean> requestBeans = new ArrayList<>();
        while (resultSet.next()){
            requestBeans.add(createRequestBeanByResultSet(resultSet));
        }
        return requestBeans;
    }


    private final String GET_ALL_REQUESTS = "SELECT * FROM requests";
    public ArrayList<RequestBean> getAllRequests() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_REQUESTS);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<RequestBean> requestBeans = new ArrayList<>();
        while (resultSet.next()){
            requestBeans.add(createRequestBeanByResultSet(resultSet));
        }
        return requestBeans;
    }

    private final String GET_REQUESTS_BY_USER = "SELECT * FROM requests WHERE user_id = ?";
    public ArrayList<RequestBean> getRequestsByUserId(int userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_REQUESTS_BY_USER);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<RequestBean> requestBeans = new ArrayList<>();
        while (resultSet.next()){
            requestBeans.add(createRequestBeanByResultSet(resultSet));
        }
        return requestBeans;
    }

    private RequestBean createRequestBeanByResultSet(ResultSet resultSet) throws SQLException {
        RequestBean requestBean = new RequestBean();
        requestBean.setId(resultSet.getInt("id"));
        requestBean.setStart(resultSet.getTimestamp("start").toString().split(" ")[0]);
        requestBean.setEnd(resultSet.getTimestamp("end").toString().split(" ")[0]);
        requestBean.setStatus(RequestStatus.getRequestStatusFromInt(resultSet.getInt("status")));
        requestBean.setReason(resultSet.getString("reason"));
        UserDAO userDAO = new UserDAO(connection);
        requestBean.setRequestingUser(userDAO.getUserById(resultSet.getInt("user_id")));
        requestBean.setRequestedItems(getItemsByRequestId(requestBean.getId()));
        return requestBean;
    }

    private final String GET_ITEMS_BY_REQUEST_ID = "SELECT * FROM request_items WHERE request_id = ?";
    private ArrayList<ItemBean> getItemsByRequestId(int requestId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_BY_REQUEST_ID);
        preparedStatement.setInt(1, requestId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ItemBean> items = new ArrayList<>();
        ItemDAO itemDAO = new ItemDAO(connection);
        while (resultSet.next()){
            items.add(itemDAO.getItemById(resultSet.getInt("item_id")));
        }
        return items;
    }

    public ArrayList<Integer> getItemsIdByRequestId(int requestId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_BY_REQUEST_ID);
        preparedStatement.setInt(1, requestId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Integer> items = new ArrayList<>();
        while (resultSet.next()){
            items.add(resultSet.getInt("item_id"));
        }
        return items;
    }

    private final String CREATE_REQUEST = "INSERT INTO requests (start, end, reason, user_id) VALUES (?, ?, ?, ?)";
    private final String CREATE_REQUEST_ITEM = "INSERT INTO request_items (request_id, item_id) VALUES (?, ?)";
    public void createRequest(int userId, Timestamp start, Timestamp end, ArrayList<Integer> items, String reason) throws SQLException {
        connection.setAutoCommit(false);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_REQUEST, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, start);
            preparedStatement.setTimestamp(2, end);
            preparedStatement.setString(3, reason);
            preparedStatement.setInt(4, userId);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int requestId = resultSet.getInt(1);
            preparedStatement = connection.prepareStatement(CREATE_REQUEST_ITEM);
            for (Integer item : items) {
                preparedStatement.setInt(1, requestId);
                preparedStatement.setInt(2, item);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private final String UPDATE_REQUEST = "UPDATE requests SET status = ?, adminNotes = ? WHERE id = ?";
    private final String UPDATE_REQUEST_DOCUMENTS = "INSERT INTO request_documents (request_id, document_id) VALUES (?, ?)";
    public void updateRequest(int id, RequestStatus status, String adminNotes, ArrayList<Integer> documents) throws SQLException {
        connection.setAutoCommit(false);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_REQUEST);
            preparedStatement.setInt(1, status.getValue());
            preparedStatement.setString(2, adminNotes);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement(UPDATE_REQUEST_DOCUMENTS);
            for (Integer document : documents) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, document);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

}
