package LazyLoaderLibs;

import java.io.InputStream;
import java.io.OutputStream;

public class LoaderUtils {

	/**
	 * @author Wild Coder
	 * @param inputStream
	 *            InputStream
	 * @param outStream
	 *            OutputStream
	 */
	public static void copyStream(InputStream inputStream,
			OutputStream outStream) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = inputStream.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				outStream.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}