package commands;

import com.google.gson.JsonObject;
import database.DBHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;
import sender.MqttSender;

public class StartAChatCommand implements Command, Runnable {

    DBHandler dbHandler;
    String firstUserNumber;
    String secondUerNumber;

    /**
     * Constructor
     *
     * @param dbHandler
     * @param firstUserNumber
     * @param secondUerNumber
     */

    public StartAChatCommand(DBHandler dbHandler, String firstUserNumber, String secondUerNumber) {
        this.dbHandler = dbHandler;
        this.firstUserNumber = firstUserNumber;
        this.secondUerNumber = secondUerNumber;
    }

    /**
     * Start a chat between two users, insert into the chats table 2 entities
     *
     * @return Result Set
     * @throws SQLException
     */
    public JSONObject execute() {
        String start_chat = "SELECT start_chat(" + "'" +firstUserNumber + "'" + ", " + "'" + secondUerNumber + "'" + ");";
        return this.dbHandler.executeSQLQuery(start_chat);
    }

    public void run() {
        JSONObject res = this.execute();
        try {
            MqttSender sender = new MqttSender();
            sender.send(res);
            sender.close();
        } catch (Exception e) {

        }
    }
}

