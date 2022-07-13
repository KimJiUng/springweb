package ezenweb.config;

import ezenweb.service.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class MsgWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private MemberService memberService;

    // 접속된 세션의 리스트 [세션,mid]
    private Map<WebSocketSession, String> list = new HashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 경로에서 아이디 추출
        String path = session.getUri().getPath();
        String mid = path.substring(path.lastIndexOf("/")+1);
        // 세션과 아이디 같이 저장
        list.put(session,mid);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        list.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject object = new JSONObject(message.getPayload());   // Payload() : 메세지 내용

        // DB 처리
        memberService.messagesend(object);
        // 현재 접속된 세션들중에 받는사람(to)가 있으면 메세지 전송
        for(WebSocketSession socketSession : list.keySet() ){
            if(list.get(socketSession).equals(object.get("to"))){
                socketSession.sendMessage(message);
            }
        }
    }
}
