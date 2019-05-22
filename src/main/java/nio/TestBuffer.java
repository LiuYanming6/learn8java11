package nio;

import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @Author: 刘艳明
 * @Date: 19-5-22 上午7:43
 * <p>
 * capacity: 容量
 * limit:    缓冲区可操作数据大小
 * position: 正在操作位置
 * <p>
 * position <= limit <= capacity
 */

public class TestBuffer {

    public void printInfo(Buffer buffer){
        System.out.print(buffer.position() + "\t");//0
        System.out.print(buffer.limit() + "\t");//1024
        System.out.println(buffer.capacity() + "\t");//1024
    }

    @Test
    public void test1() {
        /* 1.
        allocate jvm缓冲区
        allocateDirect 直接缓冲区,放在物理内存中,提高效率
         */
//        System.gc();
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        printInfo(buffer);//0,1024,1024

        // 2. 写
        buffer.put("abcd".getBytes());
        printInfo(buffer);//4,1024,1024

        // 3. 读
        buffer.flip();
        printInfo(buffer);//0,4,1024

        byte b = buffer.get();
        printInfo(buffer);//1	4	1024
        buffer.mark();//mark-reset

        byte dst[] = new byte[buffer.limit() - 1];
        buffer.get(dst, 0, 3);
//        buffer.get(dst);
        printInfo(buffer);//4	4	1024

        System.out.println("msg" + (char)b + new String(dst));

        // 4. mark-reset
        buffer.reset();
        printInfo(buffer);//1	4	1024

        if (buffer.hasRemaining()) {
            System.out.println(buffer.remaining()); //还有3个未读
        }

        // 5. 重新读
        buffer.rewind();
        printInfo(buffer);//0,4,1024

        // 6. 只是改变了指针
        buffer.clear();
        printInfo(buffer);//0	1024	1024
    }
}
