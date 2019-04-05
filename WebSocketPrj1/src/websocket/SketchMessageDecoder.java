package websocket;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class SketchMessageDecoder implements Decoder.Text<SketchMessage> {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SketchMessage decode(String message) throws DecodeException {
		SketchMessage sketchMessage = new SketchMessage();
		JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
		sketchMessage.setX(jsonObject.getJsonNumber("x").doubleValue());
		sketchMessage.setY(jsonObject.getJsonNumber("y").doubleValue());
		sketchMessage.setSize(jsonObject.getJsonNumber("size").doubleValue());
		sketchMessage.setColor(jsonObject.getJsonString("color").getString());
		return sketchMessage;
	}

	@Override
	public boolean willDecode(String message) {
		boolean flag = true;
		try 
		{
			Json.createReader(new StringReader(message)).readObject();
		} catch (Exception e) 
		{
			flag = false;
		}
		return flag;
	}

}
