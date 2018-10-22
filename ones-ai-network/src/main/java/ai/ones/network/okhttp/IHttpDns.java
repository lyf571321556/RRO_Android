package ai.ones.network.okhttp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by administrator on 2018/10/22.
 */
public interface IHttpDns {
    List<InetAddress> lookup(String hostname) throws UnknownHostException;
}