package backend.team.backend.client.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTT {
    //https://riptutorial.com/mqtt/example/28353/example-of-publish-subscriber-in-java

    private String pubTopic = "BODEGA/1/OPEN_DOOR";
    private int qos = 0;
    private String broker = "xxxxxx";
    private String user = "xxxxxx";
    private String password = "xxxxxx";
    private String content = "open";


    public void publish(){

        try{
            MqttClient client = new MqttClient(broker,String.valueOf(System.nanoTime()));

            //MQTT connection option
            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setUserName(user);
            connOpts.setPassword(password.toCharArray());

            connOpts.setCleanSession(true); //no persistent session
            connOpts.setKeepAliveInterval(1000);



            //establish a connection
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");

            //publish a message
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            message.setRetained(true); //sets retained message
            client.publish(pubTopic, message);
            System.out.println("Message published");

            //disconnect client
            client.disconnect();
            System.out.println("Disconnected");
            client.close();

        }catch (MqttException me){
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

}
