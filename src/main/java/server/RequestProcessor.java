package server;

import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
public class RequestProcessor extends Thread{
    private Socket socket;
    private Map<String,HttpServlet> servletMap;

    public RequestProcessor(Socket socket, Map<String, HttpServlet> servletMap) {
        this.socket=socket;
        this.servletMap=servletMap;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream=socket.getInputStream();
            //封装request和response对象
            Request request=new Request(inputStream);
            Response response=new Response(socket.getOutputStream());
            //根据request的url到map里找是否有对应的静态网页
            if(servletMap.get(request.getUrl())==null){
                //没在map里,表示是静态资源请求
                response.outputHtml(request.getUrl());
            }else {
                //在map里,动态资源servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request,response);
            }
            response.outputHtml(request.getUrl());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
