package nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * @Author: 刘艳明
 * @Date: 19-5-23 上午9:08
 */
public class TestNonBlockUDP {

    @Test
    public void send() throws IOException{
        DatagramChannel dc = DatagramChannel.open();

        dc.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String str = scanner.next();
            buffer.put((LocalDate.now().toString() + ": " + str).getBytes());
            buffer.flip();
            dc.send(buffer, new InetSocketAddress("127.0.0.1", 8899));
            buffer.clear();
        }

        dc.close();
    }

    @Test
    public void receive() throws IOException{
        DatagramChannel dc = DatagramChannel.open();
        dc.configureBlocking(false);
        dc.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        dc.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            selector.keys().forEach(a -> {
                if (a.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    try {
                        ((DatagramChannel)a.channel()).receive(buffer);
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, buffer.limit()));
                        buffer.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
