package cn.blingfeng.netty.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int length;

    FixedLengthFrameDecoder(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length need > 0");
        }
        this.length = length;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= length) {
            ByteBuf byteBuf = in.readBytes(length);
            out.add(byteBuf);
        }
    }
}
