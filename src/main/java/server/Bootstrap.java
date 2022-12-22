package server;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.print.Doc;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * minicat主类
 */
public class Bootstrap {
    /**
     * socket监听的端口号
     */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * minicat启动需要初始化展开的一些操作
     */
    public void start() throws Exception {
        //加载解析配置文件,web.xml
        loadServlet();
        int corePoolSize = 10;//初始线程池大小
        int maximumPoolSize = 50;//最大线程数
        long keepAliveTime = 100L;//最大保留时间
        TimeUnit unit = TimeUnit.SECONDS;//时间单位
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(50);//请求队列
        ThreadFactory threadFactory = Executors.defaultThreadFactory();//线程工厂
        RejectedExecutionHandler handle = new ThreadPoolExecutor.AbortPolicy();//拒绝策略
        //定义线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handle
        );
        /*
         * minicat v1.0,http://localhost:8080,返回固定字符串到页面
         */
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("minicat start on port: " + port);
//        while (true){
//            //阻塞监听,直到连接
//            Socket socket=serverSocket.accept();
//            //有了socket,接收到请求
//            OutputStream outputStream=socket.getOutputStream();
//            //响应信息
//            String data="Hello Mincat";
//            String responseText=
//                    HttpProtocolUtil.getHttpHeader200(data.getBytes().length)+data;
//            outputStream.write(responseText.getBytes());
//            socket.close();
//        }
        /*
         * 完成Minicat 2.0版本
         * 需求: 封装Request和Response对象,返回html静态资源文件
         */
//        while (true){
//            Socket socket=serverSocket.accept();
//            InputStream inputStream=socket.getInputStream();
//            //封装request和response对象
//            Request request=new Request(inputStream);
//            Response response=new Response(socket.getOutputStream());
//            response.outputHtml(request.getUrl());
//            socket.close();
//        }
        /**
         * Minicat3.0版本
         * 需求: 可以请求动态资源(servlet)
         */
//        while (true){
//            Socket socket=serverSocket.accept();
//            InputStream inputStream=socket.getInputStream();
//            //封装request和response对象
//            Request request=new Request(inputStream);
//            Response response=new Response(socket.getOutputStream());
//            //根据request的url到map里找是否有对应的静态网页
//            if(servletMap.get(request.getUrl())==null){
//                //没在map里,表示是静态资源请求
//                response.outputHtml(request.getUrl());
//            }else {
//                //在map里,动态资源servlet请求
//                HttpServlet httpServlet = servletMap.get(request.getUrl());
//                httpServlet.service(request,response);
//            }
//            response.outputHtml(request.getUrl());
//            socket.close();
//        }
        /**
         * Minicat4.0版本
         * 需求: 多线程改造
         */
//        while (true){
//            Socket socket=serverSocket.accept();
//            RequestProcessor requestProcessor =new RequestProcessor(socket,servletMap);
//            requestProcessor.start();
//        }
        /**
         * Minicat5.0版本
         * 需求: 线程池
         */
        while (true) {
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
//            requestProcessor.start();
            threadPoolExecutor.execute(requestProcessor);
        }
    }

    private Map<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 加载解析web.xml,初始化servlet
     */
    private void loadServlet() throws Exception {
        //加载配置文件
        InputStream resourceAsStream =
                this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            //获取根元素
            Element rootElement = document.getRootElement();
            //获取servlet标签list
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                //<servlet-name>malguy</servlet-name>
                Element servletNameEle = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameEle.getStringValue();
                //<servlet-class>server.MiniServlet</servlet-class>
                Element servletClassEle = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassEle.getStringValue();
                //根据servlet-name找到对应的mapping,找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode
                        ("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                //url-pattern
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * mincat 程序启动入口
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
