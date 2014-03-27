package videoplayer.viewer;

public class VideoFile {
	private String pid;
	private String label;
	private String imagePath = "";

	public VideoFile(String pid, String lable) {
		this.pid = pid;
		this.label = lable;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return "file pid: "+pid;
	}


	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
