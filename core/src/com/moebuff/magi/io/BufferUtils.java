package com.moebuff.magi.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Some often-used Buffer code for creating native buffers of the appropriate size.
 *
 * @author muto
 */
public class BufferUtils {
    /**
     * Construct a direct native-ordered bytebuffer with the specified size.
     *
     * @param size The size, in bytes
     * @return a ByteBuffer
     */
    public static ByteBuffer createByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }
}
