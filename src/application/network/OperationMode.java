package application.network;

public enum OperationMode {
	Train("Netzwerk trainieren"), Test("Netzwerk testen");

	private String description = "";

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private OperationMode(String description) {
		this.setDescription(description);
	}

	@Override
	public String toString() {
		return this.description;
	}
}
