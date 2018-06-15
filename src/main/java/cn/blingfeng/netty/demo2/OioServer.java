package cn.blingfeng.netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class OioServer {
    private final int port = 28081;

    public static void main(String[] args) {
        new OioServer().start();
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup group = new OioEventLoopGroup(); // new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(OioServerSocketChannel.class) // NioServerSocketChannel.class
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        ctx.writeAndFlush(Unpooled
                                                .copiedBuffer("hello", CharsetUtil.UTF_8))
                                                .addListener(ChannelFutureListener.CLOSE);
                                        super.channelActive(ctx);
                                    }
                                });
                    }
                });
        try {
            ChannelFuture f = bootstrap.bind().sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
