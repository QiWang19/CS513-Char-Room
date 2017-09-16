package util;

import java.io.Closeable;
import java.io.IOException;
/**
 * A class can be repeatedly used to close the input and out stream of server and client socket.
 * @author QiWang
 *
 */
public class CloseUtil {
	public static void closeAll (Closeable... io) {
		for (Closeable temp: io) {
			
				try {
					if (temp != null) {
					temp.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
	}

}
