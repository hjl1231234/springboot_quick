package com.studyNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 */
public class EchoServer {
    public EchoServer(int port) {
        this.port = port;
    }

    private final int port;


    public void start() {
        EchoServerHandler serverHandler = new EchoServerHandler();
        //创建eventloopergroup
        EventLoopGroup group = new NioEventLoopGroup();
        //创建serverbootstrap
        ServerBootstrap b = new ServerBootstrap();


        //制定nio传输channel
        //设置端口和socket
        b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //添加serverhandler到子channel的pipeline
                        socketChannel.pipeline().addLast(serverHandler);
                    }
                });

        try {
        //阻塞当前线程直到绑定完成
            ChannelFuture f=b.bind().sync();
            //阻塞当前线程直到channel的closefuture被获得
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //关闭eventloopgoup即可释放全部资源
//            try {
//                group.shutdownGracefully().sync();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }


    public static void main(String[] args) {
        //设置端口数值，不正确抛出异常
        if (args.length != 1) {
            System.err.println("usage:    " + EchoServer.class.getSimpleName() + "<port>");
            //这里少个个return吧？
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

}
