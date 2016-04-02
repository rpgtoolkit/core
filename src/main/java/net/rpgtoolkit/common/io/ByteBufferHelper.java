package net.rpgtoolkit.common.io;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public final class ByteBufferHelper {
  public static final Charset LEGACY_CHARSET = Charset.forName("US-ASCII");

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
   * Converts a String to bytes using the legacy character set and returns a new buffer containing
   * the String's bytes, null-terminated, with its position at 0, ready to write.
   *
   * @param s a String to create a null-terminated buffer for
   * @return a new buffer with the String's bytes
   */
  public static ByteBuffer getBuffer(String s) {
    s += '\0';
    return ByteBuffer.wrap(s.getBytes(LEGACY_CHARSET));
  }

}
