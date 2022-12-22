package server;

/**
 * http协议工具类,主要是提供响应头信息
 */
public class HttpProtocolUtil {
    /**
     * 为响应码200提供响应头信息
     * @param contentLength 响应体内容的长度
     * @return
     */
    public static String getHttpHeader200(long contentLength){
        return "HTTP/1.1 200 OK \n"+
                "Content-Type: text/html \n"+
                "Content-Length: "+ contentLength+"\n"+
                "\r\n"; //需要回车换行来间隔 头,体
    }
    /**
     * 为响应码404提供响应头信息
     * @return
     */
    public static String getHttpHeader404(){
        String str404="<h1>404 not found</h1>";
        return "HTTP/1.1 404 Not Found \n"+
                "Content-Type: text/html \n"+
                "Content-Length: "+str404.getBytes().length+"\n"+
                "\r\n"+str404; //需要回车换行来间隔 头,体
    }
}
