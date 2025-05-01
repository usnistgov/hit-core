package gov.nist.hit.core.domain.slicings;

import java.util.ArrayList;
import java.util.List;

public class SegmentSlicing {

	private List<SegmentSlicingMessageContext> messages = new ArrayList<SegmentSlicingMessageContext>();
	

	public SegmentSlicing() {
		super();
	}
		


	public List<SegmentSlicingMessageContext> getMessages() {
		return messages;
	}


	public void setMessages(List<SegmentSlicingMessageContext> messages) {
		this.messages = messages;
	}

	
	
	

	
}
