package request;

public class PageInfo {
	private int nowPage = 0;
	private String searchColumn = "";
	private String searchWord = "";

	/**
	 * 기본 생성자
	 */
	public PageInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nowPage
	 * @param searchColumn
	 * @param searchWord
	 */
	public PageInfo(int nowPage, String searchColumn, String searchWord) {
		super();
		this.nowPage = nowPage;
		this.searchColumn = searchColumn;
		this.searchWord = searchWord;
	}

	/**
	 * @return the nowPage
	 */
	public int getNowPage() {
		return nowPage;
	}

	/**
	 * @param nowPage the nowPage to set
	 */
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	/**
	 * @return the searchColumn
	 */
	public String getSearchColumn() {
		return searchColumn;
	}

	/**
	 * @param searchColumn the searchColumn to set
	 */
	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}

	/**
	 * @return the searchWord
	 */
	public String getSearchWord() {
		return searchWord;
	}

	/**
	 * @param searchWord the searchWord to set
	 */
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
}
