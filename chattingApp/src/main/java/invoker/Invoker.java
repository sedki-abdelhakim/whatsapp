package invoker;

import com.google.gson.JsonObject;
import com.mongodb.DB;
import commands.Command;
import config.ApplicationProperties;
import database.DBBroker;
import org.json.JSONObject;
import database.MongoDBConnection;
import database.PostgreSqlDBConnection;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Invoker {
    protected Hashtable htblCommands;
    protected ExecutorService threadPoolCmds;
    protected PostgreSqlDBConnection postgresqlDBConnection;
    protected DB mongoDBConnection;

    public Invoker() throws Exception {
        this.init();
    }

    public String invoke(String cmdName, JsonObject request) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ExecutionException, InterruptedException {
        Command cmd;
        Class<?> cmdClass = (Class<?>) htblCommands.get(cmdName);
        Constructor constructor = cmdClass.getConstructor(DBBroker.class, JsonObject.class);
        Object cmdInstance = constructor.newInstance(new DBBroker(postgresqlDBConnection, mongoDBConnection), request);
        cmd = (Command) cmdInstance;
        Future<JSONObject> result = threadPoolCmds.submit(cmd);
        return result.get().toString();
    }

    protected void loadCommands() throws Exception {
        htblCommands = new Hashtable();
        Properties prop = new Properties();
        InputStream in = ApplicationProperties.class.getResourceAsStream("commands.properties");
        prop.load(in);
        in.close();
        Enumeration enumKeys = prop.propertyNames();
        String strActionName, strClassName;

        while (enumKeys.hasMoreElements()) {
            strActionName = (String) enumKeys.nextElement();
            strClassName = (String) prop.get(strActionName);
//            C:\Users\welcome\Desktop\whatsapp\chattingApp\src\main\java\commands\AddAdminsToAGroupChatCommand.java
            Class<?> innerClass = Class.forName("commands." + strClassName);
            htblCommands.put(strActionName, innerClass);
        }
    }

    protected void loadThreadPool() {
        threadPoolCmds = Executors.newFixedThreadPool(40);
    }

    public void init() throws Exception {
        loadThreadPool();
        loadCommands();
        System.out.println(htblCommands);
//        postgresqlDBConnection = new PostgreSqlDBConnection();
        mongoDBConnection = new MongoDBConnection().connect();
    }
}
