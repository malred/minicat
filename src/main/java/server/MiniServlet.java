package server;

import java.nio.charset.StandardCharsets;

public class MiniServlet extends HttpServlet{

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destory() throws Exception {

    }

    @Override
    public void doPost(Request request, Response response) {
        String content="<h1>servlet post</h1>";
        try {
            response.output(
                    HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(Request request, Response response) {
        String content="<h1>servlet get</h1>";
        try {
            response.output(
                    HttpProtocolUtil.getHttpHeader200(content.getBytes().length)+content
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
