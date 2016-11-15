package Service;

import model.Item;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListSortUtil {

	public static void sort(List<Item> list, final String sortMode) {

		Collections.sort(list, new Comparator<Item>() {  
            public int compare(Item arg0, Item arg1) {  
                double length0 = arg0.getLength();
                double length1 = arg1.getLength();
                if ("SHORT_TO_LONG".equals(sortMode)) {
                	return Double.compare(length0, length1);
                } else {
                	return Double.compare(length1, length0);
                } 
            }  
        }); 
	}
}
