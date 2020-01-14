package com.studyNetty.task1;

import com.firstapp.MySpringBootApp;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * 注解作用是标记一个handler实例可以被多个channel共享
 *
 * serverhandler负责服务端业务逻辑
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    Logger logger=LoggerFactory.getLogger(MySpringBootApp.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        logger.info("before {}",msg);
//        super.channelRead(ctx, msg);//这句话究竟干了什么？有他就会bytebuf报错
        logger.info("after {}",msg);


//        ByteBuffer in=(ByteBuffer) msg;
        //这两个buffer在不同的包中
        ByteBuf in=(ByteBuf) msg;
        System.out.println("server received    "+in.toString(CharsetUtil.UTF_8));
        //将消息写给发送者不冲刷出站消息？
        ctx.write(in);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        //冲刷消息并关闭节点
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
