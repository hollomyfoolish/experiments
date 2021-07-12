package cn.hollomyfoolish;

import org.junit.jupiter.api.Test;
import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        assertTrue( true );
    }

    public static void main(String[] args) {
    }
}
