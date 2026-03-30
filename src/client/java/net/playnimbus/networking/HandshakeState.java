package net.playnimbus.networking;

public enum HandshakeState {
	JOINED((byte) 0),
	SUCCESS((byte) 1);

	private final byte state;

	HandshakeState(byte state) {
		this.state = state;
	}

	public byte getState() {
		return state;
	}

	public static HandshakeState getFromState(byte state) throws IllegalArgumentException {
		for (HandshakeState s : HandshakeState.values()) {
			if (s.getState() == state) {
				return s;
			}
		}

		throw new IllegalArgumentException("Invalid handshake state");
	}
}
