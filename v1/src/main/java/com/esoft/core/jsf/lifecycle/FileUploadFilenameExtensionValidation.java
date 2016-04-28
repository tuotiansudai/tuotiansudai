package com.esoft.core.jsf.lifecycle;

import java.util.List;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.core.IsInstanceOf;
import org.omnifaces.util.Components;
import org.omnifaces.util.Messages;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.webapp.MultipartRequest;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import com.ocpsoft.pretty.faces.servlet.PrettyFacesWrappedRequest;

/**
 * 检查上传文件的扩展名是否合法
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-6-24 上午10:49:28
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-6-24 wangzhi 1.0
 */
public class FileUploadFilenameExtensionValidation implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforePhase(PhaseEvent event) {
		final FacesContext fc = event.getFacesContext();
		HttpServletRequestWrapper request = (HttpServletRequestWrapper) fc
				.getCurrentInstance().getExternalContext().getRequest();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileUpload> fileUploads = Components.findComponentsInChildren(
					fc.getViewRoot(), FileUpload.class);
			for (FileUpload fileUpload : fileUploads) {
				String allowType = fileUpload.getAllowTypes();
				allowType = allowType.substring(1, allowType.length());
				allowType = allowType.substring(0, allowType.length() - 1);

				FileItem file = null;
				while (request instanceof HttpServletRequestWrapper
						&& !(request instanceof MultipartRequest)) {
					request = (HttpServletRequestWrapper) request.getRequest();
				}
				file = ((MultipartRequest) request).getFileItem(fileUpload
						.getClientId());
				if (file == null) {
					continue;
				}

				// 判断文件扩展名是否被允许
				boolean isExtValid = false;
				String filenameExt = null;

				int dot = file.getName().lastIndexOf('.');
				if ((dot > -1) && (dot < (file.getName().length() - 1))) {
					filenameExt = "." + file.getName().substring(dot + 1);
				}
				if (StringUtils.isNotEmpty(filenameExt)
						&& Pattern.matches(allowType, filenameExt)) {
					isExtValid = true;
				}
				if (!isExtValid) {
					fc.addMessage(null, Messages.createError(
							"filename extension:{0} is illegal!",
							file.getName()));
					fc.renderResponse();
				}
			}
		}

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.APPLY_REQUEST_VALUES;
	}

}
