package it.polimi.LabMGFwarehousemanager.daos;

import it.polimi.LabMGFwarehousemanager.beans.ItemBean;
import it.polimi.LabMGFwarehousemanager.beans.ReportBean;
import it.polimi.LabMGFwarehousemanager.enums.ReportStatus;

import java.sql.*;
import java.util.ArrayList;

public class ReportDAO {

    private final Connection connection;
    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    private final String CREATE_REPORT = "INSERT INTO reports (user, subject, body, status, date) VALUES (?, ?, ?, ?, ?)";
    private final String CREATE_REPORT_ITEM = "INSERT INTO report_items (report_id, item_id) VALUES (?, ?)";
    public void createReport(int reportUser, String reportSubject, String reportBody, ArrayList<Integer> items) throws SQLException {
        connection.setAutoCommit(false);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_REPORT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, reportUser);
            preparedStatement.setString(2, reportSubject);
            preparedStatement.setString(3, reportBody);
            preparedStatement.setInt(4, 0);
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            int reportId = resultSet.getInt(1);
            preparedStatement = connection.prepareStatement(CREATE_REPORT_ITEM);
            for (Integer item : items) {
                preparedStatement.setInt(1, reportId);
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

    private final String GET_ALL_REPORTS = "SELECT * FROM reports";
    public ArrayList<ReportBean> getAllReports() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_REPORTS);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ReportBean> reports = new ArrayList<>();
        while (resultSet.next()) {
            reports.add(createReportBean(resultSet));
        }
        return reports;
    }

    private final String GET_ITEMS_BY_REPORT_ID = "SELECT * FROM report_items WHERE report_id = ?";
    private ArrayList<ItemBean> getItemsByReportId(int reportId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ITEMS_BY_REPORT_ID);
        preparedStatement.setInt(1, reportId);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ItemBean> items = new ArrayList<>();
        ItemDAO itemDAO = new ItemDAO(connection);
        while (resultSet.next()) {
            items.add(itemDAO.getItemById(resultSet.getInt("item_id")));
        }
        return items;
    }

    private ReportBean createReportBean(ResultSet resultSet) {
        ReportBean report = new ReportBean();
        try {
            report.setId(resultSet.getInt("id"));
            report.setDate(resultSet.getTimestamp("date"));
            report.setSubject(resultSet.getString("subject"));
            UserDAO userDAO = new UserDAO(connection);
            report.setReportingUser(userDAO.getUserById(resultSet.getInt("user"), true));
            report.setBody(resultSet.getString("body"));
            report.setReportedItems(getItemsByReportId(report.getId()));
            report.setStatus(ReportStatus.getReportStatusFromInt(resultSet.getInt("status")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    private final String UPDATE_REPORT = "UPDATE reports SET status = ? WHERE id = ?";
    public void updateReport(int reportId, int reportStatus) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_REPORT);
        preparedStatement.setInt(1, reportStatus);
        preparedStatement.setInt(2, reportId);
        preparedStatement.executeUpdate();
    }
}
