package commands;


import com.google.gson.JsonObject;
import database.DBBroker;

import java.sql.SQLException;

import org.json.JSONObject;

public class LeaveAGroupChatCommand implements Command {

    DBBroker dbBroker;
    String userNumber;
    String groupChatId;

    /**
     * Constructor
     *
     * @param dbBroker
     * @param request
     */

    public LeaveAGroupChatCommand(DBBroker dbBroker, JsonObject request) {
        this.dbBroker = dbBroker;
        this.userNumber = request.get("userNumber").getAsString();
        this.groupChatId = request.get("groupChatId").getAsString();
    }

    @Override
    public JSONObject call() throws Exception {
        String leave_group_chat = "SELECT start_chat(" + "'" + userNumber + "'" + ", " + "'" + groupChatId + "'" + ");";
        return this.dbBroker.executeSQLQuery(leave_group_chat);
    }
}

