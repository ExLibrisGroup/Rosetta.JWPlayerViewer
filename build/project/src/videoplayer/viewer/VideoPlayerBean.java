package videoplayer.viewer;

import java.util.Map;


public class VideoPlayerBean {

	private Map<String,String[]> videoPlayerParameters;

	public VideoPlayerBean(Map<String,String[]> videoPlayerParameters) {
		this.videoPlayerParameters = videoPlayerParameters;
	}

	public String getVolume() {
		return (videoPlayerParameters.get("volume") != null) ? videoPlayerParameters.get("volume")[0] : "50";
	}

	public String getSkin() {
		return "skins/" + ((videoPlayerParameters.get("skin") != null) ? videoPlayerParameters.get("skin")[0] :"playcasso.zip");
	}

	public boolean isAutoStart() {
		if(videoPlayerParameters.get("autoStart") != null) {
			return videoPlayerParameters.get("autoStart")[0].equals("true") ? true : false;
		}

		return false;
	}

	public boolean isMetadata() {
		if(videoPlayerParameters.get("metadata") != null) {
			return videoPlayerParameters.get("metadata")[0].equals("true") ? true : false;
		}

		return false;
	}

	public boolean isAudioView() {
		if(videoPlayerParameters.get("audioView") != null) {
			return videoPlayerParameters.get("audioView")[0].equals("true") ? true : false;
		}

		return false;
	}

}
