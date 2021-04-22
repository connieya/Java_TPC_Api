package kr.javatpc;

import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MqttClass implements MqttCallback {

    private MqttClient client = null;

    public MqttClass() {
        new Thread(task1).start();
    }

    private ReceiveEventListener listener = null;

    Runnable task1 = new Runnable() {
        @Override
        public void run() {
            try {
                String clientId = UUID.randomUUID().toString();
                // new MqttClient()
                client = new MqttClient("tcp://localhost", clientId);
                // clientId를 랜덤으로 만듬
                MqttConnectOptions connopt = new MqttConnectOptions();
                connopt.setCleanSession(true);
                // 기존에 연결 되어 있는것이 있으면 다 끊고 새로 연결해라~
                client.connect(connopt);
                client.setCallback(MqttClass.this);
                //  콜백 함수 => messageArrived() 실행
                client.subscribe("dht11");
                // 수신자 등록

                new IotFrame(MqttClass.this);
                //  GUI 를 띄우는 부분
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    };
    public void sendMessage(String payload){
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        try {
            if (client.isConnected()){
                client.publish("led",message);
            }
        } catch (Exception e) {
            System.out.println("error1"+ e.getStackTrace());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // mqtt 서버가 끊어지면 실행되는 메서드이다.
        try {
            System.out.println("disconnect");
            client.close();
        } catch (Exception e) {
            System.out.println("error"+e.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        listener.recvMsg(topic,message);

    }
    public void setMyEventListener(ReceiveEventListener listener){
        // GUI 객체를 받는다.
        this.listener = listener;
//        MqttClass 입장에서 Iot 객체가 필요하다.
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
