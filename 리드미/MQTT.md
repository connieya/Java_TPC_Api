
`MqttClass.java`

```java
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

    public void sendMessage(String payload) {
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        try {
            if (client.isConnected()) {
                client.publish("led", message);
            }
        } catch (Exception e) {
            System.out.println("error1" + e.getStackTrace());
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        try {
            System.out.println("disconnect");
            client.close();
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        listener.recvMsg(topic, message);

    }

    public void setMyEventListener(ReceiveEventListener listener) {
        // GUI 객체를 받는다.
        this.listener = listener;

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
```

MqttClass 에서 메세지를 수신했다. 수신된 값을 IotFrame(GUI) 에 넘겨줘야한다.

그래서 MqttClass에서 setMyEventListner라는 메소드를 생성한 뒤 거기에 IotFrame 객체에

자기 객체를 넣어 넘겨 준다.


`IotFrame.java`

```java
public class IotFrame extends JFrame implements ActionListener , ReceiveEventListener {
    private static final long serialVersionUID = 1L;
    private JTextField tmp = new JTextField(10);
    private JTextField hum = new JTextField(10);
    private JButton ledOn = new JButton("LED_ON");
    private JButton ledOff = new JButton("LED_OFF");
    private JLabel msg = new JLabel("온습도 모니터링");
    private JTextArea out = new JTextArea(20,40);
    private JPanel panel = new JPanel();
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private ScrollPane sp = new ScrollPane();
    private MqttClass mqtt = null;

    public IotFrame(MqttClass mqtt){
        this();
        this.mqtt = mqtt;
        this.mqtt.setMyEventListener(this);
    }

    public IotFrame() {
        super("MQTT 사물 인터넷 통신 프로젝트");
        setSize(400,400);
        panel.add(msg);
        panel.add(ledOn);
        panel.add(ledOff);
        panel1.add(tmp);
        panel1.add(hum);
        sp.add(out);
        add(BorderLayout.NORTH, panel); // jframe에 div
        add(BorderLayout.CENTER, sp);
        add(BorderLayout.SOUTH,panel1);

        ledOn.addActionListener(this); // 통지할 수 있게 설정
        ledOff.addActionListener(this); // 통지할 수 있게 설정
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton b = (JButton) e.getSource();
        if (b.getText().equals("LED_ON")){
            mqtt.sendMessage("1");
        }else if(b.getText().equals("LED_OFF")){
            mqtt.sendMessage("2");
        }
    }

    @Override
    public void recvMsg(String topic, MqttMessage msg) {
        System.out.println(topic+", "+msg);
        String append = out.getText();
        out.setText(topic+","+msg+"\n"+append);
        JSONObject obj = new JSONObject(new String(msg.getPayload()));
        tmp.setText("온도 :"+obj.get("tmp").toString());
        hum.setText("습도 :" +obj.get("hum").toString());

    }
}
```

`ReceiveEventListner.java`

```java
public interface ReceiveEventListener {
    public void recvMsg(String topic , MqttMessage msg);
}

```

`Project05_F.java`

```java
public class Project05_F {
    public static void main(String[] args) {
        new MqttClass();
    }
}

```