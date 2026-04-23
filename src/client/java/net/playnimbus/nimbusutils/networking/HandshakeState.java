package net.playnimbus.nimbusutils.networking;

public enum HandshakeState {
	NONE((byte) 0),
	CONNECTING((byte) 1),
	CONNECTED((byte) 2)
	;

	private final byte state;

	HandshakeState(byte state) {
		this.state = state;
	}

	public static final HandshakeState[] VALUES = values();

	public byte getState() {
		return state;
	}

	public static HandshakeState getFromState(byte state) throws IllegalArgumentException {
		for (HandshakeState s : VALUES) {
			if (s.getState() == state) {
				return s;
			}
		}

		throw new IllegalArgumentException("Invalid handshake state");
	}

	public boolean isConnected() {
		return this == CONNECTED;
	}
}
