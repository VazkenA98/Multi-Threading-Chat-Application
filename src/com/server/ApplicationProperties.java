package com.server;

import java.io.*;
import java.util.*;

public class ApplicationProperties {

    public static Map<String, Boolean> roomsMap = new HashMap<>();

    public static Map<String, Boolean> getRoomsMap() {
        String roomFlagsString = getCustomRoomsFlags();
        String roomNamesString = getCustomRoomsNames();
        if (!roomFlagsString.equals("") || !roomNamesString.equals("")) {
            List<String> roomFlags = Arrays.asList(roomFlagsString.split("-"));
            List<String> roomNames = Arrays.asList(roomNamesString.split("-"));
            for (int i = 0; i < roomFlags.size(); i++) {
                if (!roomNames.get(i).equals("")) {
                    roomsMap.put(roomNames.get(i), Boolean.valueOf(roomFlags.get(i)));
                }
            }
        }
        return roomsMap;
    }

    public static String getCustomRoomsFlags() {
        Properties properties = new Properties();
        try (InputStream resourceStream = new FileInputStream("src/resources/application.properties")) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("com.custom.rooms.flags");
    }

    public static String getCustomRoomsNames() {
        Properties properties = new Properties();
        try (InputStream resourceStream = new FileInputStream("src/resources/application.properties")) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("com.custom.rooms.names");
    }

    public static void addRoomNameAndFlag(String name) {
        Properties properties = new Properties();
        String sufName = getCustomRoomsNames() == null ? "" : getCustomRoomsNames();
        String sufFlag = getCustomRoomsFlags() == null ? "" : getCustomRoomsFlags();
        try (OutputStream outputStream = new FileOutputStream("src/resources/application.properties")) {
            properties.setProperty("com.custom.rooms.names", sufName + name + "-");
            properties.setProperty("com.custom.rooms.flags", sufFlag + "true" + "-");
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeRoom(String name) {
        String sufName = getCustomRoomsNames() == null ? "" : getCustomRoomsNames().replace(name + "-", "");
        String sufFlag = getCustomRoomsFlags() == null ? "" : getCustomRoomsFlags().replaceFirst("true-", "");
        Properties OutputStream = new Properties();
        try (OutputStream outputStream = new FileOutputStream("src/resources/application.properties")) {
            OutputStream.setProperty("com.custom.rooms.names", sufName);
            OutputStream.setProperty("com.custom.rooms.flags", sufFlag);
            OutputStream.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
