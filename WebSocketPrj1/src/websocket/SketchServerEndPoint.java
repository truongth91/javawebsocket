package websocket;

import java.util.Set;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/sketchServerEndPoint1", encoders = {SketchMessageEncoder.class}, decoders = {SketchMessageDecoder.class})
public class SketchServerEndPoint {
	static Set<Session> sketchUsers = Collections.synchronizedSet(new HashSet<Session>());
	@OnOpen
	public void handleOpen(Session userSession) {
		sketchUsers.add(userSession);
	}
	@OnMessage
	public void handleMessage(SketchMessage incomingSketchMessage, Session userSession) throws IOException, EncodeException {
		SketchMessage outgoingSketchMessage = new SketchMessage();
		
		outgoingSketchMessage.setX(incomingSketchMessage.getX());
		outgoingSketchMessage.setY(incomingSketchMessage.getY());
		outgoingSketchMessage.setSize(incomingSketchMessage.getSize());
		outgoingSketchMessage.setColor(incomingSketchMessage.getColor());
		Iterator<Session> iterator = sketchUsers.iterator();
		Session tempSession = null;
		while (iterator.hasNext()) {
			tempSession = iterator.next();
			if (!tempSession.equals(userSession)) {
				tempSession.getBasicRemote().sendObject(outgoingSketchMessage);
			}
		}
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
