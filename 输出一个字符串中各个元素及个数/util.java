import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


public class Utils {

	public static void getSort(String str) {
		//查阅API，将str转换为char类型的数组。
		char[] chars = str.toCharArray();
		//定义一个TreeMap集合，用来存放字符和次数的对应关系
		TreeMap<Character, Integer> map=new TreeMap<Character, Integer>();
		//循环取出数组中的元素
		for (int i = 0; i < chars.length; i++) {
			//获取map集合中字符出现的次数
			Integer value = map.get(chars[i]);
			if(value==null){
				map.put(chars[i], 1);
			}else{
				value+=1;
				map.put(chars[i], value);
			}
			
		}
		//遍历打印输出
		Set<Character> keySet = map.keySet();
		Iterator<Character> it = keySet.iterator();
		while(it.hasNext()){
			Character key = it.next();
			Integer value = map.get(key);
			System.out.print(key);
			System.out.print("("+value+")");
		}
	}

	
}
