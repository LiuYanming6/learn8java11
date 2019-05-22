package nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author: 刘艳明
 * @Date: 19-5-22 上午8:44
 * <p>
 * <p>
 * java.nio.channels.
 *  -FileChannel
 *
 *  -SocketChannel          tcp
 *  -ServerSocketChannel    tcp
 *
 *  -DatagramChannel        udp
 *
 *  -Pipe.SinkChannel
 *  -Pipe.sourceChannel
 * 获取方式
 * Files.newByteChannel()
 */
public class TestChannel {

    /*
    本地IO, 利用通道完成文件的复制
     */
    @Test
    public void test1() {
        FileChannel inChannel = null, outChannel = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream("pom.xml");
            fos = new FileOutputStream("../pom.xml.txt");

            //获取通道
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();

            /* 分配指定大小的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (inChannel.read(buffer) != -1){
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }

             */

            inChannel.transferTo(0, fis.available(), outChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outChannel.close();
                inChannel.close();
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
    本地IO, 利用直接缓冲区完成文件的复制(内存映射文件), 效率很高
     */
    @Test
    public void test2() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("pom.xml"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("../pom.xml.txt"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.READ,
                    StandardOpenOption.WRITE);
/*
            //和 allocateDirect() 类似,只是获取方式不同
            MappedByteBuffer inBuffer = inChannel.map(READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outBuffer = outChannel.map(READ_WRITE, 0, inChannel.size());

            //直接对缓冲区进行数据的读写操作
            byte[] dst = new byte[inBuffer.limit()];
            inBuffer.get(dst);
            outBuffer.put(dst);//已经写入文件
*/
            /** 在 transferTo 中, 所以推荐使用 transferTo, 代码少,效率高
             This method is potentially much more efficient than a simple loop
             * that reads from this channel and writes to the target channel.  Many
             * operating systems can transfer bytes directly from the filesystem cache
             * to the target channel without actually copying them.
             */
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inChannel.close();
                outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
