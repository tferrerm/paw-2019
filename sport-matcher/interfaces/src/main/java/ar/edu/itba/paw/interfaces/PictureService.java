package ar.edu.itba.paw.interfaces;

import java.io.IOException;

public interface PictureService {
	
	/**
	 * Converts a picture, ensuring its size is smaller than 1000x1000
	 * @param picture Picture raw data
	 * @return Converted picture
	 * @throws IOException
	 */
	public byte[] convert(byte[] picture) throws IOException;
	
	/**
	 * Converts a picture, ensuring its size is smaller than maxWidth x maxHeight
	 * @param picture Picture raw data
	 * @param maxWidth Horizontal resolution
	 * @param maxHeight Vertical resolution
	 * @return Converted picture
	 * @throws IOException If an error occurs handling the picture's bytes
	 */
	public byte[] convert(byte[] picture, int maxWidth, int maxHeight) throws IOException;

}
