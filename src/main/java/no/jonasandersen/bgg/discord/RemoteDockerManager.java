package no.jonasandersen.bgg.discord;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class RemoteDockerManager {

  public static class SSHException extends Exception {

    public SSHException(Throwable e) {
      super(e);
    }
  }

  public static String executeSsh(String host, String user, String password, String command)
      throws SSHException {
    StringBuilder output = new StringBuilder();
    try {
      JSch jsch = new JSch();
      Session session = jsch.getSession(user, host, 22);
      session.setPassword(password);

      Properties properties = new Properties();
      properties.put("StrictHostKeyChecking", "no");
      properties.put("PreferredAuthentications", "password");

      session.setConfig(properties);
      session.connect();

      ChannelExec channel = (ChannelExec) session.openChannel("exec");
      channel.setCommand(command);
      channel.setErrStream(System.err);

      // Use BufferedReader for cleaner text handling
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(channel.getInputStream()))) {
        channel.connect();
        String line;
        while ((line = reader.readLine()) != null) {
          output.append(line).append("\n");
        }
      }

      channel.disconnect();
      session.disconnect();
    } catch (Exception e) {
      throw new SSHException(e);
    }
    return output.toString().trim();
  }
}
