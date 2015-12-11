package net.rpgtoolkit.common.io;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author Chris Hutchinson <chris@cshutchinson.com>
 */
public final class ByteBufferHelper {

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

}
