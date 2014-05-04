package com.relaxisapp.relaxis.utils;

public class CustomUtilities {

	public static int ByteToUnsignedInt(byte b) {
		return b & 0xFF;
	}

	/**
	 * @param byte1
	 *            is the least significant byte
	 * @param byte2
	 * @return Unsigned integer based on the two bytes.
	 */
	public static int TwoBytesToUnsignedInt(byte byte1, byte byte2) {
        // TODO test this
		return ((byte2 & 0xFF) << 8) + (byte1 & 0xFF);
	}
}
