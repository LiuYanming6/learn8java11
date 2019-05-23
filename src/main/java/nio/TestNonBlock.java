package nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * @Author: 刘艳明
 * @Date: 19-5-22 下午8:24
 */
public class TestNonBlock {
    @Test
    public void client() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8899));

        //切换到非阻塞
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put(LocalDateTime.now().toString().getBytes());
        buffer.flip();

        socketChannel.write(buffer);
        buffer.clear();

        socketChannel.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.bind(new InetSocketAddress(8899));

        //获取选择器
        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (selector.select() > 0) {
            Iterator<SelectionKey> iter = selector.keys().iterator();
            while (iter.hasNext()) {
                SelectionKey sk = iter.next();
                if (sk.isAcceptable()) {
                    System.out.println("accept");
                    SocketChannel client = serverSocketChannel.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    System.out.println("read");
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    SocketChannel client = (SocketChannel) sk.channel();
                    int len = client.read(buffer);
                    buffer.flip();
                    System.out.println(new String(buffer.array(), 0, len));
                }

//                iter.remove();
                sk.cancel();
            }
        }
    }
}
