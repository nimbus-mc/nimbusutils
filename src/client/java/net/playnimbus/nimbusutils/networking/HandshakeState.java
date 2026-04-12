package net.playnimbus.nimbusutils.networking;

public enum HandshakeState {
	NONE((byte) 0, "none"),
	CONNECTING((byte) 1, "connecting"),
	HUB((byte) 2, "hub"),
	NIMNITE((byte) 3, "nimnite"),
	;

	private final byte state;
	private final String name;

	HandshakeState(byte state, String name) {
		this.state = state;
		this.name = name;
	}

	public byte getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public static HandshakeState getFromState(byte state) throws IllegalArgumentException {
		for (HandshakeState s : HandshakeState.values()) {
			if (s.getState() == state) {
				return s;
			}
		}

		throw new IllegalArgumentException("Invalid handshake state");
	}

	public static HandshakeState getFromName(String name) throws IllegalArgumentException {
		for (HandshakeState s : HandshakeState.values()) {
			if (s.getName().equals(name)) {
				return s;
			}
		}

		throw new IllegalArgumentException("Invalid handshake name");
	}

	public boolean isInAGame() {
		switch (this) {
			case NIMNITE -> {
				return true;
			}
			default -> {
				return false;
			}
		}
	}
}