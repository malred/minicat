package server;

import java.io.IOException;
import java.io.InputStream;

/**
 * 把请求信息封装为Request对象(根据InputStream输入流封装)
 */
public class Request {
    private String method;//请求方式,GET/POST...
    private String url;//比如,/ /user
    private InputStream inputStream;//输入流,其他属性从输入流解析出来

    public Request() {
    }
    //传入输入流,构造
    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        //从输入流中获取请求信息
        //使用bio模式
        //获取输入流的数据长度
        int count=0;
        while (count==0){
            count=inputStream.available();
        }
        byte[] bytes=new byte[count];
        inputStream.read(bytes);
        String inputStr=new String(bytes);
        //获取第一行请求头信息
        String firstLine=inputStr.split("\\n")[0];//GET / HTTP/1.1
        String[] s = firstLine.split(" ");
        this.method=s[0];
        this.url=s[1];
        System.out.println("请求方式: "+method);
        System.out.println("请求地址: "+url);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
