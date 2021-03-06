package restFTP.service;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * This class provide an easy way to handle communications with to FTP Server.
 * 
 * @author Arthur Dewarumez
 * 
 */
public class FTPService {

	private final String hostName = "localhost";
	private final int port = 9999;
	private final FTPClient ftpClient;
	private String login;
	private static FTPService instance = null;

	public static synchronized FTPService getInstance() {
		if (FTPService.instance == null) {
			FTPService.instance = new FTPService();
		}
		return FTPService.instance;
	}

	// /**
	// * Create a new FTPConnector to the specify address and port
	// *
	// * @param address
	// * the address of the FTP server
	// * @param port
	// * the port on which the server listen.
	// */
	// public FTPService(final String hostName, final int port) {
	// this.hostName = hostName;
	// this.port = port;
	// this.ftpClient = new FTPClient();
	// this.login = null;
	// }

	private FTPService() {
		this.ftpClient = new FTPClient();
		this.login = null;
	}

	/**
	 * Return if the passerelle is connected to the FTP server.
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return this.ftpClient.isConnected();
	}

	/**
	 * Connect the FTPConnector to the server specified during the
	 * instantiation.
	 */
	public boolean connect() {
		System.out.println("[Server] New attempt to login.");
		try {
			this.ftpClient.connect(this.hostName, this.port);
			final int reply = this.ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.ftpClient.disconnect();
				return false;
			}
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
		this.ftpClient.enterLocalActiveMode();
		System.out.println("[Server] Successfull login.");
		return true;
	}

	/**
	 * Login to the FTP server with the username and password given.
	 * 
	 * @param userName
	 *            the username to use
	 * @param password
	 *            the password to use
	 * @return True if the login is successful. False otherwise
	 */
	public boolean login(final String userName, final String password) {
		this.login = userName;
		boolean result = false;
		try {
			result = this.ftpClient.login(userName, password);
		} catch (final IOException e) {
			System.out.printf(
					"[%s] I/O error occured while attempting a new login.\n",
					this.login);
			e.printStackTrace();
			return false;
		}
		if (result) {
			System.out.printf("[%s] Successfull login.\n", this.login);
		} else {
			System.out.printf("[%s] Bad password or login.\n", this.login);
		}
		return result;
	}

	/**
	 * Change the working directory to one given.
	 * 
	 * @param pathname
	 *            the new working directory
	 * @return true if successfully completed. false otherwise.
	 */
	public boolean changeDirectory(final String pathname) {
		final boolean result = false;
		try {
			this.ftpClient.changeWorkingDirectory(pathname);
		} catch (final IOException e) {
			System.out
					.printf("[%s] I/O error occured while setting a new working directory.\n",
							this.login);
			e.printStackTrace();
		}
		if (result) {
			System.out.printf("[%s] New working directory %s\n.", this.login,
					pathname);
			return true;
		}
		System.out.printf(
				"[%s] Error while setting the new working directory.\n",
				this.login);
		return false;
	}

	/**
	 * Create a new directory in the current working directory.
	 * 
	 * @param directory
	 *            the name of the new directory
	 * @return true if successfully completed, false otherwise.
	 */
	public boolean createDirectory(final String directory) {
		boolean result = false;
		try {
			result = this.ftpClient.makeDirectory(directory);
			if (result) {
				System.out.printf("[%s] New directory created.\n", this.login);
			}
		} catch (final IOException e) {
			System.out.printf("[%s] Error while creating the new directory.\n",
					this.login);
			e.printStackTrace();
			return false;
		}

		return result;
	}

	/**
	 * Create a new file.
	 * 
	 * @param remote
	 *            the name of the new file. If this file name contains a
	 *            directory name, create the new file in.
	 * @param local
	 *            the InputStream of the new file
	 * @return return true if the file is created. False otherwise. The file is
	 *         not created, if the directory name does not exist.
	 */
	public boolean createFile(final String remote, final InputStream local) {
		try {
			if (this.ftpClient.storeFile(remote, local)) {
				System.out.printf("[%s] New file %s created.\n", this.login,
						remote);
				return true;
			}
		} catch (final IOException e) {
			System.out.printf("[%s] Error while creating a new file.\n",
					this.login);
			e.printStackTrace();
		}
		return false;
	}

	// Ne marche pas. Je ne sais pas pourquoi.
	// /**
	// * List the files of the directory given
	// *
	// * @param pathname
	// * the directory to use
	// * @return a list of filename for every files in the directory
	// * @throws FTPConnectionClosedException
	// */
	// public List<String> listDirectory(final String pathname) {
	// final List<String> result = new LinkedList<>();
	// try {
	// final FTPFile[] filenames = this.ftpClient.listFiles(pathname);
	// for (int i = 0; i < filenames.length; i++) {
	// System.out.println(filenames[i]);
	// result.add(filenames[i].getName());
	// }
	// } catch (final IOException e) {
	// System.out.printf("[%s] Error while listing files\n.", this.login);
	// e.printStackTrace();
	// result.add("Impossible to list files for the path " + pathname);
	// }
	// return result;
	// }
}
