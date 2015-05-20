package org.bigbluebutton.conference.service.messaging;

import java.util.HashMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserLeftMessage implements IMessage {
	public static final String USER_LEFT = "user_leaving_request";
	public final String VERSION = "0.0.1";

	public final String meetingId;
	public final String userId;

	public UserLeftMessage(String meetingID, String internalUserId) {
		this.meetingId = meetingID;
		this.userId = internalUserId;
	}

	public String toJson() {
		HashMap<String, Object> payload = new HashMap<String, Object>();
		payload.put(Constants.MEETING_ID, meetingId);
		payload.put(Constants.USER_ID, userId);

		java.util.HashMap<String, Object> header = MessageBuilder.buildHeader(USER_LEFT, VERSION, null);

		return MessageBuilder.buildJson(header, payload);				
	}
	public static UserLeftMessage fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);

		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");

			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (USER_LEFT.equals(messageName)) {
					if (payload.has(Constants.MEETING_ID)
							&& payload.has(Constants.USER_ID)) {
						String meetingID = payload.get(Constants.MEETING_ID).getAsString();
						String userid = payload.get(Constants.USER_ID).getAsString();
						return new UserLeftMessage(meetingID, userid);
					}
				}
			}
		}
		System.out.println("Failed to parse RegisterUserMessage");
		return null;
	}
}
