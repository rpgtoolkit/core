package net.rpgtoolkit.common.io;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public final class ByteBufferHelper {
  public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  public static String getTerminatedString(final ByteBuffer buffer) {
    final StringBuilder builder = new StringBuilder();
    while (buffer.hasRemaining()) {
      final int ch = buffer.get();
      if (ch == '\0') {
        break;
      }
      builder.append((char) ch);
    }
    return builder.toString();
  }
  
  /**
   * Converts a String to bytes using the default character set and returns a new buffer containing
   * the String's bytes, with its position at 0, ready to write.
   *
   * @param s a String to create a buffer for
   * @return a new buffer with the String's bytes
   */
  public static ByteBuffer getBuffer(String s) {
    return ByteBuffer.wrap(s.getBytes(DEFAULT_CHARSET));
  }

}
