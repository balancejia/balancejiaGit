/**
 *
 */
package com.thx.fireWater.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.thx.common.struts2.BaseAction;
import com.thx.common.util.AppContext;

/**
 * @author balancejia
 *
 */
@Namespace("/resource")
@Controller(value = "siteResourceAction")
@Scope("prototype")
public class SiteResourceAction extends BaseAction {

	private static final long serialVersionUID = -1724435544372719500L;

	private String siteId;
	private String index;

	@Action(value = "img")
	public String loadImg() {

		String dir = AppContext.getProperties("resource_img_dir");

		String path = dir + "/" + siteId + "/" + index + ".jpg";

		try {

			File file = new File(path);

			if (siteId == null || index == null || !file.exists())
				file = new File(dir + "/default.jpg");

			BufferedImage image = ImageIO.read(file);

			HttpServletResponse response = getResponse();

			OutputStream os = response.getOutputStream();
			ImageIO.write(image, "JPEG", os);
			os.flush();
			os.close();
			os = null;
			response.flushBuffer();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Action(value = "doc")
	public String loadDoc() {
		String dir = AppContext.getProperties("resource_doc_dir");
		String path = dir + "/" + siteId + "/" + index + ".doc";

		return null;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
