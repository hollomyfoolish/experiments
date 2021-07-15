package cn.hollomyfoolish.mq;

import com.rabbitmq.client.Address;

import java.util.ArrayList;
import java.util.List;

public interface MQConst {
    String HOST = "c02yg4s5jg5j";
    int PORT = 5672;
    String USER = "admin";
    String PASSWORD = "admin";
    String VIRTUAL_HOST = "/";
    String BROAD_EX_NAME = "b1 broadcast";
    String SPRING_VIRTUAL_HOST = "spring";

    List<Address> cluster = new ArrayList<Address>(){
        {
            this.add(new Address(HOST, 5672));
            this.add(new Address(HOST, 5673));
            this.add(new Address(HOST, 5674));
        }
    };

}
