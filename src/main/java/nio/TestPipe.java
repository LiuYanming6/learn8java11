package nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @Author: 刘艳明
 * @Date: 19-5-23 上午11:27
 */
public class TestPipe {

    @Test
    public void test() throws IOException{
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sinkChannel = pipe.sink();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("Hello".getBytes());
        buffer.flip();
        sinkChannel.write(buffer);


        Pipe.SourceChannel sourceChannel = pipe.source();
        buffer.clear();
        int len = sourceChannel.read(buffer);
        System.out.println(new String(buffer.array(), 0, len));

        sourceChannel.close();
        sinkChannel.close();
    }
}
