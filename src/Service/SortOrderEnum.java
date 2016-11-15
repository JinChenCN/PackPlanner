package Service;

public enum SortOrderEnum {
	NATURAL("NATURAL"), ASC("SHORT_TO_LONG,"), DESC("LONG_TO_SHORT");  
    private String name;  
    private SortOrderEnum(String name) {  
        this.name = name;  
    }  
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
