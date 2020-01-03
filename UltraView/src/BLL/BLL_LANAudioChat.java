package BLL;

public interface BLL_LANAudioChat {
	public void StartSocketAndInitAudio();
	public void StartRecordingAndSending();
	public void StopRecordingAndSending();
	public void StartReceivingAndSpeaking();
	public void StopReceivingAndSpeaking();
}
