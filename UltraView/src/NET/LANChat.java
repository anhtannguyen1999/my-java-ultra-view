package NET;

import java.io.IOException;

public interface LANChat {
	public void run();
	public void SendMessage(String message);
	public void open() throws IOException;
	public void close() throws IOException;
	public void StopChat();
}
