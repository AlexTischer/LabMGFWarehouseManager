package it.polimi.geodesicwarehousemanager.controllers.superUsers;

import com.google.gson.Gson;
import it.polimi.geodesicwarehousemanager.beans.DocumentBean;
import it.polimi.geodesicwarehousemanager.daos.ItemDAO;
import it.polimi.geodesicwarehousemanager.daos.RequestDAO;
import it.polimi.geodesicwarehousemanager.enums.ItemType;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import it.polimi.geodesicwarehousemanager.utils.ConnectionHandler;


@MultipartConfig
@WebServlet(name = "LinkItems", value = "/SuperUser/LinkItems")
public class LinkItems extends HttpServlet {

    private Connection connection = null;

    public void init() {
        try {
            connection = ConnectionHandler.getConnection(getServletContext());
        } catch (UnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id;
        int type;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            type = Integer.parseInt(request.getParameter("type"));
            if (type < 0 || type > 3 || id < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
            return;
        }
        ItemDAO itemDAO = new ItemDAO(connection);
        Map<String, ArrayList<Object>> links = null;
        try {
            switch (type) {
                case 0:
                    links = itemDAO.getToolLinks(id);
                    break;
                case 1:
                    links = itemDAO.getAccessoryLinks(id);
                    break;
                case 2:
                    links = itemDAO.getDocumentLinks(id);
                    break;
                case 3:
                    ArrayList<DocumentBean> documents = new ArrayList<>();
                    RequestDAO requestDAO = new RequestDAO(connection);
                    for(int item : requestDAO.getItemsIdByRequestId(id)){
                        ArrayList<DocumentBean> doc = itemDAO.getDocumentsByItemId(item);
                        if(doc != null) {
                            documents.addAll(doc);
                        }
                    }
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    Gson gson = new Gson();
                    String json = gson.toJson(documents);
                    response.getWriter().println(json);
                    return;
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(links);
        response.getWriter().println(json);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> linksMap;
        int id;
        int type;
        try {
            linksMap = new Gson().fromJson(request.getReader(), Map.class);
            id = (int) Double.parseDouble(linksMap.get("id").toString());
            type = (int) Double.parseDouble(linksMap.get("type").toString());
            if (ItemType.getItemTypeFromInt(type) == null) {
                System.out.println("Invalid type");
                throw new ClassCastException("Invalid type");
            } else if (id < 0) {
                System.out.println("Invalid id");
                throw new ClassCastException("Invalid id");
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid userId");
            e.printStackTrace();
            return;
        } catch (ClassCastException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid type");
            e.printStackTrace();
            return;
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            return;
        }
        ArrayList<Integer> toolLinks = new ArrayList<>();
        ArrayList<Integer> accessoryLinks = new ArrayList<>();
        ArrayList<Integer> documentLinks = new ArrayList<>();

        try{
            if(linksMap.get("tools") != null) {
                for (Object s : (ArrayList) linksMap.get("tools")) {
                    toolLinks.add((int) Double.parseDouble(s.toString()));
                }
            }
            if(linksMap.get("accessories") != null){
                for (Object s : (ArrayList) linksMap.get("accessories")) {
                    accessoryLinks.add((int) Double.parseDouble(s.toString()));
                }
            }
            if (linksMap.get("documents") != null) {
                for (Object s : (ArrayList) linksMap.get("documents")) {
                    documentLinks.add((int) Double.parseDouble(s.toString()));
                }
            }

            System.out.println("Tools: " + toolLinks);
            System.out.println("Accessories: " + accessoryLinks);
            System.out.println("Documents: " + documentLinks);


        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid links");
            e.printStackTrace();
            return;
        }

        ItemDAO itemDAO = new ItemDAO(connection);
        try {
            switch (type) {
                case 0:
                    itemDAO.linkItem(id, type, accessoryLinks, documentLinks);
                    break;
                case 1:
                    itemDAO.linkItem(id, type, toolLinks, documentLinks);
                    break;
                case 2:
                    ArrayList<Integer> items = new ArrayList<>();
                    items.addAll(toolLinks);
                    items.addAll(accessoryLinks);
                    itemDAO.linkDocument(id, items);
                    break;
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }

    }

    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}