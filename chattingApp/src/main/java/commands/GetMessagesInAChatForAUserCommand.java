package commands;

import database.DBHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class GetMessagesInAChatForAUserCommand implements Command{
    DBHandler dbHandler;
    String userNumber;
    int chatId;

    /**
     * Constructor
     *
     * @param dbHandler
     * @param userNumber
     * @param chatId
     */
    public GetMessagesInAChatForAUserCommand(DBHandler dbHandler, String userNumber, int chatId) {
        super();
        this.dbHandler = dbHandler;
        this.userNumber = userNumber;
        this.chatId = chatId;
    }


    /**
     * Execute the get messages in a chat command
     * Check first that this usernumber is a participator in this chat and get the messages
     *
     * @return Result Set
     * @throws SQLException
     */
    public JSONObject execute() {


        String get_messages_in_a_chat = "SELECT get_messages_in_a_chat(" + "'" + userNumber + "'" + ", " + "'" + chatId + "'" + ");";
        return this.dbHandler.executeSQLQuery(get_messages_in_a_chat);
    }
}
