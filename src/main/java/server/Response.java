package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 封装Response对象,需要依赖于outputStream
 *
 * 改对象提供核心方法,输出(静态资源: html)
 */
public class Response {
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Response() {
    }

    public void output(String content)throws Exception{
        outputStream.write(content.getBytes());
    }

    /**
     * 根据请求路径获取静态资源绝对路径,然后返回静态资源
     * @param path 请求路径  / 指向 classes目录(编译后生成)
     * @throws Exception
     */
    public void outputHtml(String path)throws Exception{
        //获取静态资源文件的绝对路径
        String absoluteResourcePath=StaticResourceUtil.getAbsolutePath(path);
        //输出静态资源文件
        File file=new File(absoluteResourcePath);
        //是否存在
        if(file.exists()&&file.isFile()){
            //读取静态资源,返回静态资源
            StaticResourceUtil.outputStaticResource(new FileInputStream(file),outputStream);
        }else{
            //输出404
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }
}
