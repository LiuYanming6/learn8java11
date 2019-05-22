package nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.*;

/**
 * @Author: 刘艳明
 * @Date: 19-5-22 下午7:54
 *  * java.nio.channels.
 *  *  -FileChannel
 *  *
 *  *  -SocketChannel          tcp
 *  *  -ServerSocketChannel    tcp
 *  *
 *  *  -DatagramChannel        udp
 *  *
 *  *  -Pipe.SinkChannel
 *  *  -Pipe.sourceChannel
 */
public class TestBlockingNIO {

    @Test
    public void client() throws IOException{
        SocketChannel client = SocketChannel.open(
                new InetSocketAddress("127.0.0.1", 8899));

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        FileChannel fileChannel = FileChannel.open(Paths.get("pom.xml"), StandardOpenOption.READ);
        while (fileChannel.read(buffer) != -1){
            buffer.flip();
            client.write(buffer);
            buffer.clear();
        }

        //接收返回的消息
        int len = 0;
        System.out.println("等待消息");
        while ((len = client.read(buffer)) != -1){
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, len));
            buffer.clear();
        }

        fileChannel.close();
        client.close();
    }

    @Test
    public void server()  {
        String cpFile = "pom.xml.send";
        try {
            Files.delete(Paths.get(cpFile));
        }catch (NoSuchFileException e){
            System.out.println("no such file to delete");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //1. 获取通道
            ServerSocketChannel server = ServerSocketChannel.open();

            //2. 绑定连接
            server.bind(new InetSocketAddress(8899));

            //3. 获取客户端的通道
            SocketChannel client = server.accept();

            //4. 接收客户端的数据
            FileChannel fileChannel = FileChannel.open(Paths.get(cpFile), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (client.read(buffer) != -1){
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }

            buffer.put("我读完了,拜拜".getBytes());
            buffer.flip();
            client.write(buffer);

            client.close();
            server.close();
            fileChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
