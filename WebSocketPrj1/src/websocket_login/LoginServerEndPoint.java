package websocket_login;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

@ServerEndpoint(value = "/loginServerEndPoint")
public class LoginServerEndPoint {
	static Set<Session> sketchUsers = Collections.synchronizedSet(new HashSet<Session>());
	private DB dB;
	private DBCollection dBCollection;
	@OnOpen
	public void handleOpen(Session userSession) throws UnknownHostException {
		sketchUsers.add(userSession);
		dB = (new MongoClient("localhost",27017)).getDB("websocketdb");
		dBCollection = dB.getCollection("user");
	}
	@OnMessage
	public String handleMessage(String loginInfo) throws IOException, EncodeException {
		String result = "NG";
		String[] info = loginInfo.split("/");
		String username = info[0];
		String password = info[1];
		String islogin = info[2];

		BasicDBObject basicDbObject = new BasicDBObject();
		DBCursor dbCursor;
		
		if (islogin.equals("login"))
		{ 
			basicDbObject.put("username", username);
			basicDbObject.put("password", password);
			dbCursor = dBCollection.find(basicDbObject);
			if (dbCursor.count() > 0) result = username;
		}
		else if (islogin.equals("create"))
		{
			//check if username already taken
			basicDbObject.put("username", username);
			dbCursor = dBCollection.find(basicDbObject);
			if (dbCursor.count() > 0) 
			{
				result = "taken";
			}
			else 
			{
				//if no username taken then add password and insert username/password into 'user' collection
				basicDbObject.put("password", password);
				dBCollection.insert(basicDbObject);
				result = username;
			}
			
		}
		System.out.println(result);
		
		return result;
	}
	@OnClose
	public void handleClose(Session userSession) {
		sketchUsers.remove(userSession);
	}
	@OnError
	public void handleError(Throwable t) {
		System.out.println(t);
	}
	
}
