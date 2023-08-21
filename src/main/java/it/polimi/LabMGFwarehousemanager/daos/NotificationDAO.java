package it.polimi.LabMGFwarehousemanager.daos;

import it.polimi.LabMGFwarehousemanager.beans.NotificationBean;
import it.polimi.LabMGFwarehousemanager.enums.NotificationType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class NotificationDAO {
    private final Connection connection;

    public NotificationDAO(Connection connection){
        this.connection = connection;
    }

    private final String GET_NOTIFICATIONS = "SELECT * FROM notifications WHERE user_id = ? AND status = 0";
    public ArrayList<NotificationBean> getNotifications(int userId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_NOTIFICATIONS);
        preparedStatement.setInt(1, userId);
        preparedStatement.executeQuery();
        ArrayList<NotificationBean> notifications = new ArrayList<>();
        while (preparedStatement.getResultSet().next()) {
            NotificationBean notification = new NotificationBean();
            notification.setId(preparedStatement.getResultSet().getInt("id"));
            notification.setStatus(preparedStatement.getResultSet().getInt("status"));
            notification.setType(NotificationType.getNotificationTypeFromInt(preparedStatement.getResultSet().getInt("type")));
            notification.setMessage(preparedStatement.getResultSet().getString("message"));
            notifications.add(notification);
        }
        return notifications;
    }

    private final String MARK_NOTIFICATION_AS_READ = "UPDATE notifications SET status = 1 WHERE id = ?";
    public void markNotificationAsRead(int notificationId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(MARK_NOTIFICATION_AS_READ);
        preparedStatement.setInt(1, notificationId);
        preparedStatement.executeUpdate();
    }

    private final String ADD_NOTIFICATION = "INSERT INTO notifications (user_id, type, message) VALUES (?, ?, ?)";
    public void addNotification(ArrayList<Integer> userIds, NotificationType type, String message) throws SQLException {
        for(int userId : userIds){
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_NOTIFICATION);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, type.getValue());
            preparedStatement.setString(3, message);
            preparedStatement.executeUpdate();
        }
    }

}
