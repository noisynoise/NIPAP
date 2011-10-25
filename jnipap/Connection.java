package jnipap;

import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * A singelton class containing a connection to the Jnipap XML-RPC server
 *
 * This class is used by all NIPAP mapped objects to obtain a connection to
 * the NIPAP XML-RPC server.
 *
 * @author Lukas Garberg <lukas@spritelink.net>
 */
public class Connection {

	public XmlRpcClient connection;
	private XmlRpcClientConfigImpl config;

	private static Connection _instance;


	/**
	 * Creates a JnipapConnection
	 *
	 * The constructor is made private as it is only called from the
	 * getInstance-method.
	 *
	 * @param srv_url URL to the NIPAP server
	 */
	private Connection(URL srv_url) {

		// Create configuration object
		this.config = new XmlRpcClientConfigImpl();
		this.config.setServerURL(srv_url);
		this.config.setBasicUserName("dev@local");
		this.config.setBasicPassword("dev");
		this.config.setEnabledForExtensions(true);

		// Create client object
		this.connection = new XmlRpcClient();
		this.connection.setConfig(this.config);
		this.connection.setTypeFactory(new NonExNullParser(this.connection));

	}

	/**
	 * Get an instance of the JnipapConnection
	 *
	 * If no instance previously has been created an error will be thrown.
	 *
	 * @return A reference to the JnipapConnection object
	 */
	public static Connection getInstance() {

		// Throw exception if no connection previously has been created
		if (_instance == null) {
			throw new JnipapConnectionError("JnipapConnection not configured. Specify URL first!");
		}

		return _instance;

	}

	/**
	 * Get an instance of the JnipapConnection
	 *
	 * If no instance of the JnipapConnection exists, a new one will be created
	 * to the specified URL. Otherwise, the old object is returned and the URL ignored.
	 *
	 * Should this behavior be changed to alter the URL?
	 *
	 * @param srv_url URL to the NIPAP server
	 * @return A reference to the JnipapConnection object
	 */
	public static Connection getInstance(URL srv_url) {

		// Create new instance if none exist.
		if (_instance == null) {
			_instance = new Connection(srv_url);
		}

		return _instance;

	}
}

class JnipapConnectionError extends RuntimeException {

	/**
	 * Constructor
	 *
	 * @param msg Error message
	 * @return JnipapConnectionError exception
	 */
	public JnipapConnectionError(String msg) {
		super(msg);
	}

}
