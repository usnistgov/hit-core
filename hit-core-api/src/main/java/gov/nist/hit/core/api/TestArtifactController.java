/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */

package gov.nist.hit.core.api;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.exception.MessageException;
import gov.nist.hit.core.service.exception.TestCaseException;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */

@RequestMapping("/artifact")
@RestController
public class TestArtifactController {

//	static final Logger logger = LoggerFactory.getLogger(TestArtifactController.class);
	static final Logger logger = LogManager.getLogger(TestArtifactController.class);


	@Autowired
	private Streamer streamer;

	@RequestMapping(value = "/download", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded; charset=UTF-8")
	public String download(
			@ApiParam(value = "the path of the artifact", required = true) @RequestParam("path") String path,
			@ApiParam(value = "the title to give to the document", required = true) @RequestParam(value = "title", required = false) String title,
			HttpServletRequest request, HttpServletResponse response) throws MessageException {
		try {

			if (path != null && (path.endsWith("pdf") || path.endsWith("docx"))) {
				String fileName = title == null ? path.substring(path.lastIndexOf("/") + 1)
						: title + path.substring(path.lastIndexOf("."));
				InputStream content = null;
				if (!path.startsWith("/")) {
					content = TestArtifactController.class.getResourceAsStream("/" + path);
				} else {
					content = TestArtifactController.class.getResourceAsStream(path);
				}
				String contentType = path.endsWith("pdf") ? "application/pdf"
						: "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
				response.setContentType(contentType);
				response.setHeader("Content-disposition", "attachment;filename=" + fileName);
				streamer.stream(response.getOutputStream(), content);
			}
			throw new IllegalArgumentException("Invalid Path Provided");
		} catch (IOException e) {
			logger.debug("Failed to download the test package ");
			throw new TestCaseException("Cannot download the artifact " + e.getMessage());
		}

	}

}
