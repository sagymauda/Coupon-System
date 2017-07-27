package ConnectionPool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import Exceptions.connectionException;

public class ConnectionPool {
	private Set<Connection> pool = new HashSet<Connection>();
	private final static int NUM_CONNECTIONS = 10;
	private static ConnectionPool instance;

	/**
	 * constructor initializes the connection pool by opening a predetermined
	 * number of connections in a set.
	 * 
	 * private constructor in order to allow the ConnectionPool to be
	 * initialized only within the class. this method is provoked only once by
	 * the getInstance method.
	 * 
	 * @throws connectionException
	 *             if a database access error occurs and connections cannot be
	 *             opened.
	 */
	private ConnectionPool() throws connectionException {

		try (BufferedReader in = new BufferedReader(new FileReader("files/DBconnection"));) {
			String url = in.readLine();
			while (pool.size() < NUM_CONNECTIONS) {
				pool.add(DriverManager.getConnection(url));
			}
		} catch (IOException e) {
			throw new connectionException("unable to create connectionPool, URL file not found", e);

		} catch (SQLException e) {

			throw new connectionException("unable to create connectionPool, could not connect to database", e);
		}
	}

	/**
	 * getInstance returns the only existing ConnectionPool object, if one was
	 * not yet created- initializes the instance as a new connectionPool.
	 * 
	 * @return the only ConnectionPool instance
	 * @throws connectionException
	 *             if a database access error occurs
	 * 
	 */

	public static ConnectionPool getInstance() throws connectionException {
		if (instance == null) {
			instance = new ConnectionPool();
		}

		return instance;
	}

	/**
	 * getConnection retrieves a connection from the connection pool. if all
	 * connections are not available the thread will wait until one is returned
	 * to the pool.
	 * 
	 * @return a connection from the connection pool
	 * @throws connectionException
	 *             if a database access error occurs or if the thread was
	 *             Interrupted while waiting for a connection.
	 * 
	 */

	public synchronized Connection getConnection() throws connectionException {
		try {
			while (pool.isEmpty()) {
				wait();
			}
			Iterator<Connection> it = pool.iterator();
			Connection con = it.next();
			pool.remove(con);
			notify();
			return con;
		} catch (InterruptedException e) {

			throw new connectionException("unable to retrieve connection for you", e);
		}
	}

	/**
	 * returnConnection returns a connection to the connection pool. once a
	 * connection is returned , the thread notifies all waiting threads that the
	 * pool is no longer empty.
	 * 
	 * 
	 * @param connection
	 *            a connection to be returned to the connection pool.
	 */

	public synchronized void returnConnection(Connection connection) {
		pool.add(connection);
		notify();
	}

	/**
	 * closeAllConnections shuts down the entire connection pool by iterating
	 * over the set and closing each connection .
	 * 
	 * @throws connectionException
	 *             if a database access error occurs and the connections cannot
	 *             be closed.
	 * 
	 */
	public synchronized void closeAllConnections() throws connectionException {
		try {
			Iterator<Connection> it = pool.iterator();
			while (it.hasNext()) {
				it.next().close();
			}
			System.out.println("all connections are closed");
		} catch (SQLException e) {

			throw new connectionException(" unable to close connections-database access error occured", e);
		}
	}

}
