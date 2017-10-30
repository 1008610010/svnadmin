package org.svnadmin.entity;

/**
 * 项目用户 。只对单库方式有用,包括svn协议和http协议(单库)，可以每个项目设置用户的密码
 * 
 * @author <a href="mailto:yuanhuiwu@gmail.com">Huiwu Yuan</a>
 * 
 */
public class PjUsr extends Usr {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5863709168694149752L;
	/**
	 * 项目ID
	 */
	private Integer pjId;

	/**
	 * @return 项目ID
	 */
	public Integer getPjId() {
		return pjId;
	}

	/**
	 * @param pj
	 *            项目ID
	 */
	public void setPjId(Integer pjId) {
		this.pjId = pjId;
	}

}
