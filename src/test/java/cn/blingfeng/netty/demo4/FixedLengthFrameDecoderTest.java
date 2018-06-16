package cn.blingfeng.netty.demo4;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;


public class FixedLengthFrameDecoderTest {

    @Test
    public void testDecode() {
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            byteBuf.writeByte(i);
        }
        ByteBuf duplicate = byteBuf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertTrue(channel.writeInbound(duplicate.retain()));
        assertTrue(channel.finish());
        ByteBuf read = channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertNull(read);
    }

    @Test
    public void testDecode2() {
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            byteBuf.writeByte(i);
        }
        ByteBuf duplicate = byteBuf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertFalse(channel.writeInbound(duplicate.readBytes(2)));
        assertTrue(channel.writeInbound(duplicate.readBytes(7)));
        assertTrue(channel.finish());
        ByteBuf read = channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        assertEquals(byteBuf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertNull(read);
    }
}
