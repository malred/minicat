package server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StaticResourceUtil {
    /**
     * 获取静态资源文件绝对路径
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path){
        //resources
        String absolutPath=StaticResourceUtil.class.getResource("/").getPath();
        System.out.println(absolutPath);
        return absolutPath.replace("\\\\","/")+path;
    }

    /**
     * 读取静态文件输入流,通过输出流输出
     * @param fileInputStream 文件输入流
     * @param outputStream 输出流
     */
    public static void outputStaticResource(FileInputStream fileInputStream,
                                            OutputStream outputStream) throws Exception {
        int count=0;
        while (count==0){
            count= fileInputStream.available();;
        }
        //静态资源输入流长度
        int resourceSize=count;
        //输出http请求头,然后再输出具体内容
        outputStream.write(HttpProtocolUtil.getHttpHeader200(resourceSize).getBytes());
        //读取内容输出
        long written=0;//已经读取的内容长度
        int byteSize=1024;//计划缓冲的长度
        byte[] bytes=new byte[byteSize];
        while (written<resourceSize){
            //判断是否可以一次性写完(剩余文件大小<1024),则按剩余文件大小来处理
            if(written+byteSize>resourceSize){
                byteSize= (int) (resourceSize-written);//剩余的文件长度
                bytes=new byte[byteSize];
            }
            fileInputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();//刷新
            written+=byteSize;//更新已读取长度
        }
    }
}
