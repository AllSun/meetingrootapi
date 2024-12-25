package com.sun.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MeetingRoomServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://121.37.136.16:3306/api_database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Sun19930808!";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        List<MeetingRoom> meetingRooms = getMeetingRooms();

        // 转换为 JSON 格式
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < meetingRooms.size(); i++) {
            MeetingRoom room = meetingRooms.get(i);
            json.append("{")
                .append("\"id\":").append(room.getId()).append(",")
                .append("\"status\":\"").append(room.getStatus()).append("\",")
                .append("\"meetingName\":\"").append(room.getMeetingName()).append("\",")
                .append("\"reserver\":\"").append(room.getReserver()).append("\"")
                .append("}");
            if (i < meetingRooms.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        out.print(json.toString());
        out.flush();
    }

    private List<MeetingRoom> getMeetingRooms() {
        List<MeetingRoom> meetingRooms = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT id, status, meetingName, reserver FROM meetingroom";
            System.out.println("数据库连接成功！");
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    MeetingRoom room = new MeetingRoom();
                    room.setId(rs.getInt("id"));
                    room.setStatus(rs.getString("status"));
                    room.setMeetingName(rs.getString("meetingName"));
                    room.setReserver(rs.getString("reserver"));
                    meetingRooms.add(room);
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(meetingRooms);
        return meetingRooms;
    }
}

// MeetingRoom 实体类
class MeetingRoom {
    private int id;
    private String status;
    private String meetingName;
    private String reserver;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMeetingName() {
        return meetingName;
    }
    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }
    public String getReserver() {
        return reserver;
    }
    public void setReserver(String reserver) {
        this.reserver = reserver;
    }
}
