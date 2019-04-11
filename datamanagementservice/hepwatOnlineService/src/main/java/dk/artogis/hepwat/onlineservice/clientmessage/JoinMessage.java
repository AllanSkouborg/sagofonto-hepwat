package dk.artogis.hepwat.onlineservice.clientmessage;

public class JoinMessage extends Message {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
